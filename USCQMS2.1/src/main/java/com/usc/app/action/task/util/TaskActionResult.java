package com.usc.app.action.task.util;

import java.util.List;

import com.usc.app.action.cache.PromptTranslationTools;
import com.usc.app.action.utils.ActionMessage;

public class TaskActionResult
{
	public static ActionMessage getResult(List list, String info, String sign)
	{
		ActionMessage message = new ActionMessage();
		message.setDataList(list);
		message.setFlag(true);
		message.setSign(sign);
		message.setInfo(PromptTranslationTools.translationMessage(info,"task"));
		return message;
	}

}
