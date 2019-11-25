package com.usc.dto.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * ClassName: ObjectConversionHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: 类型转换器. <br/>
 * date: 2019年7月31日 下午4:25:55 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public class ObjectConversionHelper
{

	/**
	 * @describe 类型转换核心方法
	 *
	 * @param obj    需要转化的值
	 * @param type   需要转换的目标类型
	 * @param format 需要转换的时间格式
	 * @return
	 */
	public static Object convert(Object obj, String type, String format) throws RuntimeException
	{
		Locale locale = Locale.SIMPLIFIED_CHINESE;// 创建“简体中文”的Locale
//		Locale locale = new Locale("zh", "CN");//创建“简体中文”的Locale
		// c 要转换的值为空，直接返回null
		if (obj == null)
		{
			return null;
		}
		// c 如果要转换的值类型和转换的目标类型相同，则直接返回obj
		if (obj.getClass().getName().equals(type) || obj.getClass().getName().equals("java.lang." + type))
		{
			return obj;
		}
		// c 转换为Object类型，直接返回obj
		if ("Object".equals(type) || "java.lang.Object".equals(type))
		{
			return obj;
		}

		// c 如果要转换的目标值为String实例
		if (obj instanceof String)
		{
			String fromType = "String";
			String str = (String) obj;
			// c 如果obj长度为0，则返回null
			if (obj.toString().length() == 0)
			{
				return null;
			}
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				return obj;
			}
			/*
			 * setGroupingUsed() 是否使用分组方式显示 setNinimumFractionDigits()  设置数值的小数部分允许的最小位数    
			 *     * setMaximumFractionDigits()  设置数值的小数部分允许的最大位数       
			 * * setMaximumIntegerDigits()  设置数值的整数部分允许的最大位数       
			 * * setMinimumIntegerDigits()  设置数值的整数部分允许的最大位数
			 */
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				try
				{
					NumberFormat nf = NumberFormat.getNumberInstance(locale);
					nf.setGroupingUsed(false);// c 是否使用分组方式显示
					nf.setMaximumFractionDigits(0);// c 设置数值的小数部分允许的最大位数
					Number num = nf.parse(str);
					return new Integer(num.intValue());
				} catch (ParseException e)
				{
					throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ", e);
				}
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				try
				{
					NumberFormat nf = NumberFormat.getNumberInstance(locale);
					nf.setGroupingUsed(false);// c 是否使用分组方式显示
					nf.setMaximumFractionDigits(0);// c 设置数值的小数部分允许的最大位数
					Number num = nf.parse(str);
					return new Long(num.longValue());
				} catch (ParseException e)
				{
					throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ", e);
				}
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				try
				{
					NumberFormat nf = NumberFormat.getNumberInstance(locale);
					Number num = nf.parse(str);
					return new Double(num.doubleValue());
				} catch (ParseException e)
				{
					throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ", e);
				}
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				try
				{
					NumberFormat nf = NumberFormat.getNumberInstance(locale);
					Number num = nf.parse(str);
					return new Float(num.floatValue());
				} catch (ParseException e)
				{
					throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ", e);
				}
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				try
				{
					BigDecimal retBig = new BigDecimal(str);
					int iscale = str.indexOf(".");
					int keylen = str.length();
					if (iscale > -1)
					{
						iscale = keylen - (iscale + 1);
						return retBig.setScale(iscale, 5);
					} else
					{
						return retBig.setScale(0, 5);
					}
				} catch (Exception e)
				{
					throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ", e);
				}
			}
			// c 转换为boolean
			if ("Boolean".equals(type) || "java.lang.Boolean".equals(type))
			{
				if (obj.toString().equalsIgnoreCase("TRUE"))
				{
					return new Boolean(true);
				}
				return new Boolean(false);
			}
		}

		// c 如果要转换的目标值为BigDecimal实例
		if (obj instanceof BigDecimal)
		{
			String fromType = "BigDecimal";
			BigDecimal bigd = (BigDecimal) obj;
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				return nf.format(bigd.doubleValue());
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				return obj;
			}
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				return new Integer(bigd.intValue());
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				return new Long(bigd.longValue());
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				return new Float(bigd.floatValue());
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				return new Double(bigd.doubleValue());
			}
			throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ");
		}

		// c 如果要转换的目标值为Double实例
		if (obj instanceof Double)
		{
			String fromType = "Double";
			Double dbl = (Double) obj;
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				return nf.format(dbl.doubleValue());
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				return new BigDecimal(dbl.doubleValue());
			}
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				return new Integer(dbl.intValue());
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				return new Long(dbl.longValue());
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				return new Float(dbl.floatValue());
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				return obj;
			}
			throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ");
		}

		// c 如果要转换的目标值为Float实例
		if (obj instanceof Float)
		{
			String fromType = "Float";
			Float flt = (Float) obj;
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				return nf.format(flt.doubleValue());
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				return new BigDecimal(flt.doubleValue());
			}
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				return new Integer(flt.intValue());
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				return new Long(flt.longValue());
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				return obj;
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				return new Double(flt.doubleValue());
			}
			throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ");
		}

		// c 如果要转换的目标值为Long实例
		if (obj instanceof Long)
		{
			String fromType = "Long";
			Long lon = (Long) obj;
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				return nf.format(lon.doubleValue());
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				return new BigDecimal(lon.doubleValue());
			}
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				return new Integer(lon.intValue());
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				return obj;
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				return new Float(lon.floatValue());
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				return new Double(lon.doubleValue());
			}
			throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ");
		}

		// c 如果要转换的目标值为Integer实例
		if (obj instanceof Integer)
		{
			String fromType = "Integer";
			Integer intg = (Integer) obj;
			// c 转换为String
			if ("String".equals(type) || "java.lang.String".equals(type))
			{
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				return nf.format(intg.doubleValue());
			}
			// c 转换为BigDecimal
			if ("BigDecimal".equals(type) || "java.math.BigDecimal".equals(type))
			{
				String str = intg.toString();
				BigDecimal retBig = new BigDecimal(intg.doubleValue());
				int iscale = str.indexOf(".");
				int keylen = str.length();
				if (iscale > -1)
				{
					iscale = keylen - (iscale + 1);
					return retBig.setScale(iscale, 5);
				} else
				{
					return retBig.setScale(0, 5);
				}
			}
			// c 转换为Integer
			if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			{
				return obj;
			}
			// c 转换为Long
			if ("Long".equals(type) || "java.lang.Long".equals(type))
			{
				return new Long(intg.longValue());
			}
			// c 转换为Float
			if ("Float".equals(type) || "java.lang.Float".equals(type))
			{
				return new Float(intg.floatValue());
			}
			// c 转换为Double
			if ("Double".equals(type) || "java.lang.Double".equals(type))
			{
				return new Double(intg.doubleValue());
			}
			throw new RuntimeException("Could not convert " + fromType + " to " + type + ": ");
		}

		return null;
	}
}
