package com.usc.test.mate.action.service;

public interface ModelServer
{
	boolean isModelingUser(String userName);

	Object openModel(String param);

	Object closeModel(String param);

	Object upgradeModel(String param);

	Object cancelUpgradeModel(String param);
}
