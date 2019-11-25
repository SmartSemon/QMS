package com.usc.app.ims.config.action;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;

/**
 * @Author: lwp
 * @DATE: 2019/11/11 10:03
 * @Description:
 **/
public class EditChatSign extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        //个性签名
        String sign = (String) context.getExtendInfo("condition");
        String userName = (String) context.getExtendInfo("userName");
        String condition = "del = 0 AND " + "name = " + "'" + userName + "'";
        USCObject[] userList = USCObjectQueryHelper.getObjectsByCondition("SUSER", condition);
        for (USCObject user : userList) {
            context.setCurrObj(user);
            user.setFieldValue("sign", sign);
            user.save(context);
        }
        return sign;
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
