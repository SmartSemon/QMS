package com.usc.app.entry.ret;

public enum RetCode
{
	SUCCESS("200"),

	FAIL("400"),

	UNAUTHORIZED("401"),

	NOT_FOUND("404"),

	INTERNAL_SERVER_ERROR("500");

	public String code;

	RetCode(String code)
	{
		this.code = code;
	}
}
