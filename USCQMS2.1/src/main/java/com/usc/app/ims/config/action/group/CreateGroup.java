package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.api.impl.USCServerBeanProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/11/18 16:52
 * @Description: 创建群组
 **/
public class CreateGroup extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        Map<String, Object> result = new HashMap<>();
        //获取群组表单数据
        if (context.getExtendInfo("userId") != null && context.getExtendInfo("groupName") != null) {
            Map<String, Object> group = new HashMap<>();
            group.put("create_by", context.getExtendInfo("userId"));
            group.put("photo", context.getExtendInfo("photo"));
            group.put("group_name", context.getExtendInfo("groupName"));
            group.put("remark", context.getExtendInfo("remarks"));
            //保存
            try {
                context.setUserName(context.getUserName());
                context.setItemNo("CHAT_GROUP");
                context.setInitData(group);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //新建群后返回群信息（获取到群信息后添加自己到群中）
            USCObject newGroup = context.createObj("CHAT_GROUP");
            ApplicationContext applicationContext = (ApplicationContext) USCServerBeanProvider.getContext("CHAT_GROUP_USER",
                    context.getUserName());
            Map<String, Object> group_user = new HashMap<>();
            group_user.put("userId", context.getExtendInfo("userId"));
            group_user.put("groupId", newGroup.getID());
            try {
                applicationContext.setUserName(context.getUserName());
                applicationContext.setItemNo("CHAT_GROUP_USER");
                applicationContext.setInitData(group_user);
                applicationContext.createObj("CHAT_GROUP_USER");
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("code", 0);
            result.put("msg", "创建成功！");
        } else {
            result.put("code", 1);
            result.put("msg", "创建失败");
        }
        return result;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
