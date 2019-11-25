package com.usc.test.mate.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usc.test.mate.action.service.ModelServer;

@RestController
@RequestMapping(value = "/model", produces = "application/json;charset=UTF-8")
public class OpenOrCloseModelResource
{
	@Autowired
	private ModelServer modelServer;

	@PostMapping("/openModel")
	public Object openModel(@RequestBody String queryParam)
	{
		return modelServer.openModel(queryParam);
	}

	@PostMapping("/closeModel")
	public Object closeModel(@RequestBody String queryParam)
	{
		return modelServer.closeModel(queryParam);
	}

	@PostMapping("/upgradeModel")
	public Object upgradeModel(@RequestBody String queryParam)
	{
		return modelServer.upgradeModel(queryParam);
	}

	@PostMapping("/cancelUpgradeModel")
	public Object cancelUpgradeModel(@RequestBody String queryParam)
	{
		return modelServer.cancelUpgradeModel(queryParam);
	}
}
