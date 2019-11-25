package com.usc.obj.api;

import java.util.Map;

public interface USCObjConstructFactory
{
	public abstract USCObject createByType(String paramString1, String paramString2);

	public abstract USCObject create(String paramString1, String paramString2);

	public abstract USCObject create(String paramString, Map paramHashMap);
}
