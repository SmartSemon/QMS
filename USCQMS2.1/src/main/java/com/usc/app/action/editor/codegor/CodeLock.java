package com.usc.app.action.editor.codegor;

import java.util.concurrent.ConcurrentHashMap;

public abstract class CodeLock
{
	private static ConcurrentHashMap<String, Object> CodeIDS = new ConcurrentHashMap<>();

	public synchronized static boolean locked(String id)
	{
		return CodeIDS.containsKey(id);
	}

	public synchronized static void putCodeID(String id)
	{
		CodeIDS.put(id, id);
	}

	public synchronized static void removeCodeID(String id)
	{
		CodeIDS.remove(id);
	}

}
