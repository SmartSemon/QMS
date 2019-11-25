package com.usc.server.md;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

@Data
public class ModelClassView implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 5612311598280685960L;

	protected String id;
	protected String no;
	protected String name;
	protected String itemNo;
	protected String wcondition;

	protected List<ModelClassViewTreeNode> classViewNodeList;
	protected Map<String, ModelClassViewTreeNode> classViewNodeMap = new ConcurrentHashMap<String, ModelClassViewTreeNode>();
	protected ModelClassViewTreeNode rootNode;
	protected List<ItemMenu> itemMenus;
	protected ItemInfo itemInfo;
	protected ItemGrid itemGrid;
	protected ItemPage itemPropertyPage;

	public ModelClassView()
	{
	}
}
