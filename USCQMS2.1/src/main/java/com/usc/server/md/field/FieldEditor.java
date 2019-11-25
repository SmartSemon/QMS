package com.usc.server.md.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;
import com.usc.server.jdbc.DBUtil;
import com.usc.server.md.ItemField;
import com.usc.util.ObjectHelperUtils;

/**
 * ClassName: FieldEditor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: Field Editor Definition. <br/>
 * date: 2019年7月31日 下午4:35:21 <br/>
 *
 * @author SEMON
 * @version
 * @since JDK 1.8
 */
public final class FieldEditor
{
	public static final String TEXTBOX = "TextBox";
	public static final String TEXTAREA = "TextArea";
	public static final String RICHTEXT = "RichText";
	public static final String DATETIME = "DateTime";
	public static final String VALUELIST = "ValueList";
	public static final String DBVALUELIST = "DBValueList";
	public static final String MAPVALUELIST = "MapValueList";
	public static final String RADIOEDITOR = "RadioEditor";
	public static final String CHECKEDITOR = "CheckEditor";
	public static final String SLIDER = "Slider";
	public static final String ITEMSELECTOR = "ItemSelector";
	public static final String USERSELECTOR = "UserSelector";
	public static final String USERSSELECTOR = "UsersSelector";
	public static final String DEPTSELECTOR = "DeptSelector";
	public static final String PASSWORD = "Password";
	public static final String FILESELECTOR = "FileSelector";
	public static final String ONSELECTOR = "OnSelector";
	public static final String RATE = "Rate";

	private String editor;
	private String editParams;

	private String dateFormatter;
	private List radioValues;
	private List bathValues;
	private List valueList;
	private List dbValueList;
	private Map<String, Object> mapValueList;
	private Map<String, Object[]> itemSelectorValues;

	public FieldEditor()
	{
	}

	public FieldEditor(ItemField field)
	{
		if (field != null)
		{
			this.editor = field.getEditor();
			this.editParams = field.getEditParams();
		}
	}

	public String getDateFormatter()
	{

		if (this.editor.equals(FieldEditor.DATETIME))
		{
			if (dateFormatter != null)
			{
				return dateFormatter;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				JSONObject object = JSONObject.parseObject(editParams);
				if (object.containsKey("values"))
				{
					object = object.getJSONObject("values");
				}
				this.dateFormatter = object.getString("format").replace("Y", "y").replace("D", "d").trim();
			}
		}
		return this.dateFormatter;
	}

	public List getRadioValues()
	{
		if (this.editor.equals(FieldEditor.RADIOEDITOR))
		{
			if (this.radioValues != null)
			{
				return this.radioValues;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				JSONObject object = JSONObject.parseObject(editParams);
				this.radioValues = JSONArray.parseArray(object.getString("values"));
				return this.radioValues;
			}
		}
		return null;
	}

	public List getBathValues()
	{
		if (this.editor.equals(FieldEditor.CHECKEDITOR))
		{
			if (this.bathValues != null)
			{
				return this.bathValues;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				JSONObject object = JSONObject.parseObject(editParams);
				this.bathValues = JSONArray.parseArray(object.getString("values"));
				return this.bathValues;
			}
		}
		return null;
	}

	public List getValueList()
	{
		if (this.editor.equals(FieldEditor.VALUELIST))
		{
			if (this.valueList != null)
			{
				return this.valueList;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				JSONObject object = JSONObject.parseObject(editParams);
				this.valueList = JSONArray.parseArray(object.getString("values"));
				return this.valueList;
			}
		}
		return null;
	}

	public List getDBValueList()
	{

		if (this.editor.equals(FieldEditor.DBVALUELIST))
		{
			if (this.dbValueList != null)
			{
				return this.dbValueList;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				JSONObject object = JSONObject.parseObject(editParams);
				String sql = object.getString("sql");
				this.dbValueList = DBUtil.queryForList(sql);
				return this.dbValueList;
			}
		}
		return null;
	}

	public Map<String, Object> getMapValueList()
	{

		if (this.editor.equals(FieldEditor.MAPVALUELIST))
		{
			if (this.mapValueList != null)
			{
				return mapValueList;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject param = JSONObject.parseObject(editParams);
				JSONArray array = JSONArray.parseArray(param.getString("values"));
				array.forEach(object -> {
					JSONObject obj = JSONObject.parseObject(object.toString());
					String key = obj.getString("key").trim();
					String value = obj.getString("name").trim();
					map.put(key, value);
					map.put(value, key);

				});
				return map;
			}
		}
		return null;
	}

	public Map<String, Object[]> getItemSelectorValues()
	{
		if (this.editor.equals(FieldEditor.ITEMSELECTOR))
		{
			if (this.itemSelectorValues != null)
			{
				return this.itemSelectorValues;
			}
			if (!ObjectHelperUtils.isEmpty(editParams))
			{
				Map<String, Object[]> map = new HashMap<String, Object[]>();
				JSONObject param = JSONObject.parseObject(editParams);
				String itemNo = param.getString("itemNo");
				String sqlCondition = param.getString("sql").trim();
				String sql = "del=0";
				if (!"".equals(sqlCondition))
				{
					sql += " AND " + sqlCondition;
				}
				USCObject[] objects = USCObjectQueryHelper.getObjectsByCondition(itemNo, sql);
				map.put("objects", objects);
				List<Map> mapFields = JSONArray.parseArray(param.getString("mapFields"), Map.class);
				map.put("mapFields", mapFields.toArray());
				return map;
			}
		}
		return null;
	}

	public USCObject[] getItemSelectorObjects()
	{
		Map<String, Object[]> map = getItemSelectorValues();
		if (!ObjectHelperUtils.isEmpty(map))
		{
			Object[] objects = map.get("objects");
			if (objects != null)
			{
				return (USCObject[]) objects;
			}
		}
		return null;
	}

	public Map[] getItemSelectorMapFields()
	{
		Map<String, Object[]> map = getItemSelectorValues();
		if (!ObjectHelperUtils.isEmpty(map))
		{
			Object[] objects = map.get("mapFields");
			if (objects != null)
			{
				return (Map[]) objects;
			}
		}
		return null;
	}
}
