package com.usc.server.md.field;

import java.util.ArrayList;
import java.util.List;

import com.usc.util.ObjectHelperUtils;

/**
 * ClassName: FieldNameInitConst <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: System Field Container. <br/>
 * date: 2019年7月31日 下午4:37:49 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class FieldNameInitConst
{
	public static final String FIELD_ID = "id";
	public static final String FIELD_DSNO = "dsno";
	public static final String FIELD_DEL = "del";
	public static final String FIELD_OWNER = "owner";
	public static final String FIELD_CUSER = "cuser";
	public static final String FIELD_CTIME = "ctime";
	public static final String FIELD_MUSER = "muser";
	public static final String FIELD_MTIME = "mtime";
	public static final String FIELD_DUSER = "duser";
	public static final String FIELD_DTIME = "dtime";
	public static final String FIELD_MYSM = "mysm";
	public static final String FIELD_STATE = "state";

	public static final String FIELD_NODEID = "nodeid";
	public static final String FIELD_ITEMID = "itemid";

	public static final String FIELD_ITEMA = "itema";
	public static final String FIELD_ITEMAID = "itemaid";
	public static final String FIELD_ITEMB = "itemb";
	public static final String FIELD_ITEMBID = "itembid";

	public static final String FIELD_NO = "no";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_VER = "ver";
	public static final String FIELD_REMARK = "remark";

	public static final String FIELD_FNAME = "fname";
	public static final String FIELD_FLOCATION = "flocation";
	public static final String FIELD_FTYPE = "ftype";
	public static final String FIELD_FSIZE = "fsize";
	public static final String FIELD_FMTIME = "fmtime";

	static List<String> qmsSysFields = null;
	static List<String> qmsFileFields = null;

	public FieldNameInitConst()
	{
	}

	public static List<String> getQmsSysFields()
	{
		qmsSysFields = new ArrayList<String>();
		qmsSysFields.add(FIELD_ID);
		qmsSysFields.add(FIELD_DEL);
		qmsSysFields.add(FIELD_DSNO);
		qmsSysFields.add(FIELD_MYSM);
		qmsSysFields.add(FIELD_OWNER);
		qmsSysFields.add(FIELD_CUSER);
		qmsSysFields.add(FIELD_CTIME);
		qmsSysFields.add(FIELD_MUSER);
		qmsSysFields.add(FIELD_MTIME);
		qmsSysFields.add(FIELD_DUSER);
		qmsSysFields.add(FIELD_DTIME);
		qmsSysFields.add(FIELD_STATE);

		return qmsSysFields;

	}

	public static List<String> getFileFields()
	{
		qmsFileFields = new ArrayList<String>();
		qmsFileFields.add(FIELD_FLOCATION);
		qmsFileFields.add(FIELD_FMTIME);
		qmsFileFields.add(FIELD_FNAME);
		qmsFileFields.add(FIELD_FSIZE);
		qmsFileFields.add(FIELD_FTYPE);
		qmsFileFields.add(FIELD_FTYPE);

		return qmsSysFields;

	}

	public static List<String> getFileQmsSysFields()
	{
		qmsSysFields = new ArrayList<String>();
		qmsSysFields.add(FIELD_ID);
		qmsSysFields.add(FIELD_DEL);
		qmsSysFields.add(FIELD_DSNO);
		qmsSysFields.add(FIELD_MYSM);
		qmsSysFields.add(FIELD_OWNER);
		qmsSysFields.add(FIELD_CUSER);
		qmsSysFields.add(FIELD_CTIME);
		qmsSysFields.add(FIELD_MUSER);
		qmsSysFields.add(FIELD_MTIME);
		qmsSysFields.add(FIELD_DUSER);
		qmsSysFields.add(FIELD_DTIME);
		qmsSysFields.add(FIELD_STATE);
		qmsSysFields.add(FIELD_FNAME);
		qmsSysFields.add(FIELD_FLOCATION);
		qmsSysFields.add(FIELD_FTYPE);
		qmsSysFields.add(FIELD_FMTIME);
		qmsSysFields.add(FIELD_FSIZE);

		return qmsSysFields;

	}

	public static List<String> getCrlQmsSysFields()
	{
		List<String> crlQmsSysFields = new ArrayList<String>();
		crlQmsSysFields.add(FIELD_ID);
		crlQmsSysFields.add(FIELD_DEL);
		crlQmsSysFields.add(FIELD_DSNO);
		crlQmsSysFields.add(FIELD_MYSM);
		crlQmsSysFields.add(FIELD_OWNER);
		crlQmsSysFields.add(FIELD_CUSER);
		crlQmsSysFields.add(FIELD_CTIME);
		crlQmsSysFields.add(FIELD_MUSER);
		crlQmsSysFields.add(FIELD_MTIME);
		crlQmsSysFields.add(FIELD_DUSER);
		crlQmsSysFields.add(FIELD_DTIME);
		crlQmsSysFields.add(FIELD_STATE);

		crlQmsSysFields.add(FIELD_NODEID);
		crlQmsSysFields.add(FIELD_ITEMID);

		return crlQmsSysFields;

	}

	public static List<String> getRelQmsSysFields()
	{
		List<String> relQmsSysFields = new ArrayList<String>();
		relQmsSysFields.add(FIELD_ID);
		relQmsSysFields.add(FIELD_DEL);
		relQmsSysFields.add(FIELD_DSNO);
		relQmsSysFields.add(FIELD_MYSM);
		relQmsSysFields.add(FIELD_OWNER);
		relQmsSysFields.add(FIELD_CUSER);
		relQmsSysFields.add(FIELD_CTIME);
		relQmsSysFields.add(FIELD_MUSER);
		relQmsSysFields.add(FIELD_MTIME);
		relQmsSysFields.add(FIELD_DUSER);
		relQmsSysFields.add(FIELD_DTIME);
		relQmsSysFields.add(FIELD_STATE);

		relQmsSysFields.add(FIELD_ITEMA);
		relQmsSysFields.add(FIELD_ITEMAID);
		relQmsSysFields.add(FIELD_ITEMA);
		relQmsSysFields.add(FIELD_ITEMBID);

		return relQmsSysFields;

	}

	public static boolean isSystemField(String field)
	{
		List<String> sFileds = qmsSysFields == null ? getQmsSysFields() : qmsSysFields;
		return sFileds.contains(field);
	}

	public static boolean isSystemFileField(String field)
	{
		List<String> sFileds = qmsFileFields == null ? getFileFields() : qmsFileFields;
		return sFileds.contains(field);
	}

	public static boolean isEnableSystemField(String field)
	{
		if (ObjectHelperUtils.isEmpty(field))
			return false;
		List<String> sFileds = qmsSysFields == null ? getQmsSysFields() : qmsSysFields;
		return sFileds.contains(field) && (field.equals(FIELD_DSNO) || field.equals(FIELD_STATE));
	}

}
