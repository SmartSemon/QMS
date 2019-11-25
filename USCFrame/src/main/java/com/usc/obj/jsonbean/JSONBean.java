package com.usc.obj.jsonbean;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public abstract class JSONBean implements ResourceModelComponent
{
	protected String userName = null;
	protected String itemNo = null;
	protected String itemGridNo = null;
	protected String itemPropertyPageNo = null;
	protected String itemRelationPageNo = null;

	protected HashMap<String, Object> data = null;
	protected List<HashMap<String, Object>> hData = null;

	protected String itemA = null;
	protected HashMap<String, Object> itemAData = null;
	protected String relationShipNo = null;
	protected List<HashMap<String, Object>> rData = null;

	protected Integer page;
	protected String queryWord;
	protected String queryViewNo;

	private String classNodeItemNo;
	private String classNodeItemPropertyNo;
	private HashMap<String, Object> classNodeData;
	private String classItemNo;
	private String pid;
	private String condition;

	private String hAuthArray;
	private String hHalfAuthArray;
	private String nAuthArray;

	private String otherParam;

	private String mNo;

	private String upOrDownLoadType;

	private String viewNo;
	private HashMap<String, Object> classViewNodeData;

	public void remove(Object object)
	{

	}
}
