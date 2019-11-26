package com.usc.server.syslog;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SLOGObject
{
	String ACTION;
	String cuser;
	Date ctime;
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
