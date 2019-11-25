package com.usc.test.mate.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.usc.app.util.UserInfoUtils;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.autho.UserAuthority;
import com.usc.obj.api.BeanFactoryConverter;
import com.usc.obj.api.bean.UserInformation;
import com.usc.server.DBConnecter;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.jdbc.base.DataBaseUtils;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.USCModelMate;
import com.usc.server.md.mapper.MenuRowMapper;
import com.usc.test.mate.action.MGetClassViewModelData;
import com.usc.test.mate.jsonbean.USCObjectJSONBean;
import com.usc.test.mate.jsonbean.USCObjectQueryJsonBean;

@RestController
@RequestMapping(value = "/sysModelToWbeClient", produces = "application/json;charset=UTF-8")
public class ServiceToWbeClientResource
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/getModelData")
	public Map<String, Object> GetModelData(@RequestBody String queryParam) throws Exception
	{
		USCObjectJSONBean jsonBean = null;
		try
		{
			jsonBean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ItemInfo info = jsonBean.getItemInfo();

		Map<String, Object> resultMap = new HashMap<>();
		if (info == null)
		{
			resultMap.put("flag", true);
			resultMap.put("info", "对象不存在");
			return resultMap;
		}
		String userName = jsonBean.getUSERNAME();
		UserInformation userInformation = UserInfoUtils.getUserInformation(userName);
		String itemGridNo = jsonBean.ITEMGRIDNO;
		String itemPropertyNo = jsonBean.ITEMPROPERTYNO;
		int faceType = jsonBean.FACETYPE;
		String itemRelationPageNo = jsonBean.ITEMRELATIONPAGENO;

		List<ItemMenu> pageMenus = new JdbcTemplate(DBConnecter.getDataSource()).query(
				"SELECT * FROM usc_model_itemmenu WHERE del=0 AND state='F' AND itemid='" + jsonBean.getPAGEID() + "'",
				new MenuRowMapper());
		UserAuthority.authorityMenus(userInformation, pageMenus);
		resultMap.put("flag", true);
		resultMap.put("faceType", faceType);
		resultMap.put("pageMenus", pageMenus);
		ModelUtils.getClientAllPageData(userInformation, resultMap, info, itemGridNo, itemPropertyNo,
				itemRelationPageNo, faceType);

		return resultMap;
	}

	@PostMapping("/getClassViewModelData")
	public Map<String, Object> GetClassViewModelData(@RequestBody String queryParam) throws Exception
	{
		USCObjectJSONBean jsonBean = null;
		Map<String, Object> resultMap = new HashMap<>();
		try
		{
			jsonBean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
		} catch (Exception e)
		{

			e.printStackTrace();
			resultMap.put("flag", false);
			resultMap.put("info", "参数不正确，获取建模数据失败");
			return resultMap;
		}

		ItemInfo info = jsonBean.getItemInfo();

		if (info == null)
		{
			resultMap.put("flag", true);
			resultMap.put("info", "对象不存在");
			return resultMap;
		}
		String userName = jsonBean.getUSERNAME();
		UserInformation userInformation = UserInfoUtils.getUserInformation(userName);
		String itemGridNo = jsonBean.ITEMGRIDNO;
		String itemPropertyNo = jsonBean.ITEMPROPERTYNO;
		int faceType = jsonBean.FACETYPE;
		String itemRelationPageNo = jsonBean.ITEMRELATIONPAGENO;
		List<ItemMenu> pageMenus = new JdbcTemplate(DBConnecter.getDataSource()).query(
				"SELECT * FROM usc_model_itemmenu WHERE del=0 AND state='F' AND itemid='" + jsonBean.getPAGEID() + "'",
				new MenuRowMapper());
		UserAuthority.authorityMenus(userInformation, pageMenus);
		resultMap.put("flag", true);
		resultMap.put("faceType", faceType);
		resultMap.put("pageMenus", pageMenus);
		ModelUtils.getClientAllPageData(userInformation, resultMap, info, itemGridNo, itemPropertyNo,
				itemRelationPageNo, faceType);

		if (jsonBean.getVIEWNO() != null)
		{
			if (faceType == 5)
			{
				resultMap.put("classViewNodeList", MGetClassViewModelData
						.getClassViewModelData(jsonBean.getVIEWNO(), null).getClassViewNodeList());
				return resultMap;
			}
		}

		return null;
	}

	@PostMapping("/getClassNodeModelData")
	public Map<String, Object> getClassNodeModelData(@RequestBody String queryParam)
	{
		USCObjectQueryJsonBean jsonBean = null;
		try
		{
			jsonBean = BeanFactoryConverter.getJsonBean(USCObjectQueryJsonBean.class, queryParam);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		ItemInfo info = jsonBean.getItemInfo();
		if (info == null)
			return null;
		String itemGridNo = jsonBean.ITEMGRIDNO;
		String itemPropertyNo = jsonBean.ITEMPROPERTYNO;
		int faceType = jsonBean.FACETYPE;
		String itemRelationPageNo = jsonBean.ITEMRELATIONPAGENO;

		String userName = jsonBean.getUSERNAME();
		UserInformation userInformation = UserInfoUtils.getUserInformation(userName);

		Map<String, Object> result = new HashMap<>();

		Map<String, Object> itemModel = new HashMap<>();
		result.put("flag", true);
		result.put("faceType", faceType);
		ModelUtils.getClientAllPageData(userInformation, itemModel, info, itemGridNo, itemPropertyNo,
				itemRelationPageNo, 2);
		result.put("itemModel", itemModel);

		Map<String, Object> classModel = new HashMap<>();
		ItemInfo classInfo = USCModelMate.getItemInfo(jsonBean.getClassNodeItemNo());
		String classNodeItemPropertyNo = jsonBean.getClassNodeItemPropertyNo();
		ModelUtils.getClientAllPageData(userInformation, classModel, classInfo, null, classNodeItemPropertyNo, null,
				faceType);

		List<ItemMenu> pageMenus = jdbcTemplate.query(
				"SELECT * FROM usc_model_itemmenu WHERE del=0 AND state='F' AND itemid='" + jsonBean.getPAGEID() + "'",
				new MenuRowMapper());
		Object itemMenus = classModel.get("itemMenus");
		if (itemMenus != null)
		{
			List<ItemMenu> itemMenus2 = (List<ItemMenu>) itemMenus;
			pageMenus.addAll(itemMenus2);
		}

		UserAuthority.authorityMenus(userInformation, pageMenus);

		classModel.put("itemMenus", pageMenus);

		result.put("classNodeModel", classModel);

		List classNodeData = DBUtil.getSQLResultByCondition(classInfo, "itemno='" + info.getItemNo() + "'");
		result.put("classNodeDataList", classNodeData);
		return result;
	}

	int m = 0;

	@PostMapping("/getModel/gridData")
	public Object GetModelGridData(@RequestBody String queryParam)
	{
		try
		{
			USCObjectJSONBean bean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
			ItemInfo info = bean.getItemInfo();
			if (info == null)
			{
				return null;
			}
			return info.getItemGrid(bean.getITEMGRIDNO());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/getModel/propertyData")
	public Object GetModePropertyData(@RequestBody String queryParam)
	{
		try
		{
			USCObjectJSONBean bean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
			ItemInfo info = bean.getItemInfo();

			return info.getItemPage(bean.getITEMPROPERTYNO());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/getModel/menuData")
	public Object GetModeMenuData(@RequestBody String queryParam)
	{
		try
		{
			USCObjectJSONBean bean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
			ItemInfo info = bean.getItemInfo();

			return info.getItemMenuList();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/getModel/relationPageData")
	public Object GetModeRelationPageData(@RequestBody String queryParam)
	{
		try
		{
			USCObjectJSONBean bean = BeanFactoryConverter.getJsonBean(USCObjectJSONBean.class, queryParam);
			ItemInfo info = bean.getItemInfo();

			return info.getItemRelationPage(bean.getITEMRELATIONPAGENO());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping("/getItemDataListLimit")
	public Object getDataListLimit(@RequestBody String queryParam) throws Exception
	{
		Map map = new HashMap<>();
		try
		{
			USCObjectQueryJsonBean jsonBean = BeanFactoryConverter.getJsonBean(USCObjectQueryJsonBean.class,
					queryParam);
			ItemInfo info = jsonBean.getItemInfo();
			Long s = System.currentTimeMillis();
			JSONObject jsonObject = JSONObject.parseObject(queryParam);
			Object condition = jsonObject.getString("condition");
			List list = DBUtil.getSQLResult(info.getItemNo(),
					"SELECT * FROM " + info.getTableName() + " WHERE del=0 "
							+ ((condition == null) ? "" : (" AND " + condition.toString()))
							+ DataBaseUtils.getLimit(jsonBean.getPAGE()),
					info.getItemFieldList());
			Long e = System.currentTimeMillis();
			double d = (e - s);
			map = StandardResultTranslate.getResult("Action_Query_1", list);
			if (list == null || list.size() == 0)
			{
				map.put("roll", false);
			} else
			{
				if (list.size() < DataBaseUtils.getDefaultLimitPageSize())
				{
					map.put("roll", false);
				} else
				{
					map.put("roll", true);
				}
			}
			return map;
		} catch (Exception e)
		{
			e.printStackTrace();
			return StandardResultTranslate.getResult(false, e.getMessage());
		}

	}

	@GetMapping("/getClassDataListLimit")
	private Object getClassDataListLimit(@RequestParam String queryParam) throws Exception
	{
		if (queryParam == null)
			return null;
		if (queryParam.trim().equals("{}"))
			return null;
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		int page = jsonObject.getInteger("page");
		String classItemNo = jsonObject.getString("classItemNo");
		String classNodeIds = jsonObject.getString("classNodeId");
		String itemNo = jsonObject.getString("itemNo");
		ItemInfo itemInfo = USCModelMate.getItemInfo(itemNo);
		ItemInfo classItemIfo = USCModelMate.getItemInfo(classItemNo);
		if (itemInfo == null || classItemIfo == null)
			return null;
		String itemTn = classItemIfo.getTableName();
		String paramCondition = " EXISTS(SELECT 1 FROM " + itemTn + " WHERE del=0 AND itemid=" + itemInfo.getTableName()
				+ ".id AND nodeid='" + classNodeIds + "')";

		return StandardResultTranslate.getQueryResult(true, "Action_Query",
				DBUtil.getSQLResultByCondition(itemInfo, paramCondition));
	}

	@GetMapping("/getItemDataList")
	private Object getDataList(@RequestParam String queryParam)
	{
		if (queryParam == null)
			return null;
		if (queryParam.trim().equals("{}"))
			return null;
		JSONObject jsonObject = JSONObject.parseObject(queryParam);
		Object itemNo = jsonObject.getString("itemNo");
		Object condition = jsonObject.getString("condition");
		ItemInfo info = USCModelMate.getItemInfo((String) itemNo);
		if (info == null)
			return null;
		String sql = "SELECT * FROM " + info.getTableName() + " WHERE del=0";
		List<Map<String, Object>> maps = jdbcTemplate
				.queryForList(sql + ((condition == null) ? "" : (" AND " + condition.toString())));

		return (maps == null || maps.isEmpty()) ? null : maps;
	}

}
