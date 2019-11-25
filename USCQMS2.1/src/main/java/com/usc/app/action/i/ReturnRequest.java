package com.usc.app.action.i;

import java.util.List;
import java.util.Map;

import com.usc.app.util.tran.StandardResultTranslate;

public interface ReturnRequest
{
	boolean flagTrue = Boolean.TRUE;
	boolean flagFalse = Boolean.FALSE;

	default Map successfulOperation()
	{
		return StandardResultTranslate.successfulOperation();
	}

	default Map failedOperation()
	{
		return StandardResultTranslate.failedOperation();
	}

	default Map failedOperation(String mes)
	{
		return StandardResultTranslate.failedOperation(mes);
	}

	default Map createSuccessful(List ls)
	{
		Map map = StandardResultTranslate.getResult("Action_Create", ls);
		map.put("sign", "N");
		return map;
	}

	default Map modifySuccessful(List ls)
	{
		Map map = StandardResultTranslate.getResult("Action_Update", ls);
		map.put("sign", "M");
		return map;
	}

	default Map modifyFailed()
	{
		Map map = StandardResultTranslate.getResult(false, "Action_Update");
		map.put("sign", "M");
		return map;
	}

	default Map deleteSuccessful(List ls)
	{
		Map map = StandardResultTranslate.getResult("Action_Delete", ls);
		map.put("sign", "D");
		return map;
	}

	default Map addSuccessful(List ls)
	{
		Map map = StandardResultTranslate.getResult("Action_Add", ls);
		map.put("sign", "A");
		return map;
	}

}
