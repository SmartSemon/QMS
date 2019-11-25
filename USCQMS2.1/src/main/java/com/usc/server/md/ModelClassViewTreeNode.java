package com.usc.server.md;

import java.io.Serializable;

import lombok.Data;
@Data
public class ModelClassViewTreeNode implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 8782490787688143442L;

	protected String id;
	protected String no;
	protected String name;
	protected String icon;
	protected String nodecondition;
	protected String datacondition;
	protected String pid;
	protected String itemid;

	protected String treenodeid;
	protected String treenodepid;
	protected String treenodedata;

	public ModelClassViewTreeNode clone()
	{
		ModelClassViewTreeNode classViewTreeNode = new ModelClassViewTreeNode();
		classViewTreeNode.setId(id);
		classViewTreeNode.setNo(no);
		classViewTreeNode.setName(name);
		classViewTreeNode.setIcon(icon);
		classViewTreeNode.setNodecondition(nodecondition);
		classViewTreeNode.setDatacondition(datacondition);
		classViewTreeNode.setPid(pid);
		classViewTreeNode.setItemid(itemid);
		return classViewTreeNode;
	}

}
