package com.usc.obj.api.bean;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract interface ItemBean
{
	public abstract Map<String, Object> newDataItem(String paramString1, String paramString2, String paramString3,
			Map<String, Object> paramMap) throws RemoteException;

	public abstract Map<String, Object> newDataItem(String paramString1, String paramString2, String paramString3,
			String paramString4, Map<String, Object> paramMap) throws RemoteException;

	public abstract Map<String, Object> newItem(String paramString, Map<String, Object> paramMap)
			throws RemoteException;

	public abstract Map<String, Object> newItem(String paramString1, String paramString2, Map<String, Object> paramMap)
			throws RemoteException;

	public abstract boolean saveDataItem(String paramString1, String paramString2, String paramString3,
			Map<String, Object> paramMap) throws RemoteException;

	public abstract boolean saveItem(String paramString1, String paramString2, Map paramMap) throws RemoteException;

	public abstract boolean saveData(String paramString1, String paramString2, Map paramMap) throws RemoteException;

	public abstract Map<String, Object> getItemInfo(String paramString1, String paramString2) throws RemoteException;

	public abstract boolean deleteItem(String paramString1, String paramString2, String paramString3)
			throws RemoteException;

	public abstract boolean deleteDataItem(String paramString1, String paramString2, String paramString3)
			throws RemoteException;

	public abstract Vector getItemInfoByCondition(String paramString1, String paramString2) throws RemoteException;

	public abstract List getObjectsByCondition(String paramString1, String paramString2) throws RemoteException;

//  public abstract TableData getItemInfoByCondition2(String paramString, Filters paramFilters, int paramInt1, int paramInt2)
//    throws RemoteException;

	public abstract Vector getEqualItemOnArchiveArea(String paramString1, String paramString2) throws RemoteException;

	public abstract Vector getEqualItemByCondition(String paramString1, String paramString2, String paramString3,
			String paramString4) throws RemoteException;

	public abstract Integer upgradeToMaxVer(String paramString1, String paramString2) throws RemoteException;

	public abstract Vector getRepeatItemsInDesignNo(String paramString1, String paramString2, String paramString3)
			throws RemoteException;

	public abstract Vector getRepeatItemInArchiveArea(String paramString1, String paramString2, String paramString3)
			throws RemoteException;

	public abstract int checkOutObject(String paramString1, String paramString2, String paramString3,
			String paramString4) throws RemoteException;

	public abstract boolean cancelCheckOut(String paramString1, String paramString2, String paramString3)
			throws RemoteException;

	public abstract Map checkOutObjectFromCollaborate(String paramString1, String paramString2, String paramString3,
			String paramString4) throws RemoteException;

	public abstract Vector getEqualItemOnCollaborationArea(String paramString1, String paramString2,
			String paramString3) throws RemoteException;

	public abstract String requestItemID(String paramString) throws RemoteException;

	public abstract byte[] getMetaData() throws RemoteException;

	public abstract Map<String, Object> getRequestItemByID(String itemNo, String itemID) throws Exception;
}
