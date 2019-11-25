package com.usc.app.ims.config.action;

import com.usc.app.action.a.AbstractAction;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;
import com.usc.server.DBConnecter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * @Author: lwp
 * @DATE: 2019/11/4 15:30
 * @Description:
 **/
public class ChatInitUsers extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        String userName = (String) context.getExtendInfo("userName");
        //layImJSON
        Dto dto = new MapDto();
        //所有数据
        Dto data = new MapDto();
        //个人信息
        Dto mine = new MapDto();

        //0表示成功，其它表示失败
        dto.put("code", 0);
        //错误信息
        dto.put("msg", "发生错误咯！");

        //获取个人信息
        mine.put("username", userName);
        //根据userName获取当前用户信息
        String userSql = "SELECT * FROM suser WHERE del = 0 AND name = ? ";
        List<Map<String, Object>> user = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(userSql, new Object[]{userName});
        //用户id
        mine.put("id", user.get(0).get("id"));
        //在线 online：在线、hide：隐身
        mine.put("status", "online");
        //用户的个性签名
        mine.put("sign", user.get(0).get("sign"));

        //个人信息放入
        data.put("mine", mine);

        //获取部门信息
        String sdepartmentSql = "SELECT * FROM sdepartment WHERE del = 0";
        List<Map<String, Object>> sdepartmentList = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sdepartmentSql);
        List<Map<String, Object>> deptstList = sdepartmentList;

        //好友列表
        ArrayList friends = new ArrayList();
        for (int i = 0; i < deptstList.size(); i++) {
            Dto friend = new MapDto();
            friend.put("groupname", deptstList.get(i).get("name"));
            friend.put("id", deptstList.get(i).get("id"));
            //分组成员
            ArrayList lists = new ArrayList();
            String susersSql = "SELECT suser.id AS id, CONCAT(suser.name,CONCAT(CONCAT('<',spersonnel.name),'>')) AS name,suser.password,sign,status FROM " +
                    "sdepartment,spersonnel,crl_spersonnel_obj,suser,suser_relobj " +
                    "WHERE suser.del = 0 " +
                    "AND sdepartment.id = ? " +
                    "AND crl_spersonnel_obj.nodeid = ? " +
                    "AND spersonnel.id = crl_spersonnel_obj.itemid " +
                    "AND spersonnel.id = suser_relobj.itemaid " +
                    "AND suser.id = suser_relobj.itembid";
            List<Map<String, Object>> usersList = new JdbcTemplate(DBConnecter.getDataSource()).
                    queryForList(susersSql, new Object[]{deptstList.get(i).get("id"), deptstList.get(i).get("id")});
            for (Map<String, Object> users : usersList) {
                //排除自己
                if (!users.get("id").equals(user.get(0).get("id"))) {
                    Dto list = new MapDto();
                    list.put("username", users.get("name"));
                    list.put("id", users.get("id"));
                    //获取头像
                    list.put("avatar", "api/src/user/getAvatar/" + users.get("id"));
                    //获取签名
                    list.put("sign", users.get("sign"));
                    //获取状态
                    list.put("status", users.get("status"));
                    lists.add(list);
                }
            }
            friend.put("list", lists);
            friends.add(friend);
        }

        //群列表
        Set groups = new HashSet();

        //获取自己创建的群
        Map<String, Object> param = new HashMap<>();
        String groupCondition = "del = 0 AND " + "create_by = " + "'" + user.get(0).get("id") + "'";
        USCObject[] groupList = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP", groupCondition);
        if (groupList != null) {
            for (USCObject i : groupList) {
                Map<String, Object> group = new HashMap<>();
                group.put("id", i.getID());
                group.put("groupname", i.getFieldValue("group_name"));
                group.put("sign", i.getFieldValue("remark"));
                group.put("avatar", "api/src/chatGroup/getAvatar/" + i.getID());
                //获取创建者的姓名
                String userCondition = "del = 0 AND " + "id = " + "'" + i.getFieldValue("create_by") + "'";
                USCObject[] userList = USCObjectQueryHelper.getObjectsByCondition("SUSER", userCondition);
                group.put("createByUser", userList[0].getFieldValue("SNAME"));
                groups.add(group);
            }
        }
        //获取加入的群
        String groupUserCondition = "del = 0 AND " + "userId = " + "'" + user.get(0).get("id") + "'";
        USCObject[] joins = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP_USER", groupUserCondition);
        if (joins != null) {
            for (int i = 0; i < joins.length; i++) {
                //根据关联表中的群组id获取群信息
                String grop = "del = 0 AND " + "id = " + "'" + joins[i].getFieldValue("groupId") + "'";
                USCObject[] g = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP", grop);
                Map<String, Object> oup = new HashMap<>();
                oup.put("id", g[0].getID());
                oup.put("groupname", g[0].getFieldValue("group_name"));
                oup.put("sign", g[0].getFieldValue("remark"));
                oup.put("avatar", "api/src/chatGroup/getAvatar/" + g[0].getID());

                //获取创建者的姓名
                String userCondition = "del = 0 AND " + "id = " + "'" + g[0].getFieldValue("create_by") + "'";
                USCObject[] userList = USCObjectQueryHelper.getObjectsByCondition("SUSER", userCondition);
                oup.put("createByUser", userList[0].getFieldValue("SNAME"));
                groups.add(oup);
            }
        }

        //好友列表
        data.put("friend", friends);
        //群组数据放入
        data.put("group", groups);
        //放入所有数据
        dto.put("data", data);
        return dto;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
