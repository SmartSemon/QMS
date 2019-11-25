package com.usc.app.activiti.resource;

import java.util.List;

import com.usc.app.activiti.service.ModelService;
import com.usc.dto.Dto;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(value = "/actModel", produces = "application/json;charset=UTF-8")
public class ModelResource {

    @Autowired
    ModelService modelService;

    /**
     * 获取JSON展示设计器页面
     * @param modelId 模型id
     * @return 节点
     */
    @GetMapping(value = "/model/{modelId}/json")
    @CrossOrigin
    public ObjectNode getEditorJson(@PathVariable("modelId") String modelId) {
        return modelService.getEditorJson(modelId);
    }

    /**
     * 获取stencilset.json文件展示设计器菜单等
     * @return json菜单
     */
    @GetMapping("/editor/stencilset")
    @CrossOrigin
    public String getStencilset() {
        return modelService.getStencilset();
    }


    /**
     * 查询所有模型
     * @return 模型集合
     */
    @GetMapping("/modelist")
    public List<Model> modelList() {
        return modelService.getModelList();
    }

    /**
     * 新建模型
     * @param name 名称
     * @param key key
     * @param description 描述
     * @return 状态
     */
    @PostMapping("/create/{name}/{key}/{description}")
    @CrossOrigin
    public Dto create(@PathVariable("name") String name, @PathVariable("key") String key,
                      @PathVariable("description") String description) {
        return modelService.create(name, key, description);
    }

    /**
     * 模型保存设计图
     * @param modelId 模型id
     * @param name 名称
     * @param description 描述
     * @param json_xml xml
     * @param svg_xml 图片
     * @return 状态
     */
    @PutMapping("/model/{modelId}/save")
    public String saveModel(@PathVariable("modelId") String modelId, @RequestParam("name") String name,
                            @RequestParam("description") String description, @RequestParam("json_xml") String json_xml,
                            @RequestParam("svg_xml") String svg_xml) {
        return modelService.saveModel(modelId, name, description, json_xml, svg_xml);
    }

    /**
     * 模型部署
     * @param modelId 模型id
     * @return 状态
     */
    @PostMapping("/deploy/{modelId}")
    public Dto deploy(@PathVariable("modelId") String modelId) {
        return modelService.deploy(modelId);
    }

    /**
     * 模型删除
     * @param modelId 模型id
     * @return 状态
     */
    @PostMapping("deleteById/{modelId}")
    public Dto delete(@PathVariable("modelId") String modelId) {
        return modelService.delete(modelId);
    }

    /**
     * 获取所有用户作为代理人
     * @return 代理人集合
     */
    @GetMapping("getAllRole")
    public List<Dto> getAllRole() {
        return modelService.getAllRole();
    }

    /**
     * 获取角色用户
     * @param roleId 角色id
     * @return 角色用户
     */
    @GetMapping("getRoleUserList/{roleId}")
    public List<Dto> getRoleUserList(@PathVariable("roleId") String roleId) {
        return modelService.getRoleUserList(roleId);
    }

}
