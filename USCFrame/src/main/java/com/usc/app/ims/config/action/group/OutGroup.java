package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

/**
 * @Author: lwp
 * @DATE: 2019/11/20 11:40
 * @Description: 成员退群
 **/
public class OutGroup extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        if (context.getExtendInfo("groupId") != null && context.getExtendInfo("userId") != null) {
            try {
                String condition = "del = 0 AND " + "groupId = " + "'" + context.getExtendInfo("groupId") + "'"
                        + " AND userId = " + "'" + context.getExtendInfo("userId") + "'";
                USCObject[] group_user = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP_USER", condition);

                context.setCurrObj(group_user[0]);
                return group_user[0].delete(context);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
