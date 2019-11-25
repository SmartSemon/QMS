package com.usc.obj.api.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

/**
 * 
 * <p>
 * Title: BeanCreator
 * </p>
 * 
 * <p>
 * Description: 动态Bean构造器
 * </p>
 * 
 * @author PuTianXiong
 * 
 * @date 2019年5月7日
 * 
 */
public class BeanCreator
{

	public Object object = null;

	public BeanMap beanMap = null;

	public BeanCreator()
	{
		super();
	}

	public BeanCreator(Map keyMap)
	{
		this.object = createDtoBean(keyMap);
		this.beanMap = BeanMap.create(this.object);
	}

	/**
	 * set方法
	 * 
	 * @param key   键
	 * @param value 值
	 */
	public void setValue(String key, Object value)
	{
		beanMap.put(key, value);
	}

	/**
	 * get方法
	 * 
	 * @param key 键
	 * @return 值
	 */
	public Object getValue(String key)
	{
		return beanMap.get(key);
	}

	/**
	 * 获取实例对象
	 * 
	 * @return
	 */
	public Object getObject()
	{
		return this.object;
	}

	private Object createDtoBean(Map keyMap)
	{
		BeanGenerator generator = new BeanGenerator();
		Set keySet = keyMap.keySet();
		for (Iterator i = keySet.iterator(); i.hasNext();)
		{
			String key = (String) i.next();
			generator.addProperty(key, (Class) keyMap.get(key));
		}
		return generator.create();
	}
}