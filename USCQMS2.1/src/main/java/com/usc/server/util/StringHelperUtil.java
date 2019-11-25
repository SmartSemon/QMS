package com.usc.server.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author PuTianxiong 创建时间 ：2018年7月10日
 *
 *         StringHelperUtil.java 应用范围 ： String 字符串辅助类
 */
public class StringHelperUtil
{
	/**
	 * 该方法主要使用正则表达式来判断字符串中是否包含字母
	 *
	 * @author Tianxiong.Pu 2018年6月16日
	 * @param string 待检验的原始字符串
	 * @return 返回是否包含
	 */

	public static boolean judgeContainsStr(String string)
	{
		if (string == null || "".equals(string))
		{
			return false;
		}
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(string);
		return m.matches();
	}

	/**
	 * 该方法主要使用正则表达式来判断一个字符是否都为数字
	 *
	 * @author Tianxiong.Pu 2018年6月16日
	 * @param string 待检验的原始字符串
	 * @return 返回是否包含
	 */
	public static boolean isDigit(String strNum)
	{
		if (strNum == null || "".equals(strNum))
		{
			return false;
		}
		return strNum.matches("[0-9]{1,}");
	}

	/**
	 * 该方法主要使用正则表达式来判断一个字符串是否包含数字
	 *
	 * @author Tianxiong.Pu 2018年6月16日
	 * @param string 待检验的原始字符串
	 * @return 返回是否包含
	 */
	public static boolean isDigit2(String strNum)
	{
		if (strNum == null || "".equals(strNum))
		{
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/**
	 * 该方法主要使用正则表达式来判断一个字符串是否包含数字
	 *
	 * @author Tianxiong.Pu 2018年6月16日
	 * @param string 待检验的原始字符串
	 * @return 返回是否包含
	 */
	public boolean HasDigit(String content)
	{
		if (content == null || "".equals(content))
		{
			return false;
		}
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
		{
			flag = true;
		}
		return flag;
	}

	/**
	 * @param string 待计算乘积字符串
	 * @return 计算结果
	 */
	public static String mathProduct(String string)
	{
		if (string == null || "".equals(string))
		{
			return string;
		}
		Double m = 1.00;
		String xxxxx = string.replace("*", "x");
		String[] xxs = xxxxx.split("x");
		Double[] xxx = new Double[xxs.length];
		if (xxs != null)
		{
			for (int i = 0; i < xxs.length; i++)
			{
				xxx[i] = Double.valueOf(xxs[i]);
			}

			for (int j = 0; j < xxx.length; j++)
			{
				m = m * xxx[j];
			}
		}

		return String.valueOf(m);
	}

	private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则

	public static String removeChinese(String string)
	{
		// 去除中文
		if (string == null || "".equals(string))
		{
			return "";
		}
		Pattern pat = Pattern.compile(REGEX_CHINESE);
		Matcher mat = pat.matcher(string);
		return mat.replaceAll("");
	}

	// 字符串数组按照ASCII排序
	private static final Log _log = LogFactory.getLog(StringHelperUtil.class);

	/**
	 * 对字符串数组进行排序
	 *
	 * @param keys
	 * @return
	 */
	public static String[] getUrlParam(String[] keys)
	{
		if (keys == null)
		{
			return null;
		}
		for (int i = 0; i < keys.length - 1; i++)
		{
			for (int j = 0; j < keys.length - i - 1; j++)
			{
				String pre = keys[j];
				String next = keys[j + 1];
				if (isMoreThan(pre, next))
				{
					String temp = pre;
					keys[j] = next;
					keys[j + 1] = temp;
				}
			}
		}
		return keys;
	}

	/**
	 * 比较两个字符串的大小，按字母的ASCII码比较
	 *
	 * @param pre
	 * @param next
	 * @return
	 */
	private static boolean isMoreThan(String pre, String next)
	{
		if (null == pre || null == next || "".equals(pre) || "".equals(next))
		{
//        _log.error("字符串比较数据不能为空！");
			return false;
		}

		char[] c_pre = pre.toCharArray();
		char[] c_next = next.toCharArray();

		int minSize = Math.min(c_pre.length, c_next.length);

		for (int i = 0; i < minSize; i++)
		{
			if ((int) c_pre[i] > (int) c_next[i])
			{
				return true;
			} else if ((int) c_pre[i] < (int) c_next[i])
			{
				return false;
			}
		}
		if (c_pre.length > c_next.length)
		{
			return true;
		}

		return false;
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString 原始字符串
	 * @param length      指定截取长度
	 * @param size        指定List列表大小
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length, int size)
	{
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++)
		{
			String childStr = inputString.substring(index * length, (index + 1) * length);
			list.add(childStr);
		}
		return list;
	}

	/**
	 * 子字符串modelStr在字符串str中第count次出现时的下标
	 *
	 * @return
	 */
	public static int getFromIndex(String str, String modelStr, Integer count)
	{
		// 对子字符串进行匹配
		if (modelStr == null || str == null)
		{
			return -1;
		}
		Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
		int index = 0;
		// matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列
		while (slashMatcher.find())
		{
			index++;
			// 当modelStr字符第count次出现的位置
			if (index == count)
			{
				break;
			}
		}
		// matcher.start();返回以前匹配的初始索引。
		return slashMatcher.start();
	}

	/**
	 * 子字符串排序
	 *
	 * @return
	 */
	public static String sort(String str)
	{
		// 利用toCharArray可将字符串转换为char型的数组
		char[] s1 = str.toCharArray();
		for (int i = 0; i < s1.length; i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (s1[i] < s1[j])
				{
					char temp = s1[i];
					s1[i] = s1[j];
					s1[j] = temp;
				}
			}
		}
		// 再次将字符数组转换为字符串，也可以直接利用String.valueOf(s1)转换
		String st = new String(s1);
		return st;
	}

	/**
	 * @param str 待提取数据字符串
	 * @return 返回字符串中的中文数字和英文字母
	 */
	public static String getC1E(String str)
	{
//		    String str = "……^1dsf  の  adS   DFASFSADF阿德斯防守对方asdfsadf37《？：？@%#￥%#￥%@#$%#@$%^><?1234";
		String regEx = "[a-zA-Z0-9\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find())
		{
			sb.append(m.group());
		}
		return sb.toString();
	}

	/**
	 * @param string 字符串
	 * @param str    被包含字符串
	 * @return 被包含字符串的游标集合
	 */
	public static List<Integer> getIndexs(String string, String str)
	{
		if (string == null || "".equals(string) || str.equals("") || str == null)
		{
			return null;
		}
		if (!string.contains(str))
		{
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		int a = string.indexOf(str);
		int b = string.lastIndexOf(str);
		if (string.length() - 1 == str.lastIndexOf(str))
		{
			list.add(Integer.valueOf(string.length() - 1));
		}
		for (int i = a; i <= b; i++)
		{
			if (str.equals(string.substring(i, i + 1)) && b != string.length() + 1)
			{
				list.add(Integer.valueOf(i));
			}

		}
		return list;
	}

	/**
	 * @param string 字符串
	 * @param str    被包含字符串
	 * @return 被包含字符串的游标数组
	 */
	public static int[] getIndexs2(String string, String str)
	{
		if (string == null || "".equals(string) || str.equals("") || str == null)
		{
			return null;
		}
		if (!string.contains(str))
		{
			return null;
		}
		int[] idxs = new int[string.length()];
		int a = string.indexOf(str);
		int b = string.lastIndexOf(str);
		if (a == b)
		{
			idxs = new int[]
			{ a };
			return idxs;
		}
		int j = 0;
		for (int i = a; i <= b; i++)
		{
			if (str.equals(string.substring(i, i + 1)) && b != string.length() + 1)
			{
				idxs[j] = i;
				j++;
			}
		}
		int[] indexs = new int[j + 1];
		System.arraycopy(idxs, 0, indexs, 0, j);
		return indexs;
	}

	/**
	 * 判断字符串中是否包含中文
	 *
	 * @param str 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str)
	{
		if (str == null || "".equals(str))
		{
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find())
		{
			return true;
		}
		return false;
	}

	public static int[] StringToInt(String[] arrs)
	{
		if (arrs == null)
		{
			return null;
		}
		int[] ints = new int[arrs.length];

		for (int i = 0; i < arrs.length; i++)
		{

			ints[i] = Integer.parseInt(arrs[i]);

		}

		return ints;

	}

	public static float[] StringToFloat(String[] arrs)
	{
		if (arrs == null)
		{
			return null;
		}
		float[] floats = new float[arrs.length];

		for (int i = 0; i < arrs.length; i++)
		{

			floats[i] = Float.parseFloat(arrs[i]);

		}

		return floats;

	}

	public static String objectToString(Object object, int n)
	{
		if (object == null)
			return "";
		if ((object.getClass() == Double.class) || (object.getClass() == Float.class)
				|| (object.getClass() == BigDecimal.class))
		{
			String str = convertDoubleValue(((Number) object).doubleValue(), n);
			if (str.indexOf(".") != -1)
			{
				while (str.endsWith("0"))
				{
					str = str.substring(0, str.length() - 1);
				}
				if (str.endsWith("."))
				{
					str = str.substring(0, str.length() - 1);
				}
			}
			return str;
		}
		if (object.getClass() == Date.class || object.getClass() == Timestamp.class)
		{
			return DateFormat.getDateInstance().format(object);
		}
		return object.toString();
	}

	public static String convertDoubleValue(double doubleValue, int accuracy)
	{
		String value;
		if (doubleValue < 0.0D)
		{
			value = "-";
		} else
		{
			value = "";
		}
		doubleValue = Math.abs(doubleValue);

		double f = 0.5D;
		for (int i = 0; i < accuracy; i++)
		{
			f /= 10.0D;
		}
		doubleValue += f;

		value = value + doubleValue;

		if (accuracy > 0)
		{
			value = value + '.';
			for (int i = 0; i < accuracy; i++)
			{
				doubleValue *= 10.0D;

				value = value + doubleValue % 10L;
			}
		}
		if (value.indexOf(".") != -1)
		{
			while (value.endsWith("0"))
			{
				value = value.substring(0, value.length() - 1);
			}

			if (value.endsWith("."))
			{
				value = value.substring(0, value.length() - 1);
			}
		}

		return value;
	}

	public static String replaceAll(String str, String regex, String replacement)
	{
		if (str == null)
		{
			return null;
		}
		String st = str;
		int nStart = 0;
		for (;;)
		{
			int n = st.indexOf(regex, nStart);
			if (n < 0)
			{
				break;
			}
			st = st.substring(0, n) + replacement + st.substring(n + regex.length());
			nStart = n + replacement.length();
		}
		return st;
	}

	public static String addCodeFormat(String numberString, int length)
	{
		int numb = Integer.parseInt(numberString);
		numb++;
		return formatInteger(numb, length);
	}

	public static String addCodeFormat(int numb, int length)
	{
		numb++;
		return formatInteger(numb, length);
	}

	public static String formatString(String numberString, int length)
	{
		int numb = Integer.parseInt(numberString);
		return formatInteger(numb, length);
	}

	public static String formatInteger(int number, int length)
	{
		String STR_FORMAT = "";
		for (int i = 0; i < length; i++)
		{
			STR_FORMAT += "0";
		}

		DecimalFormat df = new DecimalFormat(STR_FORMAT);
		return df.format(number);
	}

}
