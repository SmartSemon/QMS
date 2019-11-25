package com.usc.app.activiti.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usc.app.activiti.ActCommonUtil;
import com.usc.app.activiti.service.ProcessService;
import com.usc.app.bs.service.impl.BaseService;
import com.usc.app.util.Utils;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.test.mate.resource.ServiceToWbeClientResource;

@Service("processService")
public class ProcessServiceImpl extends BaseService implements ProcessService
{

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	HistoryService historyService;

	@Autowired
	TaskService taskService;

	@Override
	public Dto getProcdefProcess()
	{
		// 获取所有流程定义
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		// 处理数据获取最新版本的流程定义
		Map<String, ProcessDefinition> map = new LinkedHashMap<>();
		for (ProcessDefinition pd : list)
		{
			map.put(pd.getKey(), pd);
		}
		// 获取list集合
		ArrayList<Object> processDefinitions = new ArrayList<>();
		for (ProcessDefinition pd : map.values())
		{
			// 根据DeploymentId查询deployment
			List<Deployment> deployment = repositoryService.createDeploymentQuery().deploymentId(pd.getDeploymentId())
					.list();
			Dto dto = new MapDto();
			for (Deployment dm : deployment)
			{
				dto.put("deploymentTime", dm.getDeploymentTime());
			}
			Dto processDefinition = new MapDto();
			processDefinition.put("deploymentId", pd.getDeploymentId());
			processDefinition.put("id", pd.getId());
			processDefinition.put("name", pd.getName());
			processDefinition.put("version", pd.getVersion());
			processDefinition.put("png", pd.getDiagramResourceName());
			processDefinition.put("deployTime", dto.get("deploymentTime"));
			// 获取次流程定义是否激活挂起(1:false-激活;2:true-挂起)
			processDefinition.put("suspensionState", repositoryService.isProcessDefinitionSuspended(pd.getId()));
			processDefinitions.add(processDefinition);
		}
		return new MapDto("list", processDefinitions);
	}

	@Override
	public Dto deleteByDeploymentId(String deploymentId)
	{
		repositoryService.deleteDeployment(deploymentId, true);
		return new MapDto("result", "success");
	}

	@Override
	public Dto suspension(String id)
	{
		repositoryService.suspendProcessDefinitionById(id);
		return new MapDto("result", "success");
	}

	@Override
	public Dto activation(String id)
	{
		repositoryService.activateProcessDefinitionById(id);
		return new MapDto("result", "success");
	}

	@Override
	public void getProcessPicture(String id, HttpServletResponse response) throws IOException
	{
		// 解决中文乱码问题
		processEngineConfiguration.setActivityFontName("黑体");
		processEngineConfiguration.setAnnotationFontName("宋体");
		processEngineConfiguration.setLabelFontName("微软雅黑");
		// 获取流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id)
				.singleResult();
		// 获取流程图文件名称 此种方法获取图片会有中文乱码问题
		String resourceName = processDefinition.getDiagramResourceName();
		InputStream in = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		OutputStream outputStream = response.getOutputStream();
		int len;
		byte[] buf = new byte[1024];
		while ((len = in.read(buf, 0, 1024)) != -1)
		{
			outputStream.write(buf, 0, len);
		}
		outputStream.close();
	}

	@Override
	public Dto startProcess(String queryParam) throws IOException
	{
		Dto param = new MapDto();
		if (!Utils.isEmpty(queryParam))
		{
			param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
		}
		Dto result = new MapDto();
		// 流程发起前设置发起人，记录在流程历史中
		identityService.setAuthenticatedUserId(param.getString("userName"));
		// 根据流程定义ID启动流程并且设置流程变量
		Map<String, Object> map = new HashMap<>();
		map.put("itemNo", param.getString("itemNo"));
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(param.getString("id"), map);
		// 根据流程定义ID启动流程
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById(param.getString("id"));
		// 修改业务数据状态
		boolean isRestore = ActCommonUtil.restore(processInstance.getProcessInstanceId(), param.getString("userName"),
				"1", processInstance.getProcessInstanceId(), (List<HashMap<String, Object>>) param.get("selectedRows"));
		if (isRestore)
		{
			result.put("result", true);
			return result;
		} else
		{
			// 业务数据没修改成功流程启动后先把流程结束再删除
			runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "启动失败");
			historyService.deleteHistoricProcessInstance(processInstance.getProcessInstanceId());
			result.put("result", false);
			return result;
		}
	}

	@Override
	public List<Dto> getRunProcess()
	{
		// 创建查询所有未完成流程实例，(act_hi_procinst表)，根据开始时间降序排序
		List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery().unfinished()
				.orderByProcessInstanceStartTime().desc().list();
		List<Dto> list = new ArrayList<>();
		if (hpiList != null && hpiList.size() > 0)
		{
			// 遍历流程实例根据流程实例获取最新流程任务(未完成的任务)
			for (HistoricProcessInstance hp : hpiList)
			{
				// 根据流程实例ID获取未完成的最新流程节点任务
				HistoricActivityInstance hai = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(hp.getId()).unfinished().singleResult();
				Dto dto = new MapDto();
				// id(act_hi_actinst表)
				dto.put("id", hai.getId());
				// 流程实例ID(方便对活动节点进行操作)
				dto.put("processInstanceId", hai.getProcessInstanceId());
				// 流程标题（发起人+发起时间+流程实例名称）
				dto.put("title", hp.getStartUserId() + "在" + Utils.dateToString(hp.getStartTime()) + "发起"
						+ hp.getProcessDefinitionName());
				// 流程名称
				dto.put("name", hp.getProcessDefinitionName());
				// 状态
				dto.put("state", hai.getActivityName());
				// 发起人
				dto.put("startName", hp.getStartUserId());
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public void getActivityPng(String processInstanceId, HttpServletResponse response) throws IOException
	{
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 获取历史流程实例
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 获取流程定义
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(hpi.getProcessDefinitionId());

		// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
		List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
		// 已执行的节点ID集合
		List<String> executedActivityIdList = new ArrayList<>();
		for (HistoricActivityInstance activityInstance : haiList)
		{
			executedActivityIdList.add(activityInstance.getActivityId());
		}
		// 已执行的线集合
		List<String> flowIds;
		// 获取流程走过的线
		flowIds = ActCommonUtil.getHighLightedFlows(processDefinitionEntity, haiList);
		BpmnModel bpmnModel = repositoryService.getBpmnModel(hpi.getProcessDefinitionId());
		// 获取流程图图像字符流
		InputStream imageStream = processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel,
				"png", executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", null, 2.0);

		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		int bytesRead;
		byte[] buffer = new byte[8192];
		while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1)
		{
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		imageStream.close();
	}

	@Override
	public List<Dto> getProcessReverseList(String queryParam) throws IOException
	{
		Dto param = new MapDto();
		if (!Utils.isEmpty(queryParam))
		{
			param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
		}
		// 获取流程历史中startEvent节点
		List<HistoricActivityInstance> startList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(param.getString("processInstanceId")).activityType("startEvent").list();
		// 获取流程历史中所有userTask节点，并按照节点在流程中结束时升序排序
		List<HistoricActivityInstance> userTaskList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(param.getString("processInstanceId")).activityType("userTask")
				.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();
		// 获取流程历史中startEvent节点
		List<HistoricActivityInstance> endtList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(param.getString("processInstanceId")).activityType("endEvent").list();
		startList.addAll(userTaskList);
		startList.addAll(endtList);
		List<Dto> list = new ArrayList<>();
		for (HistoricActivityInstance hai : startList)
		{
			Dto dto = new MapDto();
			// 执行节点id
			dto.put("id", hai.getId());
			// 执行环节
			dto.put("actName", hai.getActivityName());
			// 执行人(因为节点开始没有执行人信息，所以根据processInstanceId获取流程历史实例获取发起人)
			if ("startEvent".equals(hai.getActivityType()))
			{
				// 获取历史流程实例
				HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
						.processInstanceId(param.getString("processInstanceId")).singleResult();
				dto.put("assignee", hpi.getStartUserId());
			} else
			{
				dto.put("assignee", hai.getAssignee());
			}
			// 开始时间
			dto.put("startTime", Utils.dateToString(hai.getStartTime()));
			// 结束时间
			dto.put("endTime", Utils.dateToString(hai.getEndTime()));
			// 提交建议（根据活动节点获取taskid，根据taskid获取act_hi_comment表的任务提交建议）
			List<Comment> comment = taskService.getTaskComments(hai.getTaskId());
			if (comment.size() > 0)
			{
				dto.put("subOpinions", comment.get(0).getFullMessage());
			} else
			{
				dto.put("subOpinions", null);
			}
			// 任务历时(毫秒)
			dto.put("duration", Utils.msToHms(hai.getDurationInMillis()));
			list.add(dto);
		}
		return list;
	}

	@Override
	public Dto getProcessSubList(String queryParam) throws Exception
	{
		Dto param = new MapDto();
		if (!Utils.isEmpty(queryParam))
		{
			param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
		}
		/// 先判断流程是否结束
		ProcessInstance singleResult = runtimeService.createProcessInstanceQuery()
				.processInstanceId(param.getString("processInstanceId")).singleResult();
		String itemNo;
		if (singleResult != null)
		{
			// 获取对象标识
			itemNo = (String) runtimeService.getVariable(param.getString("processInstanceId"), "itemNo");
		} else
		{
			// 获取对象标识
			HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(param.getString("processInstanceId")).variableName("itemNo").singleResult();
			itemNo = (String) hvi.getValue();
		}
		Dto dto = new MapDto();
		dto.put("itemNo", itemNo);
		dto.put("facetype", 2);
		dto.put("itemGridNo", "default");
		dto.put("itemPropertyNo", "default");
		dto.put("itemRelationPageNo", "default");
		dto.put("userName", param.getString("userName"));
		dto.put("condition", "dsno=" + param.getString("processInstanceId") + " ORDER BY id DESC");
		String params = ActCommonUtil.toJsonString(dto);
		ServiceToWbeClientResource serviceToWbeClientResource = new ServiceToWbeClientResource();
		Map<String, Object> resultModel = serviceToWbeClientResource.GetModelData(params);
		Object resultItemList = serviceToWbeClientResource.getDataListLimit(params);
		Dto subList = new MapDto();
		subList.put("resultModel", resultModel);
		subList.put("resultItemList", resultItemList);
		return subList;
	}

	@Override
	public Dto endProcess(String queryParam) throws IOException
	{
		Dto param = new MapDto();
		if (!Utils.isEmpty(queryParam))
		{
			param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
		}
		// 还原流程绑定的数据是否成功
		boolean isRestore = ActCommonUtil.restore(param.getString("processInstanceId"), param.getString("userName"),
				"C", "", null);
		if (isRestore)
		{
			try
			{
				runtimeService.deleteProcessInstance(param.getString("processInstanceId"),
						"[流程作废]原因:" + param.getString("options"));
				Dto resultDto = new MapDto();
				resultDto.put("flag", true);
				resultDto.put("msg", "作废成功！");
				return resultDto;
			} catch (Exception e)
			{
				Dto resultDto = new MapDto();
				resultDto.put("flag", false);
				resultDto.put("msg", "作废失败！");
				return resultDto;
			}
		} else
		{
			Dto resultDto = new MapDto();
			resultDto.put("flag", false);
			resultDto.put("msg", "作废失败(还原业务数据失败)！");
			return resultDto;
		}
	}

	@Override
	public List<Dto> getEndProcess()
	{
		// 创建查询所有已经完成流程实例，(act_hi_procinst表)，根据结束时间降序排序
		List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery().finished()
				.orderByProcessInstanceEndTime().desc().list();
		List<Dto> list = new ArrayList<>();
		if (hpiList != null && hpiList.size() > 0)
		{
			// 遍历流程实例根据流程实例获取最新流程任务(未完成的任务)
			for (HistoricProcessInstance hp : hpiList)
			{
				// 根据流程实例processInstanceId获取已经完成的任务
				List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(hp.getId()).finished().orderByHistoricTaskInstanceEndTime().desc().list();
				Dto dto = new MapDto();
				// id(act_hi_taskinst表)
				dto.put("id", htiList.get(0).getId());
				// 流程实例ID(方便对活动节点进行操作)
				dto.put("processInstanceId", htiList.get(0).getProcessInstanceId());
				// 流程标题（发起人+发起时间+流程实例名称）
				dto.put("title", hp.getStartUserId() + "在" + Utils.dateToString(hp.getStartTime()) + "发起"
						+ hp.getProcessDefinitionName());
				// 流程名称
				dto.put("name", hp.getProcessDefinitionName());
				// 发起人
				dto.put("startName", hp.getStartUserId());
				// 发起时间
				dto.put("startTime", Utils.dateToString(htiList.get(0).getStartTime()));
				// 结束时间
				dto.put("endTime", Utils.dateToString(htiList.get(0).getEndTime()));
				// 流程状态
				dto.put("processState", htiList.get(0).getDeleteReason());
				list.add(dto);
			}
		}
		return list;
	}

	@Override
	public Dto deleteProcess(String queryParam) throws IOException
	{
		Dto param = new MapDto();
		if (!Utils.isEmpty(queryParam))
		{
			param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
		}
		try
		{
			// 删除流程实例删除已完成任务
			historyService.deleteHistoricProcessInstance(param.getString("processInstanceId"));
			Dto resultDto = new MapDto();
			resultDto.put("flag", true);
			resultDto.put("msg", "删除成功！");
			return resultDto;
		} catch (Exception e)
		{
			Dto resultDto = new MapDto();
			resultDto.put("flag", false);
			resultDto.put("msg", "删除失败！");
			return resultDto;
		}

	}
}
