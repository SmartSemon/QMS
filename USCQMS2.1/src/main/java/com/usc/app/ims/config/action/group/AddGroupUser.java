package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/11/19 13:43
 * @Description:
 **/
public class AddGroupUser extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        String groupId = (String) context.getExtendInfo("groupId");
        List<Map<String, Object>> data = (List<Map<String, Object>>) context.getExtendInfo("data");
        Map<String, Object> chat_group_user;
        for (int i = 0; i < data.size(); i++) {
            chat_group_user = new HashMap<>();
            chat_group_user.put("userId", data.get(i).get("id"));
            chat_group_user.put("groupId", groupId);
            //判断是否已添加
            String condition = "del = 0 AND " + "userId = " + "'" + data.get(i).get("id") + "'" + " AND groupId = " + "'" + groupId + "'";
            USCObject[] isUser = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP_USER", condition);
            if (isUser == null) {
                try {
                    context.setUserName(context.getUserName());
                    context.setItemNo("CHAT_GROUP_USER");
                    context.setInitData(chat_group_user);
                    context.createObj("CHAT_GROUP_USER");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
