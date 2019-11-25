package com.usc.test.mate.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDefaultValues
{

	public static List<Map<String, Object>> getDefaultField(int itemType, String itemid)
	{
		List<Map<String, Object>> listFieldMap = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> MapID = new HashMap<String, Object>();
		MapID.put("no", "id");
		MapID.put("fieldname", "id");
		MapID.put("name", "唯一ID");
		MapID.put("ftype", "VARCHAR");
		MapID.put("flength", 40);
		MapID.put("allownull", 0);
		MapID.put("only", 1);
		MapID.put("ispk", 1);
		MapID.put("type", 0);
		MapID.put("defaultv", null);
		MapID.put("editor", "TextBox");
		MapID.put("editparams", null);
		MapID.put("itemid", itemid);
		listFieldMap.add(MapID);

		HashMap<String, Object> MapDEL = new HashMap<String, Object>();
		MapDEL.put("no", "del");
		MapDEL.put("fieldname", "del");
		MapDEL.put("name", "删除标记");
		MapDEL.put("ftype", "INT");
		MapDEL.put("flength", 1);
		MapDEL.put("allownull", 0);
		MapDEL.put("only", 0);
		MapDEL.put("ispk", 0);
		MapDEL.put("type", 0);
		MapDEL.put("defaultv", null);
		MapDEL.put("editor", "TextBox");
		MapDEL.put("editparams", null);
		MapDEL.put("itemid", itemid);
		listFieldMap.add(MapDEL);

		HashMap<String, Object> MapDSNO = new HashMap<String, Object>();
		MapDSNO.put("no", "dsno");
		MapDSNO.put("fieldname", "dsno");
		MapDSNO.put("name", "数据包号");
		MapDSNO.put("ftype", "VARCHAR");
		MapDSNO.put("flength", 40);
		MapDSNO.put("allownull", 1);
		MapDSNO.put("only", 0);
		MapDSNO.put("ispk", 0);
		MapDSNO.put("type", 0);
		MapDSNO.put("defaultv", null);
		MapDSNO.put("editor", "TextBox");
		MapDSNO.put("editparams", null);
		MapDSNO.put("itemid", itemid);
		listFieldMap.add(MapDSNO);

		HashMap<String, Object> MapMSYM = new HashMap<String, Object>();
		MapMSYM.put("no", "mysm");
		MapMSYM.put("fieldname", "mysm");
		MapMSYM.put("name", "修改标记");
		MapMSYM.put("ftype", "VARCHAR");
		MapMSYM.put("flength", 1);
		MapMSYM.put("allownull", 1);
		MapMSYM.put("only", 0);
		MapMSYM.put("ispk", 0);
		MapMSYM.put("type", 0);
		MapMSYM.put("defaultv", null);
		MapMSYM.put("editor", "TextBox");
		MapMSYM.put("editparams", null);
		MapMSYM.put("itemid", itemid);
		listFieldMap.add(MapMSYM);

		HashMap<String, Object> MapOWNER = new HashMap<String, Object>();
		MapOWNER.put("no", "owner");
		MapOWNER.put("fieldname", "owner");
		MapOWNER.put("name", "所有者");
		MapOWNER.put("ftype", "VARCHAR");
		MapOWNER.put("flength", 40);
		MapOWNER.put("allownull", 1);
		MapOWNER.put("only", 0);
		MapOWNER.put("ispk", 0);
		MapOWNER.put("type", 0);
		MapOWNER.put("defaultv", null);
		MapOWNER.put("editor", "TextBox");
		MapOWNER.put("editparams", null);
		MapOWNER.put("itemid", itemid);
		listFieldMap.add(MapOWNER);

		if (itemType == 0 || itemType == 1)
		{
			HashMap<String, Object> MapNO = new HashMap<String, Object>();
			MapNO.put("no", "no");
			MapNO.put("fieldname", "no");
			MapNO.put("name", "编码");
			MapNO.put("ftype", "VARCHAR");
			MapNO.put("flength", 40);
			MapNO.put("allownull", 1);
			MapNO.put("only", 0);
			MapNO.put("ispk", 0);
			MapNO.put("type", 1);
			MapNO.put("defaultv", null);
			MapNO.put("editor", "TextBox");
			MapNO.put("editparams", null);
			MapNO.put("itemid", itemid);
			listFieldMap.add(MapNO);

			HashMap<String, Object> MapNAME = new HashMap<String, Object>();
			MapNAME.put("no", "name");
			MapNAME.put("fieldname", "name");
			MapNAME.put("name", "名称");
			MapNAME.put("ftype", "VARCHAR");
			MapNAME.put("flength", 40);
			MapNAME.put("allownull", 1);
			MapNAME.put("only", 0);
			MapNAME.put("ispk", 0);
			MapNAME.put("type", 1);
			MapNAME.put("defaultv", null);
			MapNAME.put("editor", "TextBox");
			MapNAME.put("editparams", null);
			MapNAME.put("itemid", itemid);
			listFieldMap.add(MapNAME);

			HashMap<String, Object> MapVER = new HashMap<String, Object>();
			MapVER.put("no", "ver");
			MapVER.put("fieldname", "ver");
			MapVER.put("name", "版本");
			MapVER.put("ftype", "VARCHAR");
			MapVER.put("flength", 10);
			MapVER.put("allownull", 1);
			MapVER.put("only", 0);
			MapVER.put("ispk", 0);
			MapVER.put("type", 1);
			MapVER.put("defaultv", null);
			MapVER.put("editor", "TextBox");
			MapVER.put("editparams", null);
			MapVER.put("itemid", itemid);
			listFieldMap.add(MapVER);

			HashMap<String, Object> MapTYPE = new HashMap<String, Object>();
			MapTYPE.put("no", "type");
			MapTYPE.put("fieldname", "type");
			MapTYPE.put("name", "类型");
			MapTYPE.put("ftype", "VARCHAR");
			MapTYPE.put("flength", 20);
			MapTYPE.put("allownull", 1);
			MapTYPE.put("only", 0);
			MapTYPE.put("ispk", 0);
			MapTYPE.put("type", 1);
			MapTYPE.put("defaultv", null);
			MapTYPE.put("editor", "TextBox");
			MapTYPE.put("editparams", null);
			MapTYPE.put("itemid", itemid);
			listFieldMap.add(MapTYPE);

			if (itemType == 1)
			{
				HashMap<String, Object> MapFNAME = new HashMap<String, Object>();
				MapFNAME.put("no", "fname");
				MapFNAME.put("fieldname", "fname");
				MapFNAME.put("name", "文件");
				MapFNAME.put("ftype", "VARCHAR");
				MapFNAME.put("flength", 200);
				MapFNAME.put("allownull", 1);
				MapFNAME.put("only", 0);
				MapFNAME.put("ispk", 0);
				MapFNAME.put("type", 0);
				MapFNAME.put("defaultv", null);
				MapFNAME.put("editor", "FileSelector");
				MapFNAME.put("editparams", null);
				MapFNAME.put("itemid", itemid);
				listFieldMap.add(MapFNAME);

				HashMap<String, Object> MapFTYPE = new HashMap<String, Object>();
				MapFTYPE.put("no", "ftype");
				MapFTYPE.put("fieldname", "ftype");
				MapFTYPE.put("name", "文件类型");
				MapFTYPE.put("ftype", "VARCHAR");
				MapFTYPE.put("flength", 20);
				MapFTYPE.put("allownull", 1);
				MapFTYPE.put("only", 0);
				MapFTYPE.put("ispk", 0);
				MapFTYPE.put("type", 0);
				MapFTYPE.put("defaultv", null);
				MapFTYPE.put("editor", "TextBox");
				MapFTYPE.put("editparams", null);
				MapFTYPE.put("itemid", itemid);
				listFieldMap.add(MapFTYPE);

				HashMap<String, Object> MapFLOCATION = new HashMap<String, Object>();
				MapFLOCATION.put("no", "flocation");
				MapFLOCATION.put("fieldname", "flocation");
				MapFLOCATION.put("name", "文件路径");
				MapFLOCATION.put("ftype", "VARCHAR");
				MapFLOCATION.put("flength", 2000);
				MapFLOCATION.put("allownull", 1);
				MapFLOCATION.put("only", 0);
				MapFLOCATION.put("ispk", 0);
				MapFLOCATION.put("type", 0);
				MapFLOCATION.put("defaultv", null);
				MapFLOCATION.put("editor", "TextBox");
				MapFLOCATION.put("editparams", null);
				MapFLOCATION.put("itemid", itemid);
				listFieldMap.add(MapFLOCATION);

				HashMap<String, Object> MapFSIZE = new HashMap<String, Object>();
				MapFSIZE.put("no", "fsize");
				MapFSIZE.put("fieldname", "fsize");
				MapFSIZE.put("name", "文件大小");
				MapFSIZE.put("ftype", "VARCHAR");
				MapFSIZE.put("flength", 50);
				MapFSIZE.put("allownull", 1);
				MapFSIZE.put("only", 0);
				MapFSIZE.put("ispk", 0);
				MapFSIZE.put("type", 0);
				MapFSIZE.put("defaultv", null);
				MapFSIZE.put("editor", "TextBox");
				MapFSIZE.put("editparams", null);
				MapFSIZE.put("itemid", itemid);
				listFieldMap.add(MapFSIZE);

				HashMap<String, Object> MapFMTIME = new HashMap<String, Object>();
				MapFMTIME.put("no", "fmtime");
				MapFMTIME.put("fieldname", "fmtime");
				MapFMTIME.put("name", "文件修改时间");
				MapFMTIME.put("ftype", "DATETIME");
				MapFMTIME.put("flength", 0);
				MapFMTIME.put("allownull", 1);
				MapFMTIME.put("only", 0);
				MapFMTIME.put("ispk", 0);
				MapFMTIME.put("type", 0);
				MapFMTIME.put("defaultv", null);
				MapFMTIME.put("editor", "DateTime");
				MapFMTIME.put("editparams", "{\"format\":\"YYYY-MM-DD HH:mm:ss\"}");
				MapFMTIME.put("itemid", itemid);
				listFieldMap.add(MapFMTIME);

				HashMap<String, Object> MapPFNAME = new HashMap<String, Object>();
				MapPFNAME.put("no", "pfname");
				MapPFNAME.put("fieldname", "pfname");
				MapPFNAME.put("name", "PDF文件名称");
				MapPFNAME.put("ftype", "VARCHAR");
				MapPFNAME.put("flength", 100);
				MapPFNAME.put("allownull", 1);
				MapPFNAME.put("only", 0);
				MapPFNAME.put("ispk", 0);
				MapPFNAME.put("type", 0);
				MapPFNAME.put("defaultv", null);
				MapPFNAME.put("editor", "TextBox");
				MapPFNAME.put("editparams", null);
				MapPFNAME.put("itemid", itemid);
				listFieldMap.add(MapPFNAME);

				HashMap<String, Object> MapPFLOCATION = new HashMap<String, Object>();
				MapPFLOCATION.put("no", "pflocation");
				MapPFLOCATION.put("fieldname", "pflocation");
				MapPFLOCATION.put("name", "PDF文件路径");
				MapPFLOCATION.put("ftype", "VARCHAR");
				MapPFLOCATION.put("flength", 2000);
				MapPFLOCATION.put("allownull", 1);
				MapPFLOCATION.put("only", 0);
				MapPFLOCATION.put("ispk", 0);
				MapPFLOCATION.put("type", 0);
				MapPFLOCATION.put("defaultv", null);
				MapPFLOCATION.put("editor", "TextBox");
				MapPFLOCATION.put("editparams", null);
				MapPFLOCATION.put("itemid", itemid);
				listFieldMap.add(MapPFLOCATION);

				HashMap<String, Object> MapPFSIZE = new HashMap<String, Object>();
				MapPFSIZE.put("no", "pfsize");
				MapPFSIZE.put("fieldname", "pfsize");
				MapPFSIZE.put("name", "PDF文件大小");
				MapPFSIZE.put("ftype", "VARCHAR");
				MapPFSIZE.put("flength", 50);
				MapPFSIZE.put("allownull", 1);
				MapPFSIZE.put("only", 0);
				MapPFSIZE.put("ispk", 0);
				MapPFSIZE.put("type", 0);
				MapPFSIZE.put("defaultv", null);
				MapPFSIZE.put("editor", "TextBox");
				MapPFSIZE.put("editparams", null);
				MapPFSIZE.put("itemid", itemid);
				listFieldMap.add(MapPFSIZE);

			}
		} else if (itemType == 2)
		{
			HashMap<String, Object> MapItemA = new HashMap<String, Object>();
			MapItemA.put("no", "itema");
			MapItemA.put("fieldname", "itema");
			MapItemA.put("name", "对象A");
			MapItemA.put("ftype", "VARCHAR");
			MapItemA.put("flength", 40);
			MapItemA.put("allownull", 1);
			MapItemA.put("only", 0);
			MapItemA.put("ispk", 0);
			MapItemA.put("type", 1);
			MapItemA.put("defaultv", null);
			MapItemA.put("editor", "TextBox");
			MapItemA.put("editparams", null);
			MapItemA.put("itemid", itemid);
			listFieldMap.add(MapItemA);

			HashMap<String, Object> MapItemAID = new HashMap<String, Object>();
			MapItemAID.put("no", "itemaid");
			MapItemAID.put("fieldname", "itemaid");
			MapItemAID.put("name", "对象A.ID");
			MapItemAID.put("ftype", "VARCHAR");
			MapItemAID.put("flength", 40);
			MapItemAID.put("allownull", 1);
			MapItemAID.put("only", 0);
			MapItemAID.put("ispk", 0);
			MapItemAID.put("type", 1);
			MapItemAID.put("defaultv", null);
			MapItemAID.put("editor", "TextBox");
			MapItemAID.put("editparams", null);
			MapItemAID.put("itemid", itemid);
			listFieldMap.add(MapItemAID);

			HashMap<String, Object> MapItemB = new HashMap<String, Object>();
			MapItemB.put("no", "itemb");
			MapItemB.put("fieldname", "itemb");
			MapItemB.put("name", "对象B");
			MapItemB.put("ftype", "VARCHAR");
			MapItemB.put("flength", 40);
			MapItemB.put("allownull", 1);
			MapItemB.put("only", 0);
			MapItemB.put("ispk", 0);
			MapItemB.put("type", 1);
			MapItemB.put("defaultv", null);
			MapItemB.put("editor", "TextBox");
			MapItemB.put("editparams", null);
			MapItemB.put("itemid", itemid);
			listFieldMap.add(MapItemB);

			HashMap<String, Object> MapItemBID = new HashMap<String, Object>();
			MapItemBID.put("no", "itembid");
			MapItemBID.put("fieldname", "itembid");
			MapItemBID.put("name", "对象B.ID");
			MapItemBID.put("ftype", "VARCHAR");
			MapItemBID.put("flength", 40);
			MapItemBID.put("allownull", 1);
			MapItemBID.put("only", 0);
			MapItemBID.put("ispk", 0);
			MapItemBID.put("type", 1);
			MapItemBID.put("defaultv", null);
			MapItemBID.put("editor", "TextBox");
			MapItemBID.put("editparams", null);
			MapItemBID.put("itemid", itemid);
			listFieldMap.add(MapItemBID);
		} else if (itemType == 3)
		{
			HashMap<String, Object> MapItemID = new HashMap<String, Object>();
			MapItemID.put("no", "itemid");
			MapItemID.put("fieldname", "itemid");
			MapItemID.put("name", "对象ID");
			MapItemID.put("ftype", "VARCHAR");
			MapItemID.put("flength", 40);
			MapItemID.put("allownull", 1);
			MapItemID.put("only", 0);
			MapItemID.put("ispk", 0);
			MapItemID.put("type", 1);
			MapItemID.put("defaultv", null);
			MapItemID.put("editor", "TextBox");
			MapItemID.put("editparams", null);
			MapItemID.put("itemid", itemid);
			listFieldMap.add(MapItemID);

			HashMap<String, Object> MapNodeID = new HashMap<String, Object>();
			MapNodeID.put("no", "nodeid");
			MapNodeID.put("fieldname", "nodeid");
			MapNodeID.put("name", "节点ID");
			MapNodeID.put("ftype", "VARCHAR");
			MapNodeID.put("flength", 40);
			MapNodeID.put("allownull", 1);
			MapNodeID.put("only", 0);
			MapNodeID.put("ispk", 0);
			MapNodeID.put("type", 1);
			MapNodeID.put("defaultv", null);
			MapNodeID.put("editor", "TextBox");
			MapNodeID.put("editparams", null);
			MapNodeID.put("itemid", itemid);
			listFieldMap.add(MapNodeID);
		}

		HashMap<String, Object> MapREMARK = new HashMap<String, Object>();
		MapREMARK.put("no", "remark");
		MapREMARK.put("fieldname", "remark");
		MapREMARK.put("name", "备注");
		MapREMARK.put("ftype", "VARCHAR");
		MapREMARK.put("flength", 200);
		MapREMARK.put("allownull", 1);
		MapREMARK.put("only", 0);
		MapREMARK.put("ispk", 0);
		MapREMARK.put("type", 1);
		MapREMARK.put("defaultv", null);
		MapREMARK.put("editor", "TextBox");
		MapREMARK.put("editparams", null);
		MapREMARK.put("itemid", itemid);
		listFieldMap.add(MapREMARK);

		HashMap<String, Object> MapCUSER = new HashMap<String, Object>();
		MapCUSER.put("no", "cuser");
		MapCUSER.put("fieldname", "cuser");
		MapCUSER.put("name", "创建人");
		MapCUSER.put("ftype", "VARCHAR");
		MapCUSER.put("flength", 40);
		MapCUSER.put("allownull", 1);
		MapCUSER.put("only", 0);
		MapCUSER.put("ispk", 0);
		MapCUSER.put("type", 0);
		MapCUSER.put("defaultv", null);
		MapCUSER.put("editor", "TextBox");
		MapCUSER.put("editparams", null);
		MapCUSER.put("itemid", itemid);
		listFieldMap.add(MapCUSER);

		HashMap<String, Object> MapCTIME = new HashMap<String, Object>();
		MapCTIME.put("no", "ctime");
		MapCTIME.put("fieldname", "ctime");
		MapCTIME.put("name", "创建时间");
		MapCTIME.put("ftype", "DATETIME");
		MapCTIME.put("flength", null);
		MapCTIME.put("allownull", 1);
		MapCTIME.put("only", 0);
		MapCTIME.put("ispk", 0);
		MapCTIME.put("type", 0);
		MapCTIME.put("defaultv", null);
		MapCTIME.put("editor", "DateTime");
		MapCTIME.put("editparams", "{\"format\":\"YYYY-MM-DD HH:mm:ss\"}");
		MapCTIME.put("itemid", itemid);
		listFieldMap.add(MapCTIME);

		HashMap<String, Object> MapMUSER = new HashMap<String, Object>();
		MapMUSER.put("no", "muser");
		MapMUSER.put("fieldname", "muser");
		MapMUSER.put("name", "修改人");
		MapMUSER.put("ftype", "VARCHAR");
		MapMUSER.put("flength", 40);
		MapMUSER.put("allownull", 1);
		MapMUSER.put("only", 0);
		MapMUSER.put("ispk", 0);
		MapMUSER.put("type", 0);
		MapMUSER.put("defaultv", null);
		MapMUSER.put("editor", "TextBox");
		MapMUSER.put("editparams", null);
		MapMUSER.put("itemid", itemid);
		listFieldMap.add(MapMUSER);

		HashMap<String, Object> MapMTIME = new HashMap<String, Object>();
		MapMTIME.put("no", "mtime");
		MapMTIME.put("fieldname", "mtime");
		MapMTIME.put("name", "修改时间");
		MapMTIME.put("ftype", "DATETIME");
		MapMTIME.put("flength", null);
		MapMTIME.put("allownull", 1);
		MapMTIME.put("only", 0);
		MapMTIME.put("ispk", 0);
		MapMTIME.put("type", 0);
		MapMTIME.put("defaultv", null);
		MapMTIME.put("editor", "DateTime");
		MapMTIME.put("editparams", "{\"format\":\"YYYY-MM-DD HH:mm:ss\"}");
		MapMTIME.put("itemid", itemid);
		listFieldMap.add(MapMTIME);

		HashMap<String, Object> MapDUSER = new HashMap<String, Object>();
		MapDUSER.put("no", "duser");
		MapDUSER.put("fieldname", "duser");
		MapDUSER.put("name", "删除人");
		MapDUSER.put("ftype", "VARCHAR");
		MapDUSER.put("flength", 40);
		MapDUSER.put("allownull", 1);
		MapDUSER.put("only", 0);
		MapDUSER.put("ispk", 0);
		MapDUSER.put("type", 0);
		MapDUSER.put("defaultv", null);
		MapDUSER.put("editor", "TextBox");
		MapDUSER.put("editparams", null);
		MapDUSER.put("itemid", itemid);
		listFieldMap.add(MapDUSER);

		HashMap<String, Object> MapDTIME = new HashMap<String, Object>();
		MapDTIME.put("no", "dtime");
		MapDTIME.put("fieldname", "dtime");
		MapDTIME.put("name", "删除时间");
		MapDTIME.put("ftype", "DATETIME");
		MapDTIME.put("flength", null);
		MapDTIME.put("allownull", 1);
		MapDTIME.put("only", 0);
		MapDTIME.put("ispk", 0);
		MapDTIME.put("type", 0);
		MapDTIME.put("defaultv", null);
		MapDTIME.put("editor", "DateTime");
		MapDTIME.put("editparams", "{\"format\":\"YYYY-MM-DD HH:mm:ss\"}");
		MapDTIME.put("itemid", itemid);
		listFieldMap.add(MapDTIME);

		HashMap<String, Object> MapSTATE = new HashMap<String, Object>();
		MapSTATE.put("no", "state");
		MapSTATE.put("fieldname", "state");
		MapSTATE.put("name", "状态");
		MapSTATE.put("ftype", "VARCHAR");
		MapSTATE.put("flength", 2);
		MapSTATE.put("allownull", 1);
		MapSTATE.put("only", 0);
		MapSTATE.put("ispk", 0);
		MapSTATE.put("type", 0);
		MapSTATE.put("defaultv", null);
		MapSTATE.put("editor", "TextBox");
		MapSTATE.put("editparams", null);
		MapSTATE.put("itemid", itemid);
		listFieldMap.add(MapSTATE);

		return listFieldMap;

	}

	public static List<Map<String, Object>> getDefaultMenu(List<Map<String, Object>> list, String itemid)
	{

		if (list != null && !list.isEmpty())
		{
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list)
			{
				Map<String, Object> Map = new HashMap<String, Object>();
				Map.put("pid", "0");
				Map.put("itemid", itemid);
				Map.put("no", map.get("no"));
				Map.put("name", map.get("name"));
				Map.put("mtype", "0");
				Map.put("icon", map.get("icon"));
				Map.put("implclass", map.get("implclass"));
				Map.put("wtype", map.get("wtype"));
				Map.put("abtype", map.get("abtype"));
				Map.put("reqparam", map.get("reqparam"));
				listMap.add(Map);
			}
			return listMap;
		}
		return null;

	}

	public static List<Map<String, Object>> getDefaultProperty(String itemid)
	{
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> MapNo = new HashMap<String, Object>();
		MapNo.put("defaultc", 1);
		MapNo.put("no", "default");
		MapNo.put("name", "默认属性页");
		MapNo.put("columns", 2);
		MapNo.put("itemid", itemid);
		listMap.add(MapNo);
		return listMap;

	}

	public static List<Map<String, Object>> getDefaultPropertyField(int itemType, String itemid, String rootid)
	{
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		if (itemType == 0 || itemType == 1)
		{
			Map<String, Object> MapNo = new HashMap<String, Object>();
			MapNo.put("no", "no");
			MapNo.put("name", "编码");
			MapNo.put("editable", 1);
			MapNo.put("itemid", itemid);
			MapNo.put("rootid", rootid);
			listMap.add(MapNo);
			Map<String, Object> MapName = new HashMap<String, Object>();
			MapName.put("no", "name");
			MapName.put("name", "名称");
			MapName.put("editable", 1);
			MapName.put("itemid", itemid);
			MapName.put("rootid", rootid);
			listMap.add(MapName);

			if (itemType == 1)
			{
				Map<String, Object> MapFName = new HashMap<String, Object>();
				MapFName.put("no", "fname");
				MapFName.put("name", "文件标题");
				MapFName.put("editable", 1);
				MapFName.put("itemid", itemid);
				MapFName.put("rootid", rootid);
				listMap.add(MapFName);
				Map<String, Object> MapFLocation = new HashMap<String, Object>();
				MapFLocation.put("no", "flocation");
				MapFLocation.put("name", "选择文件");
				MapFLocation.put("editable", 1);
				MapFLocation.put("itemid", itemid);
				MapFLocation.put("rootid", rootid);
				listMap.add(MapFLocation);
			}
		}

		Map<String, Object> MapRemark = new HashMap<String, Object>();
		MapRemark.put("no", "remark");
		MapRemark.put("name", "备注");
		MapRemark.put("editable", 1);
		MapRemark.put("itemid", itemid);
		MapRemark.put("rootid", rootid);
		listMap.add(MapRemark);

		return listMap;

	}

	public static List<Map<String, Object>> getDefaultGrid(String itemid)
	{
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> MapNo = new HashMap<String, Object>();
		MapNo.put("defaultc", 1);
		MapNo.put("no", "default");
		MapNo.put("name", "默认表格");
		MapNo.put("itemid", itemid);
		MapNo.put("type", 1);
		listMap.add(MapNo);
		return listMap;

	}

	public static List<Map<String, Object>> getDefaultGridField(int itemType, String itemid, String rootid)
	{
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		if (itemType == 0 || itemType == 1)
		{
			Map<String, Object> MapNo = new HashMap<String, Object>();
			MapNo.put("no", "no");
			MapNo.put("name", "编码");
			MapNo.put("align", "left");
			MapNo.put("width", 100);
			MapNo.put("editable", 1);
			MapNo.put("itemid", itemid);
			MapNo.put("rootid", rootid);
			listMap.add(MapNo);
			Map<String, Object> MapName = new HashMap<String, Object>();
			MapName.put("no", "name");
			MapName.put("name", "名称");
			MapName.put("align", "left");
			MapName.put("width", 100);
			MapName.put("editable", 1);
			MapName.put("itemid", itemid);
			MapName.put("rootid", rootid);
			listMap.add(MapName);
			if (itemType == 1)
			{
				Map<String, Object> MapFName = new HashMap<String, Object>();
				MapFName.put("no", "fname");
				MapFName.put("name", "文件标题");
				MapFName.put("align", "left");
				MapFName.put("width", 100);
				MapFName.put("editable", 1);
				MapFName.put("itemid", itemid);
				MapFName.put("rootid", rootid);
				listMap.add(MapFName);
			}
		} else if (itemType == 2)
		{
			Map<String, Object> MapItemA = new HashMap<String, Object>();
			MapItemA.put("no", "itema");
			MapItemA.put("name", "对象A");
			MapItemA.put("align", "left");
			MapItemA.put("width", 100);
			MapItemA.put("editable", 1);
			MapItemA.put("itemid", itemid);
			MapItemA.put("rootid", rootid);
			listMap.add(MapItemA);
			Map<String, Object> MapItemAID = new HashMap<String, Object>();
			MapItemAID.put("no", "itemaid");
			MapItemAID.put("name", "对象A.ID");
			MapItemAID.put("align", "left");
			MapItemAID.put("width", 100);
			MapItemAID.put("editable", 1);
			MapItemAID.put("itemid", itemid);
			MapItemAID.put("rootid", rootid);
			listMap.add(MapItemAID);

			Map<String, Object> MapItemB = new HashMap<String, Object>();
			MapItemB.put("no", "itemb");
			MapItemB.put("name", "对象B");
			MapItemB.put("align", "left");
			MapItemB.put("width", 100);
			MapItemB.put("editable", 1);
			MapItemB.put("itemid", itemid);
			MapItemB.put("rootid", rootid);
			listMap.add(MapItemB);
			Map<String, Object> MapItemBID = new HashMap<String, Object>();
			MapItemBID.put("no", "itembid");
			MapItemBID.put("name", "对象B.ID");
			MapItemBID.put("align", "left");
			MapItemBID.put("width", 100);
			MapItemBID.put("editable", 1);
			MapItemBID.put("itemid", itemid);
			MapItemBID.put("rootid", rootid);
			listMap.add(MapItemBID);
		}

		Map<String, Object> MapRemark = new HashMap<String, Object>();
		MapRemark.put("no", "remark");
		MapRemark.put("name", "备注");
		MapRemark.put("align", "left");
		MapRemark.put("width", 100);
		MapRemark.put("editable", 1);
		MapRemark.put("itemid", itemid);
		MapRemark.put("rootid", rootid);
		listMap.add(MapRemark);
		Map<String, Object> MapCuser = new HashMap<String, Object>();
		MapCuser.put("no", "cuser");
		MapCuser.put("name", "创建人");
		MapCuser.put("align", "left");
		MapCuser.put("width", 100);
		MapCuser.put("editable", 1);
		MapCuser.put("itemid", itemid);
		MapCuser.put("rootid", rootid);
		listMap.add(MapCuser);
		Map<String, Object> MapCtime = new HashMap<String, Object>();
		MapCtime.put("no", "ctime");
		MapCtime.put("name", "创建时间");
		MapCtime.put("align", "left");
		MapCtime.put("width", 100);
		MapCtime.put("editable", 1);
		MapCtime.put("itemid", itemid);
		MapCtime.put("rootid", rootid);
		listMap.add(MapCtime);
		Map<String, Object> MapState = new HashMap<String, Object>();
		MapState.put("no", "state");
		MapState.put("name", "状态");
		MapState.put("align", "left");
		MapState.put("width", 100);
		MapState.put("editable", 1);
		MapState.put("itemid", itemid);
		MapState.put("rootid", rootid);
		listMap.add(MapState);
		return listMap;

	}

	public static List<Map<String, Object>> getDefaultRelationPage()
	{
		return null;

	}

	public static List<Map<String, Object>> getDefaultRelationPageSign()
	{
		return null;

	}
}
