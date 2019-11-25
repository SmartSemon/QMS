package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

/**
 * @Author: lwp
 * @DATE: 2019/11/20 10:34
 * @Description: 群主删除群成员
 **/
public class DeleteGroupUser extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        try {
            //删除群组关联的用户数据
            USCObject group_user = USCObjectQueryHelper.getObjectByID("CHAT_GROUP_USER", (String) context.getExtendInfo("id"));
            context.setCurrObj(group_user);
            return group_user.delete(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
