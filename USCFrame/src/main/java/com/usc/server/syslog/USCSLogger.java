package com.usc.server.syslog;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.util.TypeUtils;
import com.usc.app.sys.online.OnlineUsers;
import com.usc.app.util.Symbols;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.file.FileObject;
import com.usc.server.jdbc.ItemUtiilities;
import com.usc.server.md.ItemField;
import com.usc.server.md.USCModelMate;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.util.SystemTime;
import com.usc.util.ObjectHelperUtils;

public class USCSLogger
{
	public final String SYSTEM_ACTION = "S";
	public final String DATA_ACTION = "D";
	public final String FILE_ACTION = "F";

	public USCSLogger()
	{
	}

	@Async
	public void writeNewItemLog(ApplicationContext context, USCObject object, LOGActionEnum actionName)
	{
		SLOGObject slogObject = setDefaultLogMsg(context, object, actionName);
		slogObject.setCtime((Timestamp) object.getFieldValue("ctime"));
		slogObject.setType(DATA_ACTION);
		Object name = object.getFieldValue("name");
		slogObject.setDETAILS("用户" + Symbols.L_MiddleBracket + slogObject.getCuser() + Symbols.R_MiddleBracket
				+ Symbols.SPACE + "新建数据:" + (name != null ? name : slogObject.getCuser()));
		writeLog(slogObject);
	}

	@Async
	public void writeDeleteItemLog(ApplicationContext context, USCObject object, LOGActionEnum actionName)
	{

		SLOGObject slogObject = setDefaultLogMsg(context, object, actionName);
		slogObject.setCtime((Timestamp) object.getFieldValue("dtime"));
		slogObject.setType(DATA_ACTION);
		Object name = object.getFieldValue("name");
		slogObject.setDETAILS("用户" + Symbols.L_MiddleBracket + slogObject.getCuser() + Symbols.R_MiddleBracket
				+ Symbols.SPACE + " 删除数据:" + (name != null ? name : slogObject.getOBJID()));
		writeLog(slogObject);
	}

	@Async
	public void writeModifyItemLog(ApplicationContext context, USCObject object, Map<String, Object> mObj)
	{
		SLOGObject slogObject = setDefaultLogMsg(context, object, LOGActionEnum.MODIFY);
		slogObject.setCtime((Timestamp) object.getFieldValue("mtime"));
		slogObject.setType(DATA_ACTION);
		mObj.remove("USC_OBJECT");
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String k : mObj.keySet())
		{
			if (!FieldNameInitConst.isSystemField(k))
			{
				ItemField field = USCModelMate.getItemField(object.getItemNo(), k);
				String fname = field.getName();
				Object objectV = object.getFieldValue(k);
				Object mobjectV = mObj.get(k);
				if ((objectV == null && mobjectV != null))
				{
					if (i > 0)
						buffer.append("\n");
					i++;
					buffer.append(Symbols.L_MiddleBracket + Symbols.L_Bracket + "字段:" + fname + Symbols.R_Bracket
							+ Symbols.SPACE + mobjectV + Symbols.UNDERLINE + "空" + Symbols.SQUARE + objectV
							+ Symbols.R_MiddleBracket);

					continue;
				}
				if ((objectV != null && mobjectV == null))
				{
					if (i > 0)
						buffer.append("\n");
					i++;
					buffer.append(Symbols.L_MiddleBracket + Symbols.L_Bracket + "字段:" + fname + Symbols.R_Bracket
							+ Symbols.SPACE + mobjectV + Symbols.SQUARE + objectV + Symbols.UNDERLINE + "空"
							+ Symbols.R_MiddleBracket);
					continue;
				}
				if (objectV != null && mobjectV != null)
				{
					Boolean fg = false;
					String fType = field.getFType();
					switch (fType)
					{
					case "INT":
						fg = (int) objectV != (int) mobjectV;
						break;
					case "FLOAT":
						fg = (Float) objectV != (Float) mobjectV;
						break;
					case "DOUBLE":
						fg = (Double) objectV != (Double) mobjectV;
						break;
					case "BOOLEAN":
						fg = TypeUtils.castToBoolean(objectV) == TypeUtils.castToBoolean(mobjectV);
						break;
					case "BigDecimal":
						fg = ((BigDecimal) objectV) != ((BigDecimal) mobjectV);
						break;
					case "DATETIME":
						fg = !(TypeUtils.castToTimestamp(objectV))
								.equals(SystemTime.getTimestamp(field.getFieldEditor().getDateFormatter(),
										TypeUtils.castToTimestamp(mobjectV).toString()));
						break;
					case "NUMERIC":
						fg = (int) objectV != (int) mobjectV;
						break;
					default:
						fg = !((String) objectV).equals((String) mobjectV);
						break;
					}
					if (fg)
					{
						if (i > 0)
							buffer.append("\n");
						i++;
						buffer.append(Symbols.L_MiddleBracket + Symbols.L_Bracket + "字段:" + fname + Symbols.R_Bracket
								+ Symbols.SPACE + objectV + Symbols.SQUARE + mobjectV + Symbols.R_MiddleBracket);
					}
				}
			}
		}
		if (!ObjectHelperUtils.isEmpty(buffer))
		{
			slogObject.setDETAILS(buffer.toString());
			writeLog(slogObject);
		}

	}

	@Async
	public void writeLOGINLog(UserInformation userInfo, LOGActionEnum actionName)
	{
		String userName = userInfo.getUserName();
		SLOGObject slogObject = new SLOGObject();
		slogObject.setCuser(userInfo.getUserName());
		slogObject.setCtime((Timestamp) SystemTime.getTimestamp());
		slogObject.setIP(OnlineUsers.getOnUser(userName).getIp());
		slogObject.setOBJID(userInfo.getUserID());
		slogObject.setOBJTN("SUSER");
		slogObject.setOBJNAME("系统用户");
		slogObject.setType(SYSTEM_ACTION);
		slogObject.setACTION(getActionName(actionName));
		slogObject.setDETAILS("用户" + Symbols.L_MiddleBracket + slogObject.getCuser() + Symbols.R_MiddleBracket
				+ Symbols.SPACE + " 登录系统成功");
		writeLog(slogObject);

	}

	@Async
	public void writeLOGOUTLog(UserInformation userInfo, LOGActionEnum actionName)
	{
		String userName = userInfo.getUserName();
		SLOGObject slogObject = new SLOGObject();
		slogObject.setCuser(userInfo.getUserName());
		slogObject.setCtime((Timestamp) SystemTime.getTimestamp());
		slogObject.setIP(OnlineUsers.getOnUser(userName).getIp());
		slogObject.setOBJID(userInfo.getUserID());
		slogObject.setOBJTN("SUSER");
		slogObject.setOBJNAME("系统用户");
		slogObject.setType(SYSTEM_ACTION);
		slogObject.setACTION(getActionName(actionName));
		slogObject.setDETAILS("用户" + Symbols.L_MiddleBracket + slogObject.getCuser() + Symbols.R_MiddleBracket
				+ Symbols.SPACE + " 退出系统成功");
		writeLog(slogObject);

	}

	private SLOGObject setDefaultLogMsg(ApplicationContext context, USCObject object, LOGActionEnum actionName)
	{
		String userName = context.getCurrUserName();
		String objID = object.getID();
		String objTN = object.getTableName();
		String objCAP = object.getObjCaption();
		String IP = context.getUserClientIP();
		SLOGObject slogObject = new SLOGObject();
		slogObject.setOBJID(objID);
		slogObject.setOBJTN(objTN);
		slogObject.setOBJNAME(objCAP);
		slogObject.setCuser(userName);
		slogObject.setIP(IP);
		slogObject.setACTION(getActionName(actionName));
		return slogObject;
	}

	@Async
	public void writeLog(SLOGObject slogObject)
	{
		if (slogObject != null)
		{
			Map<String, Object> log = new HashMap<String, Object>();
			log.put("OBJID", slogObject.getOBJID());
			log.put("OBJTN", slogObject.getOBJTN());
			log.put("OBJNAME", slogObject.getOBJNAME());
			log.put("type", slogObject.getType());
			log.put("ACTION", slogObject.getACTION());
			log.put("IP", slogObject.getIP());
			log.put("DETAILS", slogObject.getDETAILS());
			log.put("cuser", slogObject.getCuser());
			log.put("ctime", slogObject.getCtime());
			writeLog(slogObject.getCuser(), "SYSLOG", log);
		}
	}

	@Async
	public void writeLog(UserInformation userInformation, boolean isSlog, String OBJNAME, String OBJTN, String OBJID,
			String DETAILS, LOGActionEnum actionName)
	{
		if (userInformation != null)
		{
			Map<String, Object> log = new HashMap<String, Object>();
			log.put("OBJID", OBJID);
			log.put("OBJTN", OBJTN);
			log.put("OBJNAME", OBJNAME);
			log.put("type", isSlog ? SYSTEM_ACTION : DATA_ACTION);
			log.put("ACTION", getActionName(actionName));
			log.put("IP", OnlineUsers.getOnUser(userInformation.getUserName()).getIp());
			log.put("DETAILS", DETAILS);
			log.put("cuser", userInformation.getUserName());
			log.put("ctime", OnlineUsers.getOnUser(userInformation.getUserName()).getLoginTime());
			writeLog(userInformation.getUserName(), "SYSLOG", log);
		}
	}

	@Async
	public void writeFileLog(UserInformation userInformation, String OBJNAME, String OBJTN, String OBJID,
			String DETAILS, LOGActionEnum actionName)
	{
		if (userInformation != null)
		{
			Map<String, Object> log = new HashMap<String, Object>();
			log.put("OBJID", OBJID);
			log.put("OBJTN", OBJTN);
			log.put("OBJNAME", OBJNAME);
			log.put("type", "F");
			log.put("ACTION", getActionName(actionName));
			log.put("IP", OnlineUsers.getOnUser(userInformation.getUserName()).getIp());
			log.put("DETAILS", DETAILS);
			log.put("cuser", userInformation.getUserName());
			log.put("ctime", SystemTime.getTimestamp());
			writeLog(userInformation.getUserName(), "SYSLOG", log);
		}
	}

	@Async
	public void writeFileLog(ApplicationContext context, LOGActionEnum actionName, FileObject... fileObjects)
	{
		if (context != null && fileObjects != null)
		{
			Timestamp stime = SystemTime.getTimestamp();
			for (FileObject fileObject : fileObjects)
			{
				if (fileObject == null)
					continue;
				Map<String, Object> log = new HashMap<String, Object>();
				log.put("OBJID", fileObject.getID());
				log.put("OBJTN", fileObject.getTableName());
				log.put("OBJNAME", fileObject.getObjCaption());
				log.put("type", "F");
				log.put("ACTION", getActionName(actionName));
				log.put("IP", context.getUserClientIP());
				String acString = null;
				if (actionName == LOGActionEnum.UL)
				{
					acString = "上传文件:";
				} else if (actionName == LOGActionEnum.DL)
				{
					acString = "下载文件:";
				} else
				{
					acString = "浏览文件:";
				}
				log.put("DETAILS",
						"用户" + Symbols.L_MiddleBracket + context.getUserName() + Symbols.R_MiddleBracket + Symbols.SPACE
								+ acString
								+ (fileObject.getFieldValue("name") == null ? fileObject.getFieldValueToString("fname")
										: fileObject.getFieldValueToString("name")));

				log.put("cuser", context.getUserName());
				log.put("ctime", stime);
				writeLog(context.getUserName(), "SYSLOG", log);
			}

		}
	}

	public void writeLog(UserInformation userInformation, boolean isSlog, String OBJNAME, String OBJTN, String OBJID,
			String DETAILS, String actionName)
	{
		if (userInformation != null)
		{
			Map<String, Object> log = new HashMap<String, Object>();
			log.put("OBJID", OBJID);
			log.put("OBJTN", OBJTN);
			log.put("OBJNAME", OBJNAME);
			log.put("type", isSlog ? SYSTEM_ACTION : DATA_ACTION);
			log.put("ACTION", actionName);
			log.put("IP", OnlineUsers.getOnUser(userInformation.getUserName()).getIp());
			log.put("DETAILS", DETAILS);
			log.put("cuser", userInformation.getUserName());
			log.put("ctime", OnlineUsers.getOnUser(userInformation.getUserName()).getLoginTime());
			writeLog(userInformation.getUserName(), "SYSLOG", log);
		}
	}

	private void writeLog(String userName, String itemNo, Map<String, Object> log)
	{
		try
		{
			ItemUtiilities.newDataItem(userName, "SYSLOG", log, true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String getActionName(LOGActionEnum actionName)
	{
		switch (actionName)
		{
		case NEW:
			return "NEW";
		case MODIFY:
			return "MODIFY";
		case DELETE:
			return "DELETE";
		case UL:
			return "UPLOAD";
		case DL:
			return "DOWNLOAD";
		case BROWSE:
			return "BROWSE";
		case LOGIN:
			return "LOGIN";
		case LOGOUT:
			return "LOGOUT";

		default:
			return "OTHER";
		}
	}
}
