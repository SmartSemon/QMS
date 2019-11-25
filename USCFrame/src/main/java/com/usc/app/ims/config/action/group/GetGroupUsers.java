package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/11/19 13:36
 * @Description: 群主/非群主获取群成员
 **/
public class GetGroupUsers extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        if (context.getExtendInfo("groupId") != null) {
            //根据groupid获取chat_group_user关系表的关系数据
            String groupUserCondition = "del = 0 AND " + "groupId = " + "'" + context.getExtendInfo("groupId") + "'";
            USCObject[] groupUserList = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP_USER", groupUserCondition);
            Map<String, Object> u;
            for (int i = 0; i < groupUserList.length; i++) {
                u = new HashMap<>();
                USCObject user = USCObjectQueryHelper.getObjectByID("SUSER", (String) groupUserList[i].getFieldValue("userId"));
                u.put("id", groupUserList[i].getID());
                u.put("userId", user.getID());
                u.put("name", user.getFieldValue("SNAME"));
                u.put("status", "online".equals(user.getFieldValue("status")) ? "在线" : "离线");
                list.add(u);
            }
            result.put("code", 0);
            result.put("data", list);
        }
        return result;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
