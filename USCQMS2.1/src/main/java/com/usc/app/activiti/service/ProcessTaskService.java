package com.usc.app.activiti.service;

import com.usc.dto.Dto;
import java.io.IOException;
import java.util.List;

public interface ProcessTaskService {

    List<Dto> getTaskToDo(String queryParam) throws IOException;

    Dto handle(String taskId, String queryParam) throws IOException;

    Dto taskTransfer(String taskId, String queryParam) throws IOException;

    Dto reject(String taskId, String queryParam) throws IOException;

    List<Dto> getTaskDone(String queryParam) throws IOException;

    List<Dto> getMyProcess(String queryParam) throws IOException;

    Dto processRevoke(String queryParam) throws IOException;
}
