package com.usc.app.action.editor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.Utils;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.type.file.IFile;
import com.usc.server.DBConnecter;
import org.aspectj.weaver.ast.And;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: lwp
 * @DATE: 2019/8/15 9:02
 * @Description: 用户分配角色
 **/
public class AssignRoles extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        String queryParam = (String) context.getExtendInfo("condition");
        Dto param = new MapDto();
        if (!Utils.isEmpty(queryParam)) {
            param.putAll(new ObjectMapper().readValue(queryParam, MapDto.class));
        }
        String userId = (String) param.get("userId");
        List<String> targetKeys = (List<String>) param.get("targetKeys");
        List<Map<String, Object>> userHasRolesList = (List<Map<String, Object>>) param.get("userHasRolesList");
        ApplicationContext applicationContext = (ApplicationContext) context.cloneContext();
        //原有的角色对象集合用userHasRolesIds只装id;方便去取两个list集合的差集
        if (userHasRolesList.size() > 0) {
            if (targetKeys.size() > 0) {
                List<String> userHasRolesIds = new ArrayList<>();
                for (Map<String, Object> roleId : userHasRolesList) {
                    userHasRolesIds.add((String) roleId.get("id"));
                }
                //userHasRolesIds克隆一个，不改变userHasRolesIds以便获取另一个差集
                List<String> tempList = (List<String>) ((ArrayList<String>) userHasRolesIds).clone();
                //用户已有角色集合与新的角色集合的差集，有差集则删除差集对应的角色
                tempList.removeAll(targetKeys);
                if (tempList.size()>0){
                    for (String id : tempList){
                        String deleteSql = "UPDATE sr_srole_suser_obj SET del = 1 WHERE itemaid = ? AND itembid = ?";
                        new JdbcTemplate(DBConnecter.getDataSource()).update(deleteSql, new Object[]{id, userId});
                    }
                }
                //新的角色集合与用户已有角色集合的差集，有差集则新增差集对应的角色
                targetKeys.removeAll(userHasRolesIds);
                if (targetKeys.size()>0){
                    Map<String, Object> newData = new HashMap<>();
                    newData.put("itema", "SROLE");
                    newData.put("itemb", "SUSER");
                    newData.put("itembid", userId);
                    for (String id : targetKeys) {
                        newData.put("itemaid", id);
                        applicationContext.setInitData(newData);
                        applicationContext.createObj("SR_SRPLE_OBJ");
                    }
                }
            } else {
                //targetKeys <= 0直接删除所有角色
                for (Map<String, Object> roleId : userHasRolesList) {
                    //逻辑删除
                    String deleteSql = "UPDATE sr_srole_suser_obj SET del = 1 WHERE itemaid = ? AND itembid = ?";
                    new JdbcTemplate(DBConnecter.getDataSource()).update(deleteSql, new Object[]{roleId.get("id"), userId});
                }
            }
        } else {
            //用户空角色直接新增
            Map<String, Object> newData = new HashMap<>();
            newData.put("itema", "SROLE");
            newData.put("itemb", "SUSER");
            newData.put("itembid", userId);
            for (String roles : targetKeys) {
                newData.put("itemaid", roles);
                applicationContext.setInitData(newData);
                applicationContext.createObj("SR_SRPLE_OBJ");
            }
        }

        return null;
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }
}
