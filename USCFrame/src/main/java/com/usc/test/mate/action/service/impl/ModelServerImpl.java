package com.usc.test.mate.action.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.usc.cache.redis.RedisUtil;
import com.usc.test.mate.action.service.ModelServer;
import com.usc.util.ObjectHelperUtils;

@Service(value = "modelServer")
public class ModelServerImpl implements ModelServer
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	RedisUtil redis = new RedisUtil();

	@Override
	public Object openModel(String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);
		String user = jsonObject.getString("userName");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);
		map.put("info", "开启成功");
		if (redis.hasKey("OPENMODEL"))
		{

			String muser = (String) redis.get("OPENMODEL");
			if (!user.equals(muser))
			{
				map.put("flag", false);
				map.put("info", "用户：" + muser + " 正在建模，开启失败");
			}
		} else
		{
			redis.set("OPENMODEL", user);
		}
		return map;
	}

	@Override
	public Object closeModel(String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);
		String user = jsonObject.getString("userName");
		Object force = jsonObject.get("force");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);
		map.put("info", "关闭成功");
		if (force == null)
		{
			List<Map<String, Object>> list = jdbcTemplate
					.queryForList("SELECT id FROM usc_model_item WHERE del=0 AND state='C' "
							+ "UNION ALL SELECT id FROM usc_model_item WHERE del=0 AND state='U' "
							+ "UNION ALL SELECT id FROM usc_model_item WHERE del=0 AND state='F' AND effective=-2 "
							+ "UNION ALL SELECT id FROM usc_model_item WHERE del=0 AND effective=1  AND state='HS'"
							+ "UNION ALL SELECT id FROM usc_model_queryview WHERE del=0 AND state='C' "
							+ "UNION ALL SELECT id FROM usc_model_queryview WHERE del=0 AND state='U' "
							+ "UNION ALL SELECT id FROM usc_model_queryview WHERE del=0 AND state='F' AND effective=-2 "
							+ "UNION ALL SELECT id FROM usc_model_queryview WHERE del=0 AND effective=1  AND state='HS'"
							+ "UNION ALL SELECT id FROM usc_model_classview WHERE del=0 AND state='C' "
							+ "UNION ALL SELECT id FROM usc_model_classview WHERE del=0 AND state='U' "
							+ "UNION ALL SELECT id FROM usc_model_classview WHERE del=0 AND state='F' AND effective=-2 "
							+ "UNION ALL SELECT id FROM usc_model_classview WHERE del=0 AND effective=1 AND state='HS'"
							+ "UNION ALL SELECT id FROM usc_model_relationship WHERE del=0 AND state='C' "
							+ "UNION ALL SELECT id FROM usc_model_relationship WHERE del=0 AND state='U' "
							+ "UNION ALL SELECT id FROM usc_model_relationship WHERE del=0 AND state='F' AND effective=-2 "
							+ "UNION ALL SELECT id FROM usc_model_relationship WHERE del=0 AND effective=1  AND state='HS'"
							+ "UNION ALL SELECT id FROM usc_model_navigation WHERE del=0 AND state<>'F' ");
			boolean b = ObjectHelperUtils.isEmpty(list);
			if (!b)
			{
				map.put("force", true);
				map.put("info", "是否保存新的建模数据？");
			} else
			{
				redis.del("OPENMODEL");
			}
			return map;
		} else
		{
			if (redis.hasKey("OPENMODEL"))
			{
				String muser = (String) redis.get("OPENMODEL");
				if (user.equals(muser))
				{
					if (!(boolean) force)
					{
						String[] sqls = new String[]
						{ "DELETE FROM usc_model_classview WHERE state IN('C','U')",
								"DELETE FROM usc_model_classview_node WHERE state IN('C','U')",
								"DELETE FROM usc_model_queryview WHERE state IN('C','U')",
								"DELETE FROM usc_model_field WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_grid WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_grid_field WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_item WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_itemmenu WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_menu WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_navigation WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_property WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_property_field WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_relationpage WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_relationpage_sign WHERE del=0 AND state IN('C','U')",
								"DELETE FROM usc_model_relationship WHERE del=0 AND state IN('C','U')",

								"UPDATE usc_model_item SET effective=1 WHERE del=0 AND state='F' AND effective=0",
								"UPDATE usc_model_item SET state='F',effective=1 WHERE del=0 AND effective=1 AND state='HS'",
								"UPDATE usc_model_item SET state='HS',effective=0 WHERE del=0 AND effective=-2 AND state='F'",

								"UPDATE usc_model_classview SET effective=1 WHERE del=0 AND state='F' AND effective=0",
								"UPDATE usc_model_classview SET state='F',effective=1 WHERE del=0 AND effective=1 AND state='HS'",
								"UPDATE usc_model_classview SET state='HS',effective=0 WHERE del=0 AND effective=-2 AND state='F'",

								"UPDATE usc_model_queryview SET effective=1 WHERE del=0 AND state='F' AND effective=0",
								"UPDATE usc_model_queryview SET state='F',effective=1 WHERE del=0 AND effective=1 AND state='HS'",
								"UPDATE usc_model_queryview SET state='HS',effective=0 WHERE del=0 AND effective=-2 AND state='F'",

								"UPDATE usc_model_relationship SET effective=1 WHERE del=0 AND state='F' AND effective=0",
								"UPDATE usc_model_relationship SET state='F',effective=1 WHERE del=0 AND effective=1 AND state='HS'",
								"UPDATE usc_model_relationship SET state='HS',effective=0 WHERE del=0 AND effective=-2 AND state='F'" };
						jdbcTemplate.batchUpdate(sqls);
						redis.del("OPENMODEL");
					}
				} else
				{
					map.put("flag", false);
					map.put("info", "用户：" + muser + "正在建模，关闭失败");
				}
			} else
			{
				map.put("flag", true);
				map.put("info", "无人建模");
			}
		}

		return map;
	}

	private String tableName;

	@Override
	public Object upgradeModel(String param)
	{
		JSONObject jsonObject = JSONObject.parseObject(param);
		this.cuser = jsonObject.getString("userName");
		Map<String, Object> map = new HashMap<String, Object>();

		if (redis.hasKey("OPENMODEL"))
		{

			String muser = (String) redis.get("OPENMODEL");
			if (!cuser.equals(muser))
			{
				map.put("flag", false);
				map.put("info", "用户：" + muser + " 正在建模，您无权操作建模数据");
				return map;
			}
		} else
		{
			map.put("flag", false);
			map.put("info", "请先开启建模工作权限");
			return map;
		}
		tableName = jsonObject.getString("tableName");
		String dataString = jsonObject.getString("data");
		if (dataString == null)
		{
			map.put("flag", false);
			map.put("info", "请选择需要升版修改的建模数据");
			return map;
		}
		JSONObject obj = JSONObject.parseObject(dataString);

		if (!"F".equals(obj.getString("state")))
		{
			map.put("flag", false);
			map.put("info", "请选择已同步的最新版建模数据升版！");
			return map;
		}
		switch (tableName)
		{
		case "usc_model_item":
			cloneItem(obj);
			break;
		case "usc_model_queryview":
			cloneQueryview(obj);
			break;
		case "usc_model_classview":
			cloneClassview(obj);
			break;
		case "usc_model_relationship":
			cloneRelationship(obj);
			break;

		default:
			break;
		}
		map.put("flag", true);
		map.put("info", "升版完成！");
		return map;
	}

	private String cuser;

	private void cloneItem(JSONObject obj)
	{
		List<String> sqls = new Vector<String>();
		Integer ver = obj.getInteger("ver") + 1;
		String oldItemID = obj.getString("id");
		String newItemID = getUUID();
		String itemSql = "INSERT INTO usc_model_item (id,sort,del,mysm,itemno,name,tablename,implclass,ver,effective,state,islife,type,queryfields,briefexp,remark,contrlfile,sitem,cuser,ctime) "
				+ "SELECT '" + newItemID + "' AS id,sort,del,mysm,itemno,name,tablename,implclass," + ver
				+ " AS ver,0 AS effective,'U' AS state,islife,type,queryfields,briefexp,remark,contrlfile,sitem, '"
				+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_item WHERE id='" + oldItemID + "' ";
		sqls.add(itemSql);

		String itemFiledSql = "INSERT INTO usc_model_field (itemid,id,del,state,no,fieldname,name,ftype,flength,allownull,accuracy,only,ispk,editor,editparams,defaultv,remark,type,cuser,ctime) SELECT '"
				+ newItemID
				+ "' AS itemid,(select UUID()) AS id,del,'U' AS state,no,fieldname,name,ftype,flength,allownull,accuracy,only,ispk,editor,editparams,defaultv,remark,type,'"
				+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_field WHERE del=0 AND itemid='" + oldItemID
				+ "' ";
		sqls.add(itemFiledSql);

		getItemMenusSql(sqls, oldItemID, newItemID);

		List<Map<String, Object>> pros = jdbcTemplate
				.queryForList("SELECT id FROM usc_model_property WHERE del=0 AND itemid='" + oldItemID + "'");
		if (!ObjectHelperUtils.isEmpty(pros))
		{
			for (Map<String, Object> map : pros)
			{
				String oldProID = (String) map.get("id");
				String newProID = getUUID();
				String proSql = "INSERT INTO usc_model_property (itemid,id,del,state,no,name,width,columns,defaultc,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newProID
						+ "' AS id,del,'U' AS state,no,name,width,columns,defaultc,'" + cuser
						+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_property WHERE id='" + oldProID + "'";
				sqls.add(proSql);
				String proFieldSql = "INSERT INTO usc_model_property_field (itemid,rootid,id,sort,del,state,no,name,editable,wline,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newProID
						+ "' AS rootid,(select UUID()) AS id,sort,del,'U' AS state,no,name,editable,wline,'" + cuser
						+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_property_field WHERE del=0 AND itemid='"
						+ oldItemID + "' AND rootid='" + oldProID + "'";
				sqls.add(proFieldSql);
			}
		}
		List<Map<String, Object>> grids = jdbcTemplate
				.queryForList("SELECT id FROM usc_model_grid WHERE del=0 AND itemid='" + oldItemID + "'");
		if (!ObjectHelperUtils.isEmpty(grids))
		{
			for (Map<String, Object> map : grids)
			{
				String oldGridID = (String) map.get("id");
				String newGridID = getUUID();
				String gridSql = "INSERT INTO usc_model_grid (itemid,id,del,state,no,name,type,defaultc,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newGridID + "' AS id,del,'U' AS state,no,name,type,defaultc,'"
						+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_grid WHERE id='" + oldGridID + "'";
				sqls.add(gridSql);

				String gridFieldSql = "INSERT INTO usc_model_grid_field (itemid,rootid,id,sort,del,state,no,name,fieldname,align,screen,width,editable,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newGridID
						+ "' AS rootid,(select UUID()) AS id,sort,del,'U' AS state,no,name,fieldname,align,screen,width,editable,'"
						+ cuser
						+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_grid_field WHERE del=0 AND itemid='"
						+ oldItemID + "' AND rootid='" + oldGridID + "'";
				sqls.add(gridFieldSql);
			}
		}
		List<Map<String, Object>> relations = jdbcTemplate
				.queryForList("SELECT id FROM usc_model_relationpage WHERE del=0 AND itemid='" + oldItemID + "'");
		if (!ObjectHelperUtils.isEmpty(relations))
		{
			for (Map<String, Object> map : relations)
			{
				String oldRelationPageID = (String) map.get("id");
				String newRelationPageID = getUUID();
				String relationPageSql = "INSERT INTO usc_model_relationpage (itemid,id,del,state,no,name,defaultc,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newRelationPageID
						+ "' AS id,del,'U' AS state,no,name,defaultc,'" + cuser
						+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_relationpage WHERE id='"
						+ oldRelationPageID + "'";
				sqls.add(relationPageSql);

				String relationPageSignFieldSql = "INSERT INTO usc_model_relationpage_sign (itemid,rootid,id,sort,del,state,no,name,rtype,itemno,itemgrid,relevanceno,relevancename,param,icon,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newRelationPageID
						+ "' AS rootid,(select UUID()) AS id,sort,del,'U' AS state,no,name,rtype,itemno,itemgrid,relevanceno,relevancename,param,icon,'"
						+ cuser
						+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_relationpage_sign WHERE del=0 AND itemid='"
						+ oldItemID + "' AND rootid='" + oldRelationPageID + "'";
				sqls.add(relationPageSignFieldSql);
			}
		}
		sqls.add("UPDATE usc_model_item SET effective=-1 WHERE id='" + oldItemID + "'");
		jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));

	}

	private String getItemMenusSql(List<String> sqls, String oldItemID, String newItemID)
	{
		List<Map<String, Object>> menu = jdbcTemplate.queryForList(
				"SELECT id FROM usc_model_itemmenu WHERE del=0 AND pid='0' AND itemid='" + oldItemID + "'");
		if (!ObjectHelperUtils.isEmpty(menu))
		{
			for (Map<String, Object> map : menu)
			{
				String oldMenuId = (String) map.get("id");
				String newMenuId = getUUID();
				String itemMenuSql = "INSERT INTO usc_model_itemmenu (itemid,id,pid,sort,del,state,no,name,mtype,frontparam,frontimplclass,param,implclass,webpath,reqparam,wtype,abtype,mno,remark,icon,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newMenuId
						+ "' AS id,pid,sort,del,'U' AS state,no,name,mtype,frontparam,frontimplclass,param,implclass,webpath,reqparam,wtype,abtype,mno,remark,icon,'"
						+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_itemmenu WHERE id='" + oldMenuId
						+ "' ";
				sqls.add(itemMenuSql);
				getChrildMenuNode(sqls, oldItemID, newItemID, oldMenuId, newMenuId);
			}
		}
		return null;
	}

	private void getChrildMenuNode(List<String> sqls, String oldItemID, String newItemID, String oldPid, String newPid)
	{
		List<Map<String, Object>> menus = jdbcTemplate
				.queryForList("SELECT id,mtype,abtype FROM usc_model_itemmenu WHERE del=0 AND pid='" + oldPid
						+ "' AND itemid='" + oldItemID + "'");
		if (!ObjectHelperUtils.isEmpty(menus))
		{
			for (Map<String, Object> map : menus)
			{
				String oldMenuId = (String) map.get("id");
				String newMenuId = getUUID();
				String itemMenuSql = "INSERT INTO usc_model_itemmenu (itemid,id,pid,del,state,sort,no,name,mtype,frontparam,frontimplclass,param,implclass,webpath,reqparam,wtype,abtype,mno,remark,icon,cuser,ctime) SELECT '"
						+ newItemID + "' AS itemid,'" + newMenuId + "' AS id,'" + newPid
						+ "' AS pid,del,'U' AS state,sort,no,name,mtype,frontparam,frontimplclass,param,implclass,webpath,reqparam,wtype,abtype,mno,remark,icon,'"
						+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_itemmenu WHERE id='" + oldMenuId
						+ "' ";
				sqls.add(itemMenuSql);
				Integer mtype = (Integer) map.get("mtype");
				Object abtype = map.get("abtype");
				if (mtype == 0 && abtype != null)
				{
					continue;
				}
				getChrildMenuNode(sqls, oldItemID, newItemID, oldMenuId, newMenuId);
			}
		} else
		{
			return;
		}
	}

	private void cloneRelationship(JSONObject obj)
	{
		List<String> sqls = new Vector<String>();
		Integer ver = obj.getInteger("ver") == null ? 1 : +1;
		String oldItemID = obj.getString("id");
		String newItemID = getUUID();
		String itemSql = "INSERT INTO usc_model_relationship (id,sort,del,mysm,no,name,relationitem,itema,itemb,pitem,relatedevents,ship,ver,effective,state,remark,cuser,ctime) "
				+ "SELECT '" + newItemID
				+ "' AS id,sort,del,mysm,no,name,relationitem,itema,itemb,pitem,relatedevents,ship," + ver
				+ " AS ver,0 AS effective,'U' AS state,remark, '" + cuser
				+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_relationship WHERE id='" + oldItemID + "' ";
		sqls.add(itemSql);
		getItemMenusSql(sqls, oldItemID, newItemID);
		sqls.add("UPDATE usc_model_classview SET effective=-1 WHERE id='" + oldItemID + "'");
		jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
	}

	private void cloneClassview(JSONObject obj)
	{
		List<String> sqls = new Vector<String>();
		Integer ver = obj.getInteger("ver") == null ? 1 : +1;
		String oldViewID = obj.getString("id");
		String newViewID = getUUID();
		String itemSql = "INSERT INTO usc_model_classview (id,sort,del,mysm,no,itemno,name,wcondition,ver,effective,state,remark,cuser,ctime) "
				+ "SELECT '" + newViewID + "' AS id,sort,del,mysm,no,itemno,name,wcondition," + ver
				+ " AS ver,0 AS effective,'U' AS state,remark, '" + cuser
				+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_classview WHERE id='" + oldViewID + "' ";
		sqls.add(itemSql);
//		getItemMenusSql(sqls, oldItemID, newItemID);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT id FROM usc_model_classview_node WHERE del=0 AND state='F' AND pid='0'");
		if (!ObjectHelperUtils.isEmpty(list))
		{
			for (Map<String, Object> map : list)
			{
				String oldNodeID = String.valueOf(map.get("id"));
				String newNodeID = String.valueOf(map.get("id"));
				String nodelSql = "INSERT INTO usc_model_classview_node (itemid,id,pid,sort,del,mysm,no,itemno,datacondition,nodecondition,state,remark,cuser,ctime) "
						+ "SELECT '" + newViewID + "' AS itemid,'" + newNodeID
						+ "' AS id,'0' AS pid,sort,del,mysm,no,itemno,datacondition,nodecondition,state,remark," + "'"
						+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_classview_node WHERE id='"
						+ oldNodeID + "'";
				sqls.add(nodelSql);
				getChrildClassNode(sqls, oldViewID, newViewID, oldNodeID, newNodeID);
			}
		}
		sqls.add("UPDATE usc_model_classview SET effective=-1 WHERE id='" + oldViewID + "'");
		jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
	}

	private void getChrildClassNode(List<String> sqls, String oldViewID, String newViewID, String oldPid, String newPid)
	{
		List<Map<String, Object>> menus = jdbcTemplate
				.queryForList("SELECT id FROM usc_model_classview_node WHERE del=0 AND pid='" + oldPid
						+ "' AND itemid='" + oldViewID + "'");
		if (!ObjectHelperUtils.isEmpty(menus))
		{
			for (Map<String, Object> map : menus)
			{
				String oldNodeId = (String) map.get("id");
				String newNodeId = getUUID();
				String classViewNodeSql = "INSERT INTO usc_model_classview_node (itemid,id,pid,sort,del,mysm,no,itemno,datacondition,nodecondition,state,remark,cuser,ctime) "
						+ "SELECT '" + newViewID + "' AS itemid,'" + newNodeId
						+ "' AS id,'0' AS pid,sort,del,mysm,no,itemno,datacondition,nodecondition,state,remark," + "'"
						+ cuser + "' AS cuser,(select NOW()) AS ctime FROM usc_model_classview_node WHERE id='"
						+ oldNodeId + "'";
				sqls.add(classViewNodeSql);
				getChrildMenuNode(sqls, oldViewID, newViewID, oldNodeId, newNodeId);
			}
		}
	}

	private void cloneQueryview(JSONObject obj)
	{
		List<String> sqls = new Vector<String>();
		Integer ver = obj.getInteger("ver") == null ? 1 : +1;
		String oldItemID = obj.getString("id");
		String newItemID = getUUID();
		String itemSql = "INSERT INTO usc_model_queryview (id,sort,del,mysm,no,itemno,name,wcondition,ver,effective,state,remark,cuser,ctime) "
				+ "SELECT '" + newItemID + "' AS id,sort,del,mysm,no,itemno,name,wcondition," + ver
				+ " AS ver,0 AS effective,'U' AS state,remark, '" + cuser
				+ "' AS cuser,(select NOW()) AS ctime FROM usc_model_queryview WHERE id='" + oldItemID + "' ";
		sqls.add(itemSql);
		getItemMenusSql(sqls, oldItemID, newItemID);
		sqls.add("UPDATE usc_model_queryview SET effective=-1 WHERE id='" + oldItemID + "'");
		jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
	}

	@Override
	public Object cancelUpgradeModel(String param)
	{
		return null;
	}

	@Override
	public boolean isModelingUser(String userName)
	{
		if (userName == null)
		{
			return false;
		}
		if (redis.hasKey("OPENMODEL"))
		{

			String muser = (String) redis.get("OPENMODEL");
			if (userName.equals(muser))
			{
				return true;
			}
		}
		return false;
	}

	private String getUUID()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
