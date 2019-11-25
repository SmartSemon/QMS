package com.usc.app.activiti.service.impl;

import com.usc.app.activiti.service.ModelService;
import com.usc.app.bs.service.impl.BaseService;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.server.DBConnecter;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngineConfiguration;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("modelService")
public class ModelServiceImpl extends BaseService implements ModelDataJsonConstants, ModelService {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    //    @Autowired
//    private SysRoleService sysRoleService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public ObjectNode getEditorJson(String modelId) {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService.getModelEditorSource(model.getId()), StandardCharsets.UTF_8));
                modelNode.put("model", editorJsonNode);

            } catch (Exception e) {
                e.printStackTrace();
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

    @Override
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            assert stencilsetStream != null;
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }

    @Override
    public List<Model> getModelList() {
        return repositoryService.createModelQuery().orderByCreateTime().desc().list();
    }

    @Override
    public Dto create(String name, String key, String description) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        } catch (DataAccessException e) {
            e.printStackTrace();
            SQLException sqle = (SQLException) e.getCause();
            return new MapDto("ErrorCode", sqle.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new MapDto("Message", e.getMessage());
        }
        return new MapDto("result", "success");
    }

    @Override
    public String saveModel(String modelId, String name, String description, String json_xml, String svg_xml) {
        try {
            Model model = repositoryService.getModel(modelId);
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, name);
            modelJson.put(MODEL_DESCRIPTION, description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes(StandardCharsets.UTF_8));
            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes(StandardCharsets.UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception e) {
            throw new ActivitiException("Error saving model", e);
        }
        return null;
    }

    @Override
    public Dto deploy(String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes;
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String processName = modelData.getName() + ".bpmn20.xml";
            repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8)).deploy();
            return new MapDto("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new MapDto("Message", e.getMessage());
        }
    }

    @Override
    public Dto delete(String modelId) {
        try {
            repositoryService.deleteModel(modelId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            SQLException sqle = (SQLException) e.getCause();
            return new MapDto("ErrorCode", sqle.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new MapDto("Message", e.getMessage());
        }
        return new MapDto("result", "success");
    }

    @Override
    public List<Dto> getAllRole() {
        List<Dto> roleList = new ArrayList<>();
        String sql = "SELECT id,name FROM srole WHERE del = 0 AND type = 1";
        try {
            List<Map<String, Object>> list = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sql);
            for (Map<String, Object> role : list) {
                Dto dto = new MapDto();
                role.forEach((key, value) -> {
                    dto.put(key, value);
                });
                roleList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<Dto> list = sysRoleService.selectList("FIND_LIST", new MapDto());
//        for (Dto value : list) {
//            Dto dto = new MapDto();
//            dto.put("id", value.get("id"));
//            dto.put("name", value.get("roleName"));
//            roleList.add(dto);
//        }x
        return roleList;
    }

    @Override
    public List<Dto> getRoleUserList(String roleId) {
        List<Dto> userList = new ArrayList<>();
        String sql = "SELECT " +
                "suser.id,suser.name " +
                "FROM " +
                "suser " +
                "LEFT JOIN sr_srole_suser_obj ON sr_srole_suser_obj.itembid = suser.id " +
                "LEFT JOIN srole ON sr_srole_suser_obj.itemaid = srole.id " +
                "WHERE " +
                "srole.id = ?";
        try {
            List<Map<String, Object>> list = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sql, new Object[]{roleId});
            for (Map<String, Object> user : list) {
                Dto dto = new MapDto();
                user.forEach((key, value) -> {
                    dto.put(key, value);
                });
                userList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Dto params = new MapDto();
//        params.put("id", roleId);
//        List<Dto> list = sysRoleService.selectList("FIND_USERS_BYROLEID", params);
//        for (Dto value : list) {
//            Dto dto = new MapDto();
//            dto.put("id", value.get("id"));
//            dto.put("name", value.get("user_name"));
//            userList.add(dto);
//        }
        return userList;
    }
}
