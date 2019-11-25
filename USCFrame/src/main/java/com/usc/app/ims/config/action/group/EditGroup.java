package com.usc.app.ims.config.action.group;

import com.usc.app.action.a.AbstractAction;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/11/19 10:41
 * @Description: 修改群主
 **/
public class EditGroup extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        Map<String,Object> result = new HashMap<>();
        if (context.getExtendInfo("id")!=null){
            try {
                USCObject group = USCObjectQueryHelper.getObjectByID("CHAT_GROUP", (String) context.getExtendInfo("id"));
                context.setCurrObj(group);
                group.setFieldValue("group_name",context.getExtendInfo("groupName"));
                group.setFieldValue("photo",context.getExtendInfo("photo"));
                group.setFieldValue("create_by",context.getExtendInfo("userId"));
                group.setFieldValue("remark",context.getExtendInfo("remarks"));
                group.save(context);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }

    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
