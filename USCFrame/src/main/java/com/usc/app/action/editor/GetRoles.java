package com.usc.app.action.editor;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.server.DBConnecter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/8/14 15:05
 * @Description: 获取所有角色分类和获取此用户的已有的角色
 **/
public class GetRoles extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        //获取所有角色并整理为树形结构
        String sroleSql = "SELECT * FROM srole WHERE del = 0";
        List<Map<String, Object>> sroleList = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sroleSql);
        //转为树形结构并且把id当做key(前台需要)
        List resultList = new ArrayList<>();
        for (Map<String, Object> srole : sroleList) {
            if ("0".equals(srole.get("pid"))) {
                resultList.add(findChildren(srole,sroleList));
            }
        }
        //获取此用户已有角色
        String userId = (String) context.getExtendInfo("condition");
        String suserHasRoleSql = "SELECT srole.* FROM srole,sr_srole_suser_obj,suser " +
                "WHERE srole.id = sr_srole_suser_obj.itemaid " +
                "AND suser.id = ? AND sr_srole_suser_obj.itembid = ? " +
                "AND sr_srole_suser_obj.del = 0";
        //获取用户的已有角色的集合
        List<Map<String, Object>> userHasRolesList = new JdbcTemplate(DBConnecter.getDataSource()).
                queryForList(suserHasRoleSql, new Object[]{userId,userId});
        Dto dto = new MapDto();
        dto.put("rolesList",resultList);
        dto.put("userHasRolesList",userHasRolesList);
        return StandardResultTranslate.getResult("Action_Query",dto);
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }

    public static Object findChildren(Map<String,Object> obj,List<Map<String,Object>> list) {
        for (Map<String,Object> it : list) {
            //给角色赋key，前台需要
            it.put("key",it.get("id"));
            if(obj.get("id").equals(it.get("pid"))) {
                obj.computeIfAbsent("children", k -> new ArrayList<>());
                List childrenList = (List) obj.get("children");
                childrenList.add(findChildren(it,list));
            }
        }
        return obj;
    }

}
