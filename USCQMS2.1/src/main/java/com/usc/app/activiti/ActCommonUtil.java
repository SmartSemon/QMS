package com.usc.app.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.GeneralObject;
import com.usc.test.mate.resource.ServiceToWbeClientResource;

/**
 * @Author: lwp
 * @DATE: 2019/8/29 16:57
 * @Description:
 **/
public class ActCommonUtil
{
	/**
	 * 根据流程实例ID获取存放的流程变量ItemNo
	 *
	 * @param processInstanceId 流程实例Id
	 * @return ItemNo
	 */
	public static String getItemNo(String processInstanceId)
	{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 先判断流程是否结束
		ProcessInstance singleResult = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String itemNo;
		if (singleResult != null)
		{
			// 获取对象标识
			itemNo = (String) processEngine.getRuntimeService().getVariable(processInstanceId, "itemNo");
			return itemNo;
		} else
		{
			// 获取对象标识
			HistoricVariableInstance hvi = processEngine.getHistoryService().createHistoricVariableInstanceQuery()
					.processInstanceId(processInstanceId).variableName("itemNo").singleResult();
			itemNo = (String) hvi.getValue();
			return itemNo;
		}
	}

	/**
	 * 还原流程绑定的数据
	 *
	 * @param processInstanceId 流程实例ID
	 * @param userName          用户
	 * @param state             状态
	 * @param dson              数据包号（流程实例id）
	 * @param rows              业务数据
	 * @return true
	 */
	public static boolean restore(String processInstanceId, String userName, String state, String dson,
			List<HashMap<String, Object>> rows)
	{
		Dto restore = new MapDto();
		// 设置状态为1，（C:初始状态，0:待审批，1:审批中，2:已审批，其它:未知状态）
		restore.put("state", state);
		restore.put("dsno", dson);
		// 获取ItemNo
		String itemNo = ActCommonUtil.getItemNo(processInstanceId);
		List<HashMap<String, Object>> list;
		if (rows != null)
		{
			list = rows;
		} else
		{
			try
			{
				// 获取此次流程的业务数据
				Dto listDto = new MapDto();
				listDto.put("itemNo", itemNo);
				listDto.put("facetype", 2);
				listDto.put("itemGridNo", "default");
				listDto.put("itemPropertyNo", "default");
				listDto.put("itemRelationPageNo", "default");
				listDto.put("userName", userName);
				listDto.put("condition", "dsno=" + processInstanceId + " ORDER BY id DESC");
				String params = ActCommonUtil.toJsonString(listDto);
				ServiceToWbeClientResource serviceToWbeClientResource = new ServiceToWbeClientResource();
				HashMap<String, Object> object = (HashMap<String, Object>) serviceToWbeClientResource
						.getDataListLimit(params);
				list = (List<HashMap<String, Object>>) object.get("dataList");
			} catch (Exception e)
			{
				System.err.println(e);
				return false;
			}
		}
		for (HashMap<String, Object> objectMap : list)
		{
			USCObject uscObject = new GeneralObject(itemNo, objectMap);
			ApplicationContext applicationContext = new ApplicationContext(userName, uscObject);
//            applicationContext.setUserName(userName);
//            applicationContext.setCurrObj(uscObject);
//            applicationContext.setItemNo(itemNo);
			uscObject.setFieldValues(restore);
			uscObject.save(applicationContext);
		}
		return true;
	}

	/**
	 * Dto 转jsonString
	 *
	 * @param paramObject dto
	 * @return string
	 */
	public static String toJsonString(Object paramObject)
	{
		try
		{
			ObjectMapper localObjectMapper = new ObjectMapper();
			localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			return localObjectMapper.writeValueAsString(paramObject);
		} catch (Exception localException)
		{
			localException.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取流程走过的线
	 *
	 * @param processDefinitionEntity   流程实例
	 * @param historicActivityInstances 活动节点实例
	 * @return 活动节点线
	 */
	public static List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances)
	{
		List<String> highFlows = new ArrayList<>();
		// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++)
		{
			// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());
			// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();
			// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++)
			{
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
				// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
				// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime()))
				{
					// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else
				{
					// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
			// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions)
			{
				// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl))
				{
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}

	/**
	 * 判断下一个节点是否为结束节点
	 *
	 * @param taskId 任务节点
	 * @return 布尔
	 */
	public static Boolean nextTaskIsEnd(String taskId)
	{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 参数校验
		// 查询当前任务对象
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		// 根据taskId获取流程实例Id
		String processInstanceId = task.getProcessInstanceId();
		String definitionId = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) ProcessEngines
				.getDefaultProcessEngine().getRepositoryService()).getDeployedProcessDefinition(definitionId);
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(task.getTaskDefinitionKey());
		// 获取节点所有流向线路信息
		List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
		for (PvmTransition tr : outTransitions)
		{
			// 获取线路的终点节点
			PvmActivity ac = tr.getDestination();
			// 当其中一条流线的中点不是结束节点时，直接返回 false（不是结束节点）
			if (!"endEvent".equals(ac.getProperty("type")))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断下一个节点是不是网关节点
	 *
	 * @param taskId 任务节点
	 * @return 布尔
	 */
	// 判断下一节点是不是排他网关
	public static Boolean nextTaskIsExclusiveGateway(String taskId)
	{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 参数校验
		// 查询当前任务对象
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		// 根据taskId获取流程实例Id
		String processInstanceId = task.getProcessInstanceId();
		String definitionId = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) ProcessEngines
				.getDefaultProcessEngine().getRepositoryService()).getDeployedProcessDefinition(definitionId);
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(task.getTaskDefinitionKey());
		// 获取节点所有流向线路信息
		List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
		for (PvmTransition tr : outTransitions)
		{
			// 获取线路的终点节点
			PvmActivity ac = tr.getDestination();
			// 当其中一条流线的中点不是结束节点时，直接返回 false（不是结束节点）
			if (!"exclusiveGateway".equals(ac.getProperty("type")))
			{
				return false;
			}
		}
		return true;
	}
}
