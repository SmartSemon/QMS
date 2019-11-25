package com.usc.server.md.field;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.usc.server.md.ItemField;
import com.usc.server.util.LoggerFactory;
import com.usc.server.util.StringHelperUtil;
import com.usc.server.util.SystemTime;

/**
 * ClassName: FieldAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: Field Type Adapter. <br/>
 * date: 2019年7月31日 下午4:34:38 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class FieldAdapter
{
	protected final static String FIELD_TYPE_INT = "INT";
	protected final static String FIELD_TYPE_VARCHAR = "VARCHAR";
	protected final static String FIELD_TYPE_FLOAT = "FLOAT";
	protected final static String FIELD_TYPE_DOUBLE = "DOUBLE";
	protected final static String FIELD_TYPE_BOOLEAN = "BOOLEAN";
	protected final static String FIELD_TYPE_DATETIME = "DATETIME";
	protected final static String FIELD_TYPE_LONGTEXT = "LONGTEXT";
	protected final static String FIELD_TYPE_BIGDECIMAL = "BigDecimal";
	protected final static String FIELD_TYPE_NUMERIC = FIELD_TYPE_BIGDECIMAL;

	protected static ItemField itemField;

	public static boolean isInt(String fieldType)
	{
		return FIELD_TYPE_INT.equalsIgnoreCase(fieldType);
	}

	public static boolean isVarchar(String fieldType)
	{
		return FIELD_TYPE_VARCHAR.equalsIgnoreCase(fieldType);
	}

	public static boolean isFloat(String fieldType)
	{
		return FIELD_TYPE_FLOAT.equalsIgnoreCase(fieldType);
	}

	public static boolean isDouble(String fieldType)
	{
		return FIELD_TYPE_DOUBLE.equalsIgnoreCase(fieldType);
	}

	public static boolean isBoolean(String fieldType)
	{
		return FIELD_TYPE_BOOLEAN.equalsIgnoreCase(fieldType);
	}

	public static boolean isDateTime(String fieldType)
	{
		return FIELD_TYPE_DATETIME.equalsIgnoreCase(fieldType);
	}

	public static boolean isNumeric(String fieldType)
	{
		return FIELD_TYPE_NUMERIC.equalsIgnoreCase(fieldType);
	}

	public static boolean isRichText(String fieldType)
	{
		return FIELD_TYPE_LONGTEXT.equalsIgnoreCase(fieldType);
	}

	public static Map<String, Object> filterResult(ItemField itemField, Object value)
	{
		Map<String, Object> objects = new HashMap<String, Object>();
		if (value == null || "".equals(value.toString().trim()))
		{
			return null;
		}
		setItemField(itemField);
		if (itemField != null)
		{
			String fieldType = itemField.getFType().toUpperCase();
			switch (fieldType)
			{
			case FIELD_TYPE_INT:
				objects = getFieldIntValue(value);
				break;
			case FIELD_TYPE_VARCHAR:
				objects = getFieldVarcharValue(value);
				break;
			case FIELD_TYPE_FLOAT:
				objects = getFieldFloatValue(value);
				break;
			case FIELD_TYPE_DOUBLE:
				objects = getFieldDoubleValue(value);
				break;
			case FIELD_TYPE_NUMERIC:
				objects = getFieldNumericValue(value);
				break;
			case FIELD_TYPE_BOOLEAN:
				objects = getFieldBooleanValue(value);
				break;
			case FIELD_TYPE_DATETIME:
				objects = getFieldDateTimeValue(value);
				break;
			case FIELD_TYPE_LONGTEXT:
				objects = getFieldRichTextValue(value);
				break;
			default:
				objects = null;
				break;
			}
		} else
		{
			return null;
		}
		return objects;

	}

	private static Map<String, Object> getFieldRichTextValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(itemField.getNo(), value.toString());
			return (map == null || map.isEmpty()) ? null : map;
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}

		return null;
	}

	private static boolean comparativeLength(Object value)
	{
		if (itemField.getFType().equals(FIELD_TYPE_LONGTEXT))
		{
			return true;
		}
		return itemField.getFLength() >= String.valueOf(value).length();
	}

	private static Map<String, Object> getFieldNumericValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (value instanceof BigDecimal)
			{
				map.put(getItemField().getNo(), (BigDecimal) value);
			} else
			{
				map.put(getItemField().getNo(),
						Double.valueOf(String.format("%." + getItemField().getAccuracy() + "f", value)));
			}
			return map;
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}

		return null;
	}

	private static Map<String, Object> getFieldDateTimeValue(Object value)
	{

		Map<String, Object> map = new HashMap<String, Object>();
		if (value instanceof Timestamp)
		{
			map.put(getItemField().getNo(), (Timestamp) value);
		} else if (value instanceof Date)
		{
			map.put(getItemField().getNo(), new Timestamp(((Date) value).getTime()));
		} else if (value instanceof String)
		{
			map.put(getItemField().getNo(), SystemTime.getTimestamp(
					JSONObject.parseObject(itemField.getEditParams()).getString("format"), String.valueOf(value)));
		}
		return (map == null || map.isEmpty()) ? null : map;
	}

	private static Map<String, Object> getFieldDoubleValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (value instanceof Double)
			{
				map.put(getItemField().getNo(), (double) value);
			} else
			{
				map.put(getItemField().getNo(),
						Double.valueOf(String.format("%." + getItemField().getAccuracy() + "f", value)));
			}
			return map;
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}
		return null;
	}

	private static Map<String, Object> getFieldFloatValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (value instanceof Float)
			{
				map.put(getItemField().getNo(), (float) value);
			} else
			{
				map.put(getItemField().getNo(), Float.valueOf(String.valueOf(value)));
			}
			return map;
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}
		return null;
	}

	private static Map<String, Object> getFieldVarcharValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(getItemField().getNo(), value.toString());
			return map;
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}
		return null;
	}

	private static Map<String, Object> getFieldBooleanValue(Object value)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (value instanceof Boolean)
		{
			map.put(getItemField().getNo(), ((boolean) value) ? 1 : 0);
		} else if (value instanceof String)
		{
			String v = String.valueOf(value);
			if (StringHelperUtil.isDigit(v))
			{
				map.put(getItemField().getNo(), Integer.valueOf(v) == 1 ? 1 : 0);
			} else
			{
				map.put(getItemField().getNo(), Boolean.valueOf(v) ? 1 : 0);
			}
		} else if (value instanceof Integer)
		{
			int i = Integer.valueOf(String.valueOf(value));
			if (i == 0 || i == 0)
			{
				map.put(getItemField().getNo(), i == 1 ? 1 : 0);
			} else
			{
				return null;
			}

		}
		return (map == null || map.isEmpty()) ? null : map;
	}

	private static Map<String, Object> getFieldIntValue(Object value)
	{
		if (value == null)
		{
			return null;
		}
		if (comparativeLength(value))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (value instanceof Integer)
			{
				map.put(getItemField().getNo(), value);
				return map;
			} else if (value instanceof String)
			{
				String v = String.valueOf(value);
				if (StringHelperUtil.isDigit(v))
				{
					map.put(getItemField().getNo(), Integer.valueOf(v));
					return map;
				} else if (!StringHelperUtil.isDigit(String.valueOf(value)) && ("false".equals(v) || "true".equals(v)))
				{
					map = getFieldBooleanValue(v);
				}
			}
		} else
		{
			LoggerFactory.logError("ItemField: " + itemField.getNo() + " is out of length");
		}
		return null;
	}

	/**
	 * @return the itemField
	 */
	public static ItemField getItemField()
	{
		return itemField;
	}

	/**
	 * @param itemField the itemField to set
	 */
	public static void setItemField(ItemField itemField)
	{
		FieldAdapter.itemField = itemField;
	}
}
