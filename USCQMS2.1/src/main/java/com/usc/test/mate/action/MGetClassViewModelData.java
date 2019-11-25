package com.usc.test.mate.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.ModelClassView;
import com.usc.server.md.ModelClassViewTreeNode;
import com.usc.server.md.USCModelMate;
import com.usc.util.ObjectHelperUtils;

public class MGetClassViewModelData
{
	private static final String split = "#";
	private static final String PARENT = "PARENT.";
	private static final String sign = "%";
	private static final String CLASSVIEWTREENODEVALUE = "CLASSVIEWTREENODEVALUE";

	private static List<ModelClassViewTreeNode> treeNodeList = null;
	private static Map<String, ModelClassViewTreeNode> treeNodeMap = null;

	private static String ORDER = "order";
	private static String BY = "by";

	public static ModelClassView getClassViewModelData(String viewNo, USCObject object)
	{

		ModelClassView classView = USCModelMate.getModelClassViewInfo(viewNo);
		if (classView != null)
		{
			List<ModelClassViewTreeNode> treeNodes = new ArrayList<ModelClassViewTreeNode>();
			ModelClassViewTreeNode rootNode = classView.getRootNode();
			rootNode.setTreenodedata(rootNode.getName());
			rootNode.setTreenodeid(rootNode.getName());
			rootNode.setTreenodepid("0");
			treeNodes.add(rootNode);
			treeNodeList = classView.getClassViewNodeList();
			treeNodeMap = classView.getClassViewNodeMap();

			getChildNode(rootNode, treeNodeList, treeNodeMap, treeNodes, object);

			classView.setClassViewNodeList(treeNodes);

		}
		return classView;
	}

	private static void getChildNode(ModelClassViewTreeNode pNode, List<ModelClassViewTreeNode> treeNodeList,
			Map<String, ModelClassViewTreeNode> treeNodeMap, List<ModelClassViewTreeNode> treeNodes, USCObject object)
	{
		String pid = pNode.getId();
		treeNodeList.forEach(node -> {
			if (pid.equals(node.getPid()))
			{
				List<ModelClassViewTreeNode> cNodeList = getNodeData(pNode, node, treeNodes, object);
				if (!ObjectHelperUtils.isEmpty(cNodeList))
				{
					cNodeList.forEach(cpNode -> {
						getChildNode(cpNode, treeNodeList, treeNodeMap, treeNodes, object);
					});

				}
			}
		});
	}

	private static List<ModelClassViewTreeNode> getNodeData(ModelClassViewTreeNode pNode, ModelClassViewTreeNode node,
			List<ModelClassViewTreeNode> treeNodes, USCObject object)
	{
		if (node == null || node.getDatacondition() == null)
		{
			return null;
		}

		List<ModelClassViewTreeNode> cTreeNodes = new ArrayList<ModelClassViewTreeNode>();
		String pNodeID = pNode.getTreenodeid();
		String pDataCondition = pNode.getDatacondition().replace(CLASSVIEWTREENODEVALUE,
				PARENT + CLASSVIEWTREENODEVALUE);
		String cDataCondition = node.getDatacondition();
		String treeNodeDataCondition = getOrderByCondition(pDataCondition, cDataCondition);
		String cNodeDataSql = node.getNodecondition();
		if (ObjectHelperUtils.isEmpty(cNodeDataSql))
		{
			ModelClassViewTreeNode treeNodeData = node.clone();
			treeNodeData.setTreenodepid(pNodeID);
			treeNodeData.setDatacondition(treeNodeDataCondition);
			String treeNodeId = pNodeID + split + node.getName();

			treeNodeData.setTreenodeid(treeNodeId);
			treeNodeData.setTreenodedata(node.getName());
			treeNodes.add(treeNodeData);
			cTreeNodes.add(treeNodeData);
			return cTreeNodes;
		}
		if (cNodeDataSql.contains(CLASSVIEWTREENODEVALUE + sign))
		{
			cNodeDataSql = replaceCNodeDataSql(cNodeDataSql, pNodeID);

		}
		if (object != null)
		{
			cNodeDataSql = USCObjExpHelper.parseObjValueInExpression(object, cNodeDataSql);
		}
		List<Map<String, Object>> nodeMaps = DBUtil.queryForList(cNodeDataSql);
		if (nodeMaps == null)
		{
			return null;
		}

		nodeMaps.forEach(nodeData -> {
			nodeData.forEach((k, v) -> {
				String ndata = String.valueOf(v).replace("null", "");
				if (!ndata.equals(""))
				{
					ModelClassViewTreeNode treeNodeData = node.clone();
					treeNodeData.setTreenodepid(pNodeID);
					treeNodeData.setDatacondition(treeNodeDataCondition);
					String treeNodeId = pNodeID + split + v;
					treeNodeData.setTreenodeid(treeNodeId);
					treeNodeData.setTreenodedata(ndata);
					treeNodes.add(treeNodeData);
					cTreeNodes.add(treeNodeData);
				}

			});
		});
		return cTreeNodes;

	}

	private static String replaceCNodeDataSql(String cNodeDataSql, String pNodeID)
	{

		int i = 1;
		while (cNodeDataSql.contains(CLASSVIEWTREENODEVALUE + sign))
		{
			if (i == 20)
			{
				return cNodeDataSql;
			}
			int index = cNodeDataSql.indexOf(CLASSVIEWTREENODEVALUE + sign);
			int n = index + (CLASSVIEWTREENODEVALUE + sign).length();

			while (index > 6)
			{
				String b = cNodeDataSql.substring(index - 2, index - 1);
				if (b.equals(sign))
				{
					String nodeValue = cNodeDataSql.substring(index - 2, n);
					String str = replaceParentNodeData(nodeValue, pNodeID);
					cNodeDataSql = cNodeDataSql.replace(nodeValue, str);
					break;
				}
				index--;

			}
			i++;
		}
		return cNodeDataSql;
	}

	private static String replaceParentNodeData(String nodeValue, String pNodeID)
	{
		int n = 0;
		while (nodeValue.contains(PARENT))
		{
			n++;
			nodeValue = nodeValue.substring(nodeValue.indexOf(PARENT) + 7);
		}
		return replaceParentNodeData(n, pNodeID);
	}

	private static String replaceParentNodeData(int n, String pNodeID)
	{
		String[] pNodeIDs = pNodeID.split(split);
		int len = pNodeIDs.length;
		if (n > len || n < 1)
		{
			return "null";
		}
		return pNodeIDs[len - n];
	}

	private static String getOrderByFields(String pOrderby, ModelClassViewTreeNode rootNode)
	{
		String dcon = rootNode.getDatacondition();
		String dconlow = rootNode.getDatacondition().toLowerCase();
		if (dconlow.contains(BY))
		{
			String orderBy = dcon.substring(dconlow.indexOf(BY) + 2, dcon.length()).trim();
			if (!ObjectHelperUtils.isEmpty(pOrderby))
			{
				orderBy += "," + pOrderby;
			}
			return orderBy;
		}
		return "";
	}

	private static String getOrderByCondition(String pTreeNodeDataCondition, String cTreeNodeDataCondition)
	{
		String pSql = pTreeNodeDataCondition.toLowerCase();
		if (!pSql.contains(ORDER))
		{
			return pTreeNodeDataCondition + " AND " + cTreeNodeDataCondition;
		}

		int podIndex = pSql.indexOf(BY) + 2;
		String pOrderfs = pTreeNodeDataCondition.substring(podIndex, pTreeNodeDataCondition.length()).trim();
		String order = " ORDER BY ";
		if (!cTreeNodeDataCondition.toLowerCase().contains(ORDER))
		{
			return pTreeNodeDataCondition.substring(0, pSql.indexOf(ORDER)) + " AND " + cTreeNodeDataCondition + order
					+ pOrderfs;
		}
		String sql = cTreeNodeDataCondition.toLowerCase();
		int odIndex = sql.indexOf(BY) + 2;
		String orderfs = cTreeNodeDataCondition.substring(odIndex, cTreeNodeDataCondition.length()).trim();
		return pTreeNodeDataCondition.substring(0, pSql.indexOf(ORDER)) + " AND "
				+ (cTreeNodeDataCondition.substring(0, sql.indexOf(ORDER))) + order + orderfs + "," + pOrderfs;
	}

}
