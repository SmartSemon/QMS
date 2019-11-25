package com.usc.server.util;

import java.util.concurrent.ConcurrentHashMap;

import com.usc.server.md.ItemInfo;
import com.usc.server.md.ModelClassView;
import com.usc.server.md.ModelQueryView;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;

public class MateFactory
{
	private static ConcurrentHashMap<String, ItemInfo> itemInfoMap = new ConcurrentHashMap<String, ItemInfo>();
	private static ConcurrentHashMap<String, ModelRelationShip> shipMap = new ConcurrentHashMap<String, ModelRelationShip>();
	private static ConcurrentHashMap<String, ModelQueryView> queryView = new ConcurrentHashMap<String, ModelQueryView>();
	private static ConcurrentHashMap<String, ModelClassView> classView = new ConcurrentHashMap<String, ModelClassView>();

	public static ItemInfo getItemInfo(String itemNo)
	{
		if (itemNo == null)
		{
			return null;
		}
		ItemInfo info = itemInfoMap.get(itemNo);
		if (info != null)
		{
			return info;
		} else
		{
			info = USCModelMate.getItemInfo(itemNo);
			if (info != null)
			{
				putItemInfo(info);
			}
		}
		return info;
	}

	public static void putItemInfo(ItemInfo info)
	{
		itemInfoMap.putIfAbsent(info.getItemNo(), info);
	}

	public static ModelRelationShip getRelationShip(String shipNo)
	{
		ModelRelationShip ship = shipMap.get(shipNo);
		if (ship != null)
		{
			return ship;
		} else
		{
			ship = USCModelMate.getRelationShipInfo(shipNo);
			if (ship != null)
			{

			}
		}
		return ship;
	}

	public static void putRelationShip(ModelRelationShip ship)
	{
		shipMap.putIfAbsent(ship.getNo(), ship);
	}

	public static ModelQueryView getQueryView(String viewNo)
	{
		ModelQueryView ship = queryView.get(viewNo);
		if (ship != null)
		{
			return ship;
		} else
		{
			ship = USCModelMate.getModelQueryViewInfo(viewNo);
			if (ship != null)
			{

			}
		}
		return ship;
	}

	public static void putQueryView(ModelQueryView view)
	{
		queryView.putIfAbsent(view.getNo(), view);
	}

	public static void putClassView(ModelClassView view)
	{
		classView.putIfAbsent(view.getNo(), view);
	}

	public static ModelClassView getClassView(String viewNo)
	{
		ModelClassView ship = classView.get(viewNo);
		if (ship != null)
		{
			return ship;
		} else
		{
			ship = USCModelMate.getModelClassViewInfo(viewNo);
			if (ship != null)
			{

			}
		}
		return ship;
	}

	public static void clear()
	{
		itemInfoMap.clear();
		shipMap.clear();
		queryView.clear();
	}
}