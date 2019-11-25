package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.USCServerBeanProvider;
import com.usc.obj.util.USCObjectQueryHelper;

/**
 * @Author: lwp
 * @DATE: 2019/11/19 11:10
 * @Description: 群主删除群组
 **/
public class DeleteGroup extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        if (context.getExtendInfo("groupId") != null) {
            try {
                //删除群组关联的用户数据
//                ApplicationContext applicationContext = (ApplicationContext) USCServerBeanProvider.getContext("CHAT_GROUP_USER",
//                        context.getUserName());
//                applicationContext.setUserName(context.getUserName());
//                applicationContext.setItemNo("CHAT_GROUP_USER");
//                String condition = "del = 0 AND " + "groupId = " + "'" + context.getExtendInfo("groupId") + "'";
//                USCObject[] groupUser = USCObjectQueryHelper.getObjectsByCondition("CHAT_GROUP_USER", condition);
//                for (int i = 0; i < groupUser.length; i++) {
//                    applicationContext.setCurrObj(groupUser[i]);
//                    groupUser[i].delete(applicationContext);
//                }
                //删除群组
                USCObject group = USCObjectQueryHelper.getObjectByID("CHAT_GROUP", (String) context.getExtendInfo("groupId"));
                context.setCurrObj(group);
                group.delete(context);
                return true;
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
