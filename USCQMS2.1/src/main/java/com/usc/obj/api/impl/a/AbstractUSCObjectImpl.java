package com.usc.obj.api.impl.a;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.format.datetime.DateFormatter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.usc.app.action.mate.MateFactory;
import com.usc.app.execption.ThrowRemoteException;
import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.RelationShip;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.USCObjectAction;
import com.usc.obj.util.USCObjExpHelper;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemGrid;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemMenu;
import com.usc.server.md.ItemPage;
import com.usc.server.md.ItemRelationPage;
import com.usc.server.md.field.FieldAdapter;
import com.usc.server.md.field.FieldEditor;
import com.usc.server.md.field.FieldMappingConverter;
import com.usc.server.md.field.FieldNameInitConst;
import com.usc.server.md.field.FieldUtils;
import com.usc.server.util.LoggerFactory;
import com.usc.util.ObjectHelperUtils;

/**
 *
 * <p>
 * Title: USCObjectImpl
 * </p>
 *
 * <p>
 * Description: USCObject实现类
 * </p>
 *
 * @author PuTianXiong
 *
 * @date 2019年5月7日
 *
 */
public abstract class AbstractUSCObjectImpl implements USCObject, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4551319315703124886L;
	protected String itemObjType = null;
	protected Map<String, Object> dataMap = null;
	protected Map<String, Object> uMap = null;
	protected RelationShip relationShip = null;
	protected USCObjectAction objectAction = null;

	private ItemInfo itemInfo = null;

	public AbstractUSCObjectImpl(String objType)
	{
		init(objType, null);
	}

	public AbstractUSCObjectImpl(String objType, HashMap<String, Object> map)
	{
		init(objType, map);
	}

	public AbstractUSCObjectImpl(String objType, JSONObject map)
	{
		init(objType, map);
	}

	private void init(String objType, Map<String, Object> map)
	{
		this.itemObjType = objType;
		this.itemInfo = MateFactory.getItemInfo(itemObjType);
		this.dataMap = new HashMap<String, Object>();
		if (ObjectHelperUtils.isNotEmpty(map))
		{
			map.forEach((field, v) -> {
				ItemField itemField = getItemField(field);
				if (itemField != null)
				{
					try
					{
						dataMap.put(field, FieldUtils.getObjectByType(v, itemField));
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	public Object getFieldValueBykey(String field)
	{
		if (dataMap == null)
		{
			return null;
		}
		if (dataMap.containsKey(field))
		{
			return dataMap.get(field);
		}
		return null;
	}

	public Object getFieldValue(String field)
	{
		if (dataMap == null)
		{
			return null;
		}
		if (!containsField(field))
		{
			return null;
		}
		ItemField itemField = getItemField(field);
		return getDBValue(itemField);
	}

	private Object getDBValue(ItemField field)
	{
		String strFieldType = field.getFType();
		String editor = field.getEditor();
		Object object = dataMap.get(field.getNo());
		if (object == null)
		{
			return null;
		}
		if (FieldAdapter.isInt(strFieldType))
		{
			if (FieldEditor.MAPVALUELIST.equals(editor))
			{
				object = FieldMappingConverter.getValue2Key(field, Integer.valueOf((Integer) object));
			}
			return object;
		} else if (FieldAdapter.isVarchar(strFieldType))
		{
			if (FieldEditor.MAPVALUELIST.equals(editor))
			{
				object = FieldMappingConverter.getValue2Key(field, (String) object);
			}
			return object;
		} else if (FieldAdapter.isFloat(strFieldType))
		{
			return TypeUtils.castToFloat(object);
		} else if (FieldAdapter.isDouble(strFieldType))
		{
			return TypeUtils.castToDouble(object);
		} else if (FieldAdapter.isNumeric(strFieldType))
		{
			return TypeUtils.castToBigDecimal(object);
		} else if (FieldAdapter.isBoolean(strFieldType))
		{
			if (object instanceof Boolean)
			{
				return (boolean) object;
			} else if (object instanceof Integer)
			{
				return BooleanUtils.toBoolean((Integer) object);
			} else if (object instanceof String)
			{
				return BooleanUtils.toBoolean(String.valueOf(object));
			}
			return false;
		} else if (FieldAdapter.isDateTime(strFieldType))
		{
			if (object instanceof String)
			{
				try
				{
					object = new DateFormatter(field.getFieldEditor().getDateFormatter()).parse(object.toString(),
							Locale.getDefault());
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
			} else if (object instanceof Date)
			{
				return (Date) object;
			}
			return object;
		} else
		{
			return object;
		}

	}

	public String getFieldValueToString(String field)
	{
		Object object = getFieldValue(field);
		if (!ObjectHelperUtils.isEmpty(object))
		{
			try
			{
				return String.valueOf(FieldUtils.getObjectByType(object, getItemField(field))).trim();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	public Integer getFieldValueToInteger(String field)
	{
		Object value = getFieldValue(field);

		return TypeUtils.castToInt(value);
	}

	public Boolean getFieldValueToBoolen(String field)
	{
		Object value = getFieldValue(field);
		return TypeUtils.castToBoolean(value);
	}

	public Date getFieldValueToDate(String field)
	{
		Object value = getFieldValue(field);
		return TypeUtils.castToDate(value);
	}

	public Float getFieldValueToFloat(String field)
	{
		Object value = getFieldValue(field);
		return TypeUtils.castToFloat(value);
	}

	public Double getFieldValueToDouble(String field)
	{
		Object value = getFieldValue(field);
		return TypeUtils.castToDouble(value);
	}

	public BigDecimal getFieldValueToBigDecimal(String field)
	{
		Object value = getFieldValue(field);
		return TypeUtils.castToBigDecimal(value);
	}

	public boolean containsField(String paramString)
	{
		return this.itemInfo.containsField(paramString);
	}

	public boolean delete(InvokeContext context) throws Exception
	{
		if (isDeleteAble(context))
		{
			return false;
		}
		if (!isDeleted(context))
		{
			USCObjectAction objectAction = getUSCObjectAction(context);
			if (objectAction == null)
			{
				return false;
			}
			objectAction.delete(context);
		}

		return Boolean.TRUE;
	}

	public USCObjectAction getUSCObjectAction(InvokeContext context)
	{
		if (this.objectAction != null)
		{
			this.objectAction.setCurrObj(this);
		} else
		{
			this.objectAction = context.getActionObjType();
			objectAction.setCurrObj(this);
		}
		return objectAction;
	}

	public String getID()
	{
		Object id = getFieldValue(FieldNameInitConst.FIELD_ID) != null ? getFieldValue(FieldNameInitConst.FIELD_ID)
				: getFieldValue("DID");
		if (id == null)
		{
			LoggerFactory.logError("对象不完整", new NullPointerException("_Object incompleteness"));
			return null;
		}
		return String.valueOf(id);
	}

	public String getFieldNoByFieldName(String paramString)
	{
		List<ItemField> fields = this.itemInfo.getItemFieldList();
		if (fields != null)
		{
			for (ItemField itemField : fields)
			{
				if (paramString.equals(itemField.getFieldName()))
				{
					return itemField.getNo();
				}
			}
		}
		return null;
	}

	public String getObjIcon()
	{
		return null;
	}

	public String getObjCaption()
	{
		return this.itemInfo.getName();
	}

	public String getItemNo()
	{
		return this.itemObjType;
	}

	public Map<String, Object> getFieldValues()
	{
		return this.dataMap;
	}

	public String getTableName()
	{
		return this.itemInfo.getTableName();
	}

	public void setFieldValue(String field, Object paramObject)
	{
		if (field == null)
		{
			return;
		}
		if (FieldNameInitConst.isSystemField(field) && !FieldNameInitConst.isEnableSystemField(field))
			return;
		if (uMap == null)
		{
			uMap = new HashMap<String, Object>();
		}
		if (containsField(field))
		{
			ItemField itemField = this.getItemField(field);
			if (ObjectHelperUtils.isEmpty(paramObject) && this.getFieldValue(field) != null)
			{
				uMap.put(field, null);
			} else
			{

				if (!String.valueOf(paramObject).equals(this.getFieldValueToString(field)))
				{
					FieldAdapter adapter = new FieldAdapter();
					Map<String, Object> d = adapter.filterResult(itemField, paramObject);
					if (!ObjectHelperUtils.isEmpty(d))
					{
						uMap.putAll(d);
					}
				}

			}
		} else
		{
			ThrowRemoteException.throwException(new Throwable("Current Business Object Field Not Found : " + field));
		}
	}

	public void setFieldValues(Map<String, Object> uData)
	{
		if (ObjectHelperUtils.isEmpty(uData))
			return;
		uData.forEach((field, v) -> setFieldValue(field, v));
	}

	public ItemField getItemField(String field)
	{
		return this.itemInfo.getItemField(field);
	}

	public ItemPage getItemPage(String paramString)
	{
		return this.itemInfo.getItemPage(paramString);
	}

	public ItemGrid getItemGrid(String paramString)
	{
		return this.itemInfo.getItemGrid(paramString);
	}

	public ItemRelationPage getItemRelationPage(String paramString)
	{
		return this.itemInfo.getItemRelationPage(paramString);
	}

	public ItemMenu getMenu(String paramString)
	{
		return this.itemInfo.getItemMenu(paramString);
	}

	public boolean isDeleteAble(InvokeContext paramInvokeContext) throws Exception
	{
		if ("F".equals(getFieldValueToString(FieldNameInitConst.FIELD_STATE)))
		{
			return false;
		}
		return true;
	}

	public void setCurrObj(USCObject uscObject)
	{
		this.itemObjType = uscObject.getItemNo();
		this.dataMap = uscObject.getFieldValues();
	}

	public String toString()
	{
		String str = this.itemInfo.getBriefExp();
		if (ObjectHelperUtils.isEmpty(str))
		{
			int lf = this.itemInfo.getIsLife();
			return (lf == 0 || lf == 1) ? (this.getFieldValueToString("no") + "::" + this.getFieldValueToString("name"))
					: null;
		} else
		{
			return USCObjExpHelper.parseObjValueInExpression(this, str);
		}
	}
}
