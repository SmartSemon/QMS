package com.usc.server.md.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.usc.server.md.ItemField;
import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemGridField;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemPageField;
import com.usc.server.md.ItemRelationPage;
import com.usc.server.md.ItemRelationPageSign;
import com.usc.server.md.ModelClassViewTreeNode;

public class ModelInfoToMap
{

	public static Map<String, ItemField> getItemFieldType(List<ItemField> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemField> map = new HashMap<String, ItemField>();
		for (ItemField field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemPageField> getItemPageField(List<ItemPageField> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemPageField> map = new HashMap<String, ItemPageField>();
		for (ItemPageField field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemGrid> getItemGrid(List<ItemGrid> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemGrid> map = new HashMap<String, ItemGrid>();
		for (ItemGrid field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemGridField> getItemGridField(List<ItemGridField> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemGridField> map = new HashMap<String, ItemGridField>();
		for (ItemGridField field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemMenu> getItemMenu(List<ItemMenu> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemMenu> map = new HashMap<String, ItemMenu>();
		for (ItemMenu field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemPage> getItemPage(List<ItemPage> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemPage> map = new HashMap<String, ItemPage>();
		for (ItemPage field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemRelationPage> getItemRelationPage(List<ItemRelationPage> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemRelationPage> map = new HashMap<String, ItemRelationPage>();
		for (ItemRelationPage field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;

	}

	public static Map<String, ItemRelationPageSign> getItemRelationPageSign(List<ItemRelationPageSign> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ItemRelationPageSign> map = new HashMap<String, ItemRelationPageSign>();
		for (ItemRelationPageSign field : list)
		{
			map.put(field.getNo(), field);
		}
		return map;
	}

	public static Map<String, ModelClassViewTreeNode> getClassTreeNode(List<ModelClassViewTreeNode> list)
	{
		if (list==null)
		{
			return null;
		}
		Map<String, ModelClassViewTreeNode> map = new ConcurrentHashMap<String, ModelClassViewTreeNode>();
		for (ModelClassViewTreeNode field : list)
		{
			map.put(field.getId(), field);
		}
		return map;
	}
}
