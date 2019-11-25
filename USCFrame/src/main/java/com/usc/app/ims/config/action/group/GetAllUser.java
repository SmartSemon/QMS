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
 * @DATE: 2019/11/19 13:57
 * @Description:
 **/
public class GetAllUser extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        Map<String,Object> result = new HashMap<>();
        List usersList = new ArrayList();
        String condition = "del = 0";
        USCObject[] userList = USCObjectQueryHelper.getObjectsByCondition("SUSER", condition);
        Map<String, Object> user;
        for (int i = 0; i < userList.length; i++) {
            if (!"admin".equals(userList[i].getFieldValue("id"))){
                //根据用户id获取用户与员工的关系表中的员工id
                user = new HashMap<>();
                String suser_reobj_condition = "del = 0 AND " + "itembid = " + "'" + userList[i].getID() + "'";
                USCObject suser_reobj = USCObjectQueryHelper.getObjectByCondition("SR_SUSER", suser_reobj_condition);
                //根据员工id获取员工姓名
                USCObject spersonnel = USCObjectQueryHelper.getObjectByID("SPERSONNEL", (String) suser_reobj.getFieldValue("itemaid"));
                user.put("id", userList[i].getID());
                user.put("name", userList[i].getFieldValue("SNAME") + "<" + spersonnel.getFieldValue("SUNAME") + ">");
                user.put("sign", userList[i].getFieldValue("sign"));
                user.put("remark", userList[i].getFieldValue("remark"));
                usersList.add(user);
            }
        }
        result.put("code",0);
        result.put("msg","查询成功");
        result.put("list",usersList);
        return result;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
