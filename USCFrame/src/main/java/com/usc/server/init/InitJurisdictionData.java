package com.usc.server.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;
import com.usc.cache.redis.RedisUtil;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemRelationPageSign;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;
import com.usc.server.md.mapper.MenuRowMapper;
import com.usc.server.util.LoggerFactory;
import com.usc.test.mate.resource.ModelUtils;
import com.usc.util.ObjectHelperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitJurisdictionData
{
	private static List<Map<String, Object>> dataList = null;

	private static JdbcTemplate jdbcTemplate;

	public static void init(JdbcTemplate jdbc)
	{
		jdbcTemplate = jdbc;
		RedisUtil redisUtil = new RedisUtil();
		List<Map<String, Object>> list;
		try
		{
			list = getInit();
			if (!ObjectHelperUtils.isEmpty(list))
			{
				redisUtil.del("SYSTEM_PERMISSIONS");
				redisUtil.set("SYSTEM_PERMISSIONS", list);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			LoggerFactory.logError(InitJurisdictionData.class.getSimpleName() + "Init Failed");
		} finally
		{
			jdbcTemplate = null;
		}

	}

	public static List<Map<String, Object>> getInit() throws Exception
	{
		dataList = new Vector<Map<String, Object>>();
		if (navigation0())
		{
			return dataList;
		}

		return null;

	}

	private static boolean navigation0() throws Exception
	{

		List<Map<String, Object>> map1s = jdbcTemplate
				.queryForList("SELECT * FROM usc_model_navigation WHERE del=0 AND pid='0'");
		if (map1s != null)
		{

			for (Map<String, Object> map : map1s)
			{
//				id1 = (String) map.get("id");
				getNavigation1(map);
			}
		}
		return !ObjectHelperUtils.isEmpty(dataList);
	}

	private static int n = 0;

	private static void getNavigation1(Map<String, Object> map)
	{
		n = 1;
		String id = (String) map.get("id");
		String pid = (String) map.get("pid");
		L1ID = pid + "_" + id;

		setResultData(L1ID, pid, map.get("no"), map.get("name"), "usc_model_navigation", "home");

		List<Map<String, Object>> map1s = jdbcTemplate
				.queryForList("SELECT * FROM usc_model_navigation WHERE del=0 AND pid='" + id + "'");
		if (!ObjectHelperUtils.isEmpty(map1s))
		{
			for (Map<String, Object> map2 : map1s)
			{
				map2.put("pid", L1ID);
				getCNavigation2(map2);
			}
		} else
		{
			int facetype = map.get("facetype") != null ? (Integer) map.get("facetype") : -1;
			String pageParams = String.valueOf(map.get("params")).replace("null", "");
			if (!pageParams.equals(""))
			{
				getModelData(facetype, pageParams, L1ID);
			}
		}
	}

	private static boolean b = false;

	private static void getCNavigation2(Map<String, Object> map)
	{
		n = 2;
		String id = (String) map.get("id");
		String pid = (String) map.get("pid");
		L2ID = pid + "_" + id;
		setResultData(L2ID, pid, map.get("no"), map.get("name"), "usc_model_navigation", "home");

		List<Map<String, Object>> map1s = jdbcTemplate
				.queryForList("SELECT * FROM usc_model_navigation WHERE del=0 AND pid='" + id + "'");
		if (!ObjectHelperUtils.isEmpty(map1s))
		{
			for (Map<String, Object> map2 : map1s)
			{
				map2.put("pid", L2ID);
				getCNavigation3(map2);
			}
		} else
		{
			int facetype = map.get("facetype") != null ? (Integer) map.get("facetype") : -1;
			String pageParams = String.valueOf(map.get("params")).replace("null", "");
			if (!pageParams.equals(""))
			{
				getModelData(facetype, pageParams, L2ID);
			}
		}
	}

	private static void getCNavigation3(Map<String, Object> map)
	{
		n = 3;
		String id = (String) map.get("id");
		String pid = (String) map.get("pid");
		L3ID = pid + "_" + id;
		setResultData(L3ID, pid, map.get("no"), map.get("name"), "usc_model_navigation", "home");

		List<Map<String, Object>> map1s = jdbcTemplate
				.queryForList("SELECT * FROM usc_model_navigation WHERE del=0 AND pid='" + id + "'");
		if (!ObjectHelperUtils.isEmpty(map1s))
		{
			for (Map<String, Object> map2 : map1s)
			{
				map2.put("pid", L3ID);
				getCNavigation4(map2);
			}
		} else
		{
			int facetype = map.get("facetype") != null ? (Integer) map.get("facetype") : -1;
			String pageParams = String.valueOf(map.get("params")).replace("null", "");
			if (!pageParams.equals(""))
			{
				getModelData(facetype, pageParams, L3ID);
			}
		}
	}

	private static void getCNavigation4(Map<String, Object> map)
	{
		n = 4;
		String id = (String) map.get("id");
		String pid = (String) map.get("pid");
		L4ID = pid + "_" + id;
		setResultData(L4ID, pid, map.get("no"), map.get("name"), "usc_model_navigation", "home");

		int facetype = map.get("facetype") != null ? (Integer) map.get("facetype") : -1;
		String pageParams = String.valueOf(map.get("params")).replace("null", "");
		if (!pageParams.equals(""))
		{
			getModelData(facetype, pageParams, L4ID);
		}
	}

	private static String L1ID = null;
	private static String L2ID = null;
	private static String L3ID = null;
	private static String L4ID = null;
	private static String tab = "usc_model_navigation";

	private static void setResultData(String cid, String pid, Object no, Object name, String tableName, String type)
	{
		Map<String, Object> nData = new ConcurrentHashMap<String, Object>();
		if (!tableName.equals(tab))
		{
			String MID = null;
			switch (n)
			{
			case 1:
				MID = L1ID;
				break;
			case 2:
				MID = L2ID;
				break;
			case 3:
				MID = L3ID;
				break;
			case 4:
				MID = L4ID;
				break;

			default:
				break;
			}
			cid = MID + "_" + cid;
			pid = MID + "_" + pid;

		}

		nData.put("id", cid);
		nData.put("pid", pid);
		nData.put("no", no);
		nData.put("name", name);
		nData.put("ctn", tableName);
		nData.put("type", type);
		if (dataList == null)
		{
			System.out.println();
		}

		dataList.add(nData);
	}

	private static void setResultData2(String cid, String pid, Object no, Object name, String tableName, String type)
	{
		Map<String, Object> nData = new ConcurrentHashMap<String, Object>();

		nData.put("id", cid);
		nData.put("pid", pid);
		nData.put("no", no);
		nData.put("name", name);
		nData.put("ctn", tableName);
		nData.put("type", type);

		dataList.add(nData);
	}

	private static Map<String, Object> modelData = null;

	private static void getModelData(int faceType, String pageParams, String nid)
	{
		if (faceType == -1 || faceType == 0)
		{
			return;
		}
		b = false;
		JSONObject object = JSONObject.parseObject(pageParams);
		String itemNo = object.getString("itemNo");
		String itemGridNo = object.getString("itemGridNo");
		String itemPropertyNo = object.getString("itemPropertyNo");
		String itemRelationPageNo = object.getString("itemRelationPageNo");

		ItemInfo itemInfo = USCModelMate.getItemInfo(itemNo);
		if (itemInfo == null)
		{
			log.error(">>>>>> item Not found:" + itemNo);
			return;
		}
		setPageMenus(nid);

		switch (faceType)
		{
		case 1:

			setItemData1(itemInfo, nid);

			break;
		case 2:
			modelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(modelData, itemInfo, itemGridNo, itemPropertyNo, itemRelationPageNo, faceType);
			setItemData1(itemInfo, nid);
			Object itemRelationPage = modelData.get("itemRelationPage");
			if (itemRelationPage != null)
			{
				List<ItemRelationPageSign> itemRelationPageSigns = (List<ItemRelationPageSign>) itemRelationPage;
				setItemRelationPageData2(itemInfo, itemRelationPageSigns, faceType);
			}
			break;
		case 21:
			modelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(modelData, itemInfo, itemGridNo, itemPropertyNo, itemRelationPageNo, faceType);
			setItemData1(itemInfo, nid);
			Object itemRelationPage21 = modelData.get("itemRelationPage");
			if (itemRelationPage21 != null)
			{
				List<ItemRelationPageSign> itemRelationPageSigns = (List<ItemRelationPageSign>) itemRelationPage21;
				setItemRelationPageData2(itemInfo, itemRelationPageSigns, faceType);
			}
			break;
		case 3:
			modelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(modelData, itemInfo, itemGridNo, itemPropertyNo, itemRelationPageNo, faceType);
			setItemData1(itemInfo, nid);
			Object itemRelationPage3 = modelData.get("itemRelationPage");
			if (itemRelationPage3 != null)
			{
				List<ItemRelationPageSign> itemRelationPageSigns = (List<ItemRelationPageSign>) itemRelationPage3;
				setItemRelationPageData2(itemInfo, itemRelationPageSigns, faceType);
			}
			break;
		case 4:
			modelData = new HashMap<String, Object>();
			ItemInfo classInfo = USCModelMate.getItemInfo(object.getString("classNodeItemNo"));
			String classNodeItemGridNo = null;
			String classNodeItemPropertyNo = object.getString("classNodeItemPropertyNo");
			Map<String, Object> classModelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(classModelData, classInfo, classNodeItemGridNo, classNodeItemPropertyNo, null,
					faceType);
			setItemData1(classInfo, nid);

			modelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(modelData, itemInfo, itemGridNo, itemPropertyNo, itemRelationPageNo, faceType);
			setItemData1(itemInfo, nid + "_" + classInfo.getId());
			Object itemRelationPage4 = modelData.get("itemRelationPage");
			if (itemRelationPage4 != null)
			{
				List<ItemRelationPageSign> itemRelationPageSigns = (List<ItemRelationPageSign>) itemRelationPage4;
				setItemRelationPageData1(itemInfo, itemRelationPageSigns, faceType, nid + "_" + classInfo.getId());
			}

			break;
		case 5:
			modelData = new HashMap<String, Object>();
			ModelUtils.getAllPageData(modelData, itemInfo, itemGridNo, itemPropertyNo, itemRelationPageNo, faceType);
			setItemData1(itemInfo, nid);
			Object itemRelationPage5 = modelData.get("itemRelationPage");
			if (itemRelationPage5 != null)
			{
				List<ItemRelationPageSign> itemRelationPageSigns = (List<ItemRelationPageSign>) itemRelationPage5;
				setItemRelationPageData2(itemInfo, itemRelationPageSigns, faceType);
			}
			break;

		default:

			break;
		}
	}

	private static void setItemRelationPageData1(ItemInfo itemInfo, List<ItemRelationPageSign> itemRelationPageSigns,
			int faceType, String nid)
	{
		if (itemInfo == null || itemRelationPageSigns == null)
		{
			log.error(">>>>>> itemInfo is null Or itemRelationPageSigns is null");
			return;
		}
		for (ItemRelationPageSign itemRelationPageSign : itemRelationPageSigns)
		{
			String rType = itemRelationPageSign.getRType();
			if (rType.equals("relationpage"))
			{
				ModelRelationShip relationShip = itemRelationPageSign.getModelRelationShip();
				if (relationShip == null)
				{
					log.error(">>>>>> " + itemInfo.toString() + " relationShip->" + itemRelationPageSign.toString()
							+ " is null");
					continue;
				}
				setModelRelationShipData1(itemInfo, relationShip, nid);
				List<ItemMenu> relMenus = relationShip.getRelationMenuList();
				if (relMenus != null)
				{
					setRelationShipMenu1(relationShip, relMenus, nid);
				}
				ItemInfo Binfo = USCModelMate.getItemInfo(itemRelationPageSign.getItemNo());
				setItemData1(Binfo, nid + "_" + relationShip.getId());
			}
		}
	}

	private static void setRelationShipMenu1(ModelRelationShip relationShip, List<ItemMenu> relMenus, String nid)
	{
		if (relMenus == null || relationShip == null)
		{
			log.error(">>>>>> relMenus is null Or relationShip is null");
			return;
		}
		for (ItemMenu itemMenu : relMenus)
		{
			setResultData2(nid + "_" + itemMenu.getId(), nid + "_" + relationShip.getId(), itemMenu.getNo(),
					itemMenu.getName(), "usc_model_itemmenu", "edit");
		}
	}

	private static void setModelRelationShipData1(ItemInfo itemInfo, ModelRelationShip relationShip, String nid)
	{
		if (itemInfo == null || relationShip == null)
		{
			log.error(">>>>>> itemInfo is null Or relationShip is null");
			return;
		}
		setResultData2(nid + "_" + relationShip.getId(), nid + "_" + itemInfo.getId(), relationShip.getNo(),
				relationShip.getName(), "usc_model_relationship", "api");
	}

	private static void setItemRelationPageData2(ItemInfo itemInfo, List<ItemRelationPageSign> itemRelationPageSigns,
			int faceType)
	{
		if (itemInfo == null || itemRelationPageSigns == null)
		{
			log.error(">>>>>> itemInfo is null Or itemRelationPageSigns is null");
			return;
		}
		for (ItemRelationPageSign itemRelationPageSign : itemRelationPageSigns)
		{
			String rType = itemRelationPageSign.getRType();
			if (rType.equals("relationpage"))
			{
				ModelRelationShip relationShip = itemRelationPageSign.getModelRelationShip();
				if (relationShip == null)
				{
					log.error(">>>>>> " + itemInfo.toString() + " relationPageSign->" + itemRelationPageSign.toString()
							+ " is null");
					continue;
				}
				setModelRelationShipData(itemInfo, relationShip);
				List<ItemMenu> relMenus = relationShip.getRelationMenuList();
				if (relMenus != null)
				{
					setRelationShipMenu(relationShip, relMenus);
				}
				ItemInfo Binfo = USCModelMate.getItemInfo(itemRelationPageSign.getItemNo());
				setItemData(Binfo, relationShip.getId());
				if (String.valueOf(faceType).startsWith("3"))
				{
					List<ItemRelationPageSign> relationPageSigns = relationShip.getItemRelationPage();
					if (relationPageSigns != null)
					{
						setItemRelationPageData2(Binfo, relationPageSigns, faceType);
					}

				}
			}
		}
	}

	private static void setRelationShipMenu(ModelRelationShip relationShip, List<ItemMenu> relMenus)
	{
		if (relMenus == null || relationShip == null)
		{
			log.error(">>>>>> relMenus is null Or relationShip is null");
			return;
		}
		for (ItemMenu itemMenu : relMenus)
		{
			setResultData(itemMenu.getId(), relationShip.getId(), itemMenu.getNo(), itemMenu.getName(),
					"usc_model_itemmenu", "edit");
		}

	}

	private static void setModelRelationShipData(ItemInfo itemInfo, ModelRelationShip relationShip)
	{
		if (itemInfo == null || relationShip == null)
		{
			log.error(">>>>>> itemInfo is null Or relationShip is null");
			return;
		}
		setResultData(relationShip.getId(), itemInfo.getId(), relationShip.getNo(), relationShip.getName(),
				"usc_model_relationship", "api");
	}

	private static void setItemData(ItemInfo itemInfo, String pid)
	{
		String itemID = itemInfo.getId();
		setResultData(itemID, pid, itemInfo.getItemNo(), itemInfo.getName(), "usc_model_item", "appstore");
		List<ItemMenu> itemMenus = itemInfo.getItemMenuList();
		if (itemMenus != null)
		{
			for (ItemMenu itemMenu : itemMenus)
			{
				setResultData(itemMenu.getId(), itemID, itemMenu.getNo(), itemMenu.getName(), "usc_model_itemmenu",
						"edit");
			}
		}

	}

	private static void setItemData1(ItemInfo itemInfo, String pid)
	{
		String itemID = pid + "_" + itemInfo.getId();
		setResultData2(itemID, pid, itemInfo.getItemNo(), itemInfo.getName(), "usc_model_item", "appstore");
		List<ItemMenu> itemMenus = itemInfo.getItemMenuList();
		if (itemMenus != null)
		{
			for (ItemMenu itemMenu : itemMenus)
			{
				setResultData2(pid + "_" + itemMenu.getId(), itemID, itemMenu.getNo(), itemMenu.getName(),
						"usc_model_itemmenu", "edit");
			}
		}

	}

	private static void setPageMenus(String pid)
	{
		List<ItemMenu> pageMenus = jdbcTemplate
				.query("SELECT * FROM usc_model_itemmenu WHERE del=0 AND state='F' AND itemid='"
						+ pid.substring(pid.lastIndexOf("_") + 1, pid.length()) + "'", new MenuRowMapper());
		if (!ObjectHelperUtils.isEmpty(pageMenus))
		{
			for (ItemMenu itemMenu : pageMenus)
			{
				setResultData2(pid + "_" + itemMenu.getId(), pid, itemMenu.getNo(), itemMenu.getName(),
						"usc_model_itemmenu", "edit");
			}
		}
	}
}
