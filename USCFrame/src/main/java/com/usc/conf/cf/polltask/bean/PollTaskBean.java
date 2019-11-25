package com.usc.conf.cf.polltask.bean;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PollTaskBean implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7487572995416312141L;

	private String name;
	private String implclass;
	private String polltime;
	private Integer outtime;
	private boolean isenable;
	private boolean selfstart;
}
