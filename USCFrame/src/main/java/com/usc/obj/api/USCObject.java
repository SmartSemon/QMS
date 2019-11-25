package com.usc.obj.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemRelationPage;

public abstract interface USCObject extends USCObjectAction, Serializable
{
	public abstract Object getFieldValue(String paramString);

	public abstract Object getFieldValueBykey(String paramString);

	public abstract String getFieldValueToString(String field);

	public abstract Integer getFieldValueToInteger(String field);

	public abstract Boolean getFieldValueToBoolen(String field);

	public abstract Date getFieldValueToDate(String field);

	public abstract Float getFieldValueToFloat(String field);

	public abstract Double getFieldValueToDouble(String field);

	public abstract BigDecimal getFieldValueToBigDecimal(String field);

	public abstract boolean containsField(String paramString);

	public abstract void setFieldValue(String paramString, Object paramObject);

	public abstract void setFieldValues(Map<String, Object> paramHashMap);

	public abstract String getID();

	public abstract String getFieldNoByFieldName(String paramString);

	public abstract String getObjIcon();

	public abstract String getObjCaption();

	public abstract String getItemNo();

	public abstract Map<String, Object> getFieldValues();

	public abstract String getTableName();

	public abstract boolean save(InvokeContext context);

	public abstract ItemPage getItemPage(String paramString);

	public abstract ItemGrid getItemGrid(String paramString);

	public abstract ItemRelationPage getItemRelationPage(String paramString);

	public abstract ItemMenu getMenu(String paramString);
}
