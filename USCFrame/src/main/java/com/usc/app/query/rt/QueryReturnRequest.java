package com.usc.app.query.rt;

import java.util.List;
import java.util.Map;

import com.usc.app.action.i.ReturnRequest;
import com.usc.app.util.tran.StandardResultTranslate;

public interface QueryReturnRequest extends ReturnRequest
{
	default Map queryTrue(List dataList)
	{
		return StandardResultTranslate.getQueryResult(flagTrue, "Action_Query", dataList);
	}

	default Map queryFalse(List dataList)
	{
		return StandardResultTranslate.getQueryResult(flagFalse, "Action_Query", dataList);
	}

	default Map addTrue(List dataList)
	{
		return StandardResultTranslate.getQueryResult(flagTrue, "Action_AddRelation", dataList);
	}

	default Map addFalse(List dataList)
	{
		return StandardResultTranslate.getQueryResult(flagFalse, "Action_AddRelation", dataList);
	}
}
