package com.usc.app.action.utils;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.usc.obj.api.USCObject;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ActionMessage implements ResultMessage
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean flag;
	private String info;
	private String sign;
	private String errCode;
	private List<Object> dataList;

	public ActionMessage()
	{
	}

	/**
	 * @param flag     事件操作成功与否
	 * @param sign     事件特殊标记
	 * @param info     事件处理结束提示语
	 * @param errCode  若事件执行异常或错误返回对应的错误状态码
	 * @param dataList 事件执行完成后需要返回列表数据
	 */
	public ActionMessage(boolean flag, String sign, String info, String errCode, Object dataList)
	{
		this.flag = flag;
		this.sign = sign;
		this.info = info;
		setDataList(dataList);
	}

	public List<Object> getDataList()
	{
		return dataList;
	}

	public void setDataList(Object objs)
	{
		if (objs != null)
		{
			this.dataList = new JSONArray();
			if (objs.getClass().isArray())
			{
				Object[] objects = (Object[]) objs;
				if (objects[0] instanceof USCObject)
				{
					for (Object object : objects)
					{
						dataList.add(((USCObject) object).getFieldValues());
					}
					return;
				}
			}
			if (objs instanceof Collection)
			{
				List objects = (List) objs;
				if (objects.get(0) instanceof USCObject)
				{

					for (Object object : objects)
					{
						dataList.add(((USCObject) object).getFieldValues());
					}
					return;
				}
			}

			this.dataList = List.of(objs);
		}
	}
}
