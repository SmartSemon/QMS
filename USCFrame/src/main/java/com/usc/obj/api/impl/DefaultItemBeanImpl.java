package com.usc.obj.api.impl;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.usc.app.execption.ThrowRemoteException;
import com.usc.obj.api.bean.ItemBean;
import com.usc.server.DBConnecter;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.jdbc.ItemUtiilities;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.util.SystemTime;
import com.usc.server.util.uuid.USCUUID;

public class DefaultItemBeanImpl implements ItemBean
{

	@Override
	public Map<String, Object> newDataItem(String paramString1, String paramString2, String paramString3,
			Map<String, Object> paramMap) throws RemoteException
	{
		Connection connection = null;
		try
		{
			connection = DBConnecter.getConnection();
		} catch (Exception e)
		{
			throw ThrowRemoteException.throwExceptionDetails(e);
		}
		return null;
	}

	@Override
	public Map<String, Object> newDataItem(String paramString1, String paramString2, String paramString3,
			String paramString4, Map<String, Object> paramMap) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map newItem(String paramString, Map<String, Object> paramMap) throws RemoteException
	{
		paramMap.putIfAbsent("id", USCUUID.UUID());
		paramMap.putIfAbsent("del", false);
		paramMap.putIfAbsent("mysm", "N");
		paramMap.putIfAbsent("ctime", SystemTime.getTimestamp());
		paramMap.putIfAbsent("state", "C");
		return null;
	}

	@Override
	public Map<String, Object> newItem(String paramString1, String paramString2, Map<String, Object> paramMap)
			throws RemoteException
	{
		paramMap.putIfAbsent("del", false);
		paramMap.putIfAbsent("mysm", "N");
		paramMap.putIfAbsent("state", "C");
		try
		{
			return ItemUtiilities.newDataItem(paramString1, paramString2, (Map<String, Object>) paramMap);
		} catch (Exception e)
		{
			throw ThrowRemoteException.throwExceptionDetails(e);
		}
	}

	@Override
	public boolean saveDataItem(String userName, String itemNo, String strID, Map paramMap) throws RemoteException
	{
		Map<String, Object> sysFMap = new HashMap<String, Object>();
		sysFMap.put(FieldNameInitConst.FIELD_MUSER, userName);
		sysFMap.put(FieldNameInitConst.FIELD_MYSM, "M");
		sysFMap.put(FieldNameInitConst.FIELD_MTIME, new Timestamp(System.currentTimeMillis()));
		paramMap.putAll(sysFMap);
		try
		{
			return DBUtil.saveDBRecord(itemNo, strID, paramMap);
		} catch (Exception e)
		{
			throw ThrowRemoteException.throwExceptionDetails(e);
		}
//		return false;
	}

	@Override
	public boolean saveItem(String paramString1, String paramString2, Map paramMap) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveData(String paramString1, String paramString2, Map paramMap) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map getItemInfo(String itemNo, String strID) throws RemoteException
	{
		try
		{
			return DBUtil.getObjValuesByID(itemNo, strID);
		} catch (Exception e)
		{
			throw ThrowRemoteException.throwExceptionDetails(e);
		}
//		return null;
	}

	@Override
	public boolean deleteItem(String paramString1, String strID, String userName) throws RemoteException
	{
		boolean b = false;
		Object[] objects = new Object[]
		{ userName, new Timestamp(System.currentTimeMillis()), strID };
		try
		{
			b = DBUtil.deleteRecord(paramString1, objects);
		} catch (Exception e)
		{
//			LoggerFactory.logError("eorr:deleteItemException", e);
			throw ThrowRemoteException.throwExceptionDetails(e);
		}
		return b;
	}

	@Override
	public boolean deleteDataItem(String paramString1, String paramString2, String paramString3) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector getItemInfoByCondition(String paramString1, String paramString2) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getEqualItemOnArchiveArea(String paramString1, String paramString2) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getEqualItemByCondition(String paramString1, String paramString2, String paramString3,
			String paramString4) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer upgradeToMaxVer(String paramString1, String paramString2) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getRepeatItemsInDesignNo(String paramString1, String paramString2, String paramString3)
			throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getRepeatItemInArchiveArea(String paramString1, String paramString2, String paramString3)
			throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int checkOutObject(String paramString1, String paramString2, String paramString3, String paramString4)
			throws RemoteException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean cancelCheckOut(String paramString1, String paramString2, String paramString3) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map checkOutObjectFromCollaborate(String paramString1, String paramString2, String paramString3,
			String paramString4) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getEqualItemOnCollaborationArea(String paramString1, String paramString2, String paramString3)
			throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestItemID(String paramString) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getRequestItemByID(String itemNo, String itemID) throws Exception
	{
		return DBUtil.getObjValuesByID(itemNo, itemID);
	}

	@Override
	public byte[] getMetaData() throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getObjectsByCondition(String itemNo, String condition) throws RemoteException
	{
		// TODO Auto-generated method stub
		return DBUtil.getSQLResultByCondition(itemNo, condition);
	}

}
