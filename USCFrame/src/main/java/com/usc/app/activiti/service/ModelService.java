package com.usc.app.activiti.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.usc.dto.Dto;
import org.activiti.engine.repository.Model;

import java.util.List;

public interface ModelService {

    ObjectNode getEditorJson(String modelId);

    String getStencilset();

    List<Model> getModelList();

    Dto create(String name, String key, String description);

    String saveModel(String modelId, String name, String description, String json_xml, String svg_xml);

    Dto deploy(String modelId);

    Dto delete(String modelId);

    List<Dto> getAllRole();

    List<Dto> getRoleUserList(String roleId);
}
