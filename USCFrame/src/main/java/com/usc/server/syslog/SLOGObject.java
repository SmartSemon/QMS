package com.usc.server.syslog;

import java.sql.Timestamp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SLOGObject
{
	String ACTION;
	String cuser;
	Timestamp ctime;
	String id;
	String IP;
	String OBJNAME;
	String OBJID;
	String OBJTN;
	String DETAILS;
	String remark;
	String type;

	public SLOGObject()
	{
	}
}
