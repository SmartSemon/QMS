package com.usc.app.activiti.service;

import com.usc.dto.Dto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProcessService {

	Dto getProcdefProcess();

	Dto deleteByDeploymentId(String deploymentId);

	Dto suspension(String id);

	Dto activation(String id);

	void getProcessPicture(String id, HttpServletResponse response) throws IOException;

	Dto startProcess(String queryParam) throws IOException;

	List<Dto> getRunProcess();

	void getActivityPng(String processInstanceId, HttpServletResponse response) throws IOException;

	List<Dto> getProcessReverseList(String queryParam) throws IOException;

	Dto getProcessSubList(String queryParam) throws Exception;

	Dto endProcess(String queryParam) throws IOException;

	List<Dto> getEndProcess();

	Dto deleteProcess(String queryParam) throws IOException;

}
