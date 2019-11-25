package com.usc.server.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.cache.redis.RedisUtil;
import com.usc.server.jdbc.DBUtil;
import com.usc.util.ObjectHelperUtils;

public class USCModelMate
{
	static RedisUtil redisUtil = new RedisUtil();
	private static Map<String, ItemInfo> itemInfoData = new HashMap<String, ItemInfo>();
	private static Map<String, ItemInfo> itemInfoDataByTable = new HashMap<String, ItemInfo>();
	private static Map<String, ModelRelationShip> relationShipData = new HashMap<String, ModelRelationShip>();
	private static Map<String, ModelClassView> classViewData = new HashMap<String, ModelClassView>();
	private static Map<String, ModelQueryView> queryViewData = new HashMap<String, ModelQueryView>();

	public static boolean containsObj(String no)
	{
		if (getItemInfo(no) != null)
			return true;
		if (getItemInfoByTable(no) != null)
			return true;
		return false;
	}

	private static ItemInfo getItemInfoByTableData(String itemNo)
	{
		return itemInfoDataByTable.get(itemNo);
	}

	private static void setItemInfoByTableData(String itemNo, ItemInfo info)
	{
		if (itemNo == null || info == null)
		{
			return;
		}
		itemInfoDataByTable.put(itemNo, info);
	}

	private static ItemInfo getItemInfoData(String itemNo)
	{
		return itemInfoData.get(itemNo);
	}

	private static void setItemInfoData(String itemNo, ItemInfo info)
	{
		if (itemNo == null || info == null)
		{
			return;
		}
		itemInfoData.put(itemNo, info);
	}

	private static ModelRelationShip getRelationShip(String no)
	{
		return relationShipData.get(no);
	}

	private static void setRelationShip(String shipNo, ModelRelationShip relationShip)
	{
		if (shipNo == null || relationShip == null)
		{
			return;
		}
		relationShipData.put(shipNo, relationShip);
	}

	private static ModelQueryView getModelQueryViewData(String no)
	{
		return queryViewData.get(no);
	}

	private static void setModelQueryViewData(String shipNo, ModelQueryView queryView)
	{
		if (shipNo == null || queryView == null)
		{
			return;
		}
		queryViewData.put(shipNo, queryView);
	}

	private static ModelClassView getModelClassViewData(String no)
	{
		return classViewData.get(no);
	}

	private static void setModelClassViewData(String viewNo, ModelClassView classView)
	{
		if (viewNo == null || classView == null)
		{
			return;
		}
		classViewData.put(viewNo, classView);
	}

	public static ItemInfo getItemInfo(String itemNo)
	{
		if (itemNo == null)
			return null;
		if (itemInfoData.containsKey(itemNo))
		{
			return getItemInfoData(itemNo);
		} else
		{
			Object info = redisUtil.hget("MODEL_ITEMDATA", itemNo);
			if (info != null)
			{
				setItemInfoData(itemNo, (ItemInfo) info);
				return (ItemInfo) info;
			}
		}
		return null;
	}

	public static ItemInfo getItemInfoByTable(String tableName)
	{
		if (tableName == null)
			return null;
		if (itemInfoDataByTable.containsKey(tableName))
		{
			return getItemInfoByTableData(tableName);
		} else
		{

			Object info = redisUtil.hget("MODEL_ITEMDATABYTABLE", tableName);
			if (info != null)
			{
				setItemInfoByTableData(tableName, (ItemInfo) info);
				return (ItemInfo) info;
			}
		}
		return null;
	}

	public static ModelRelationShip getRelationShipInfo(String relationShipNo)
	{
		if (relationShipNo == null)
			return null;
		if (relationShipData.containsKey(relationShipNo))
		{
			return getRelationShip(relationShipNo);
		} else
		{

			Object info = redisUtil.hget("MODEL_RELATIONSHIPDATA", relationShipNo);
			if (info != null)
			{
				setRelationShip(relationShipNo, (ModelRelationShip) info);
				return (ModelRelationShip) info;
			}
		}
		return null;
	}

	public static ModelQueryView getModelQueryViewInfo(String modelQueryViewNo)
	{
		if (modelQueryViewNo == null)
			return null;
		if (queryViewData.containsKey(modelQueryViewNo))
		{
			return getModelQueryViewData(modelQueryViewNo);
		}

		Object info = redisUtil.hget("MODEL_QUERYVIEWDATA", modelQueryViewNo);
		if (info != null)
		{
			setModelQueryViewData(modelQueryViewNo, (ModelQueryView) info);
			return (ModelQueryView) info;
		}
		return null;

	}

	public static ModelClassView getModelClassViewInfo(String classViewNo)
	{
		Object info = redisUtil.hget("MODEL_CLASSVIEWDATA", classViewNo);
		if (info != null)
		{
			setModelClassViewData(classViewNo, (ModelClassView) info);
			return (ModelClassView) info;

		}
		return null;
	}

	public static boolean VerifyItemUniqueness(ItemInfo itemInfo, Map map) throws Exception
	{
		if (itemInfo == null || map == null)
		{
			return false;
		}

		return ObjectHelperUtils.isEmpty(DBUtil.getSQLResultByOnlyFieldObject(itemInfo, map)) ? false : true;

	}

	public static List<ItemField> getOnlyItemFields(ItemInfo itemInfo)
	{
		if (itemInfo == null)
		{
			return null;
		}
		List<ItemField> onlyFields = new ArrayList<ItemField>();
		for (ItemField itemField : itemInfo.getItemFieldList())
		{
			if (itemField.getType() == 0)
			{
				continue;
			}
			if (itemField.getOnly() == 1)
			{
				onlyFields.add(itemField);
			}
		}
		return ObjectHelperUtils.isEmpty(onlyFields) ? null : onlyFields;

	}

	public static ItemField getItemField(String objType, String field)
	{
		ItemInfo info = getItemInfo(objType);
		if (info != null && info.containsField(field))
		{
			return info.getItemField(field);
		}
		return null;

	}

	public boolean containsField(Object itemInfo, String field)
	{
		ItemInfo info = null;
		if (itemInfo instanceof String)
		{
			info = getItemInfo((String) itemInfo);
		} else if (itemInfo instanceof ItemInfo)
		{
			info = (ItemInfo) itemInfo;
		}
		if (info == null)
		{
			return false;
		}
		return info.containsField(field);
	}

	public static void clearAllCache()
	{
		itemInfoData.clear();
		itemInfoDataByTable.clear();
		relationShipData.clear();
		classViewData.clear();
		queryViewData.clear();
	}

	public static void removeItemInfoDataCache(String key)
	{
		itemInfoData.remove(key);
	}

	public static void removeItemInfoDataByTableCache(String key)
	{
		itemInfoDataByTable.remove(key);
	}

	public static void removeRelationShipDataCache(String key)
	{
		relationShipData.remove(key);
	}

	public static void removClassViewDataCache(String key)
	{
		classViewData.remove(key);
	}

	public static void removQueryViewDataDataCache(String key)
	{
		queryViewData.remove(key);
	}

}
