package com.usc.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * ClassName: Dto <br/>
 * date: 2019年7月31日 下午4:25:18 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public interface Dto extends Map<String, Object>, Serializable
{

	/**
	 * @describe 以Object类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Object getObject(String key);

	/**
	 * @describe 以String类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key);

	/**
	 * @describe 以Integer类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key);

	/**
	 * @describe 以Long类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Long getLong(String key);

	/**
	 * @describe 以Double类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Double getDouble(String key);

	/**
	 * @describe 以Float类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Float getFloat(String key);

	/**
	 * @describe 以BigDecimal类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public BigDecimal getBigDecimal(String key);

	/**
	 * @describe 以Boolean类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key);

	/**
	 * @describe 以Date类型返回键值：yyyy-MM-dd
	 *
	 * @param key
	 * @return
	 */
	public Date getDate(String key);

	/**
	 * @describe 以Date类型返回键值：yyyy-MM-dd hh:mm:ss
	 *
	 * @param key
	 * @return
	 */
	public Date getDateTime(String key);

	/**
	 * @describe 以Timestamp(时间戳)类型返回键值
	 *
	 * @param key
	 * @return
	 */
	public Timestamp getTimestamp(String key);

	/**
	 * @describe 给Dto压入第一个默认List对象，为了方便存取(省去根据Key来存取和类型转换的过程)
	 *
	 * @param list
	 */
	public void setDefaultAList(List<?> list);

	/**
	 * @describe 给Dto压入第二个默认List对象，为了方便存取(省去根据Key来存取和类型转换的过程)
	 *
	 * @param list
	 */
	public void setDefaultBList(List<?> list);

	/**
	 * @describe 获取第一个默认List对象，为了方便存取(省去根据Key来存取和类型转换的过程)
	 *
	 * @return
	 */
	public <T> List<T> getDefaultAList();

	/**
	 * @describe 获取第二个默认List对象，为了方便存取(省去根据Key来存取和类型转换的过程)
	 *
	 * @return
	 */
	public <T> List<T> getDefaultBList();

	/**
	 * @describe 给Dto压入一个默认的Json格式字符串
	 *
	 * @param jsonString
	 */
	public void setDefaultJson(String jsonString);

	/**
	 * @describe 获取默认的Json格式字符串
	 *
	 * @return
	 */
	public String getDefaultJson();

	/**
	 * @describe 将此Dto对象转换为XML格式字符串，默认为节点元素值风格
	 *
	 * @param pStyle
	 * @return
	 */
	public String toXml();

	/**
	 * @describe 将此Dto对象转换为XML格式字符串，默认为节点元素值风格
	 *
	 * @param pStyle
	 * @return
	 */
	public String toXml(String Style);

	/**
	 * @describe 将此Dto对象转换为Json格式字符串
	 *
	 * @return
	 */
	public String toJson();

	/**
	 * @describe 将此Dto对象转换为Json格式字符串
	 *
	 * @return
	 */
	public String toJson(String format);

	public String getKeySetToString();

}
