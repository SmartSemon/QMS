package com.usc.app.action.editor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.DBConnecter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/8/19 16:24
 * @Description:
 **/
public class DBList extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        String sql = (String) context.getExtendInfo("condition");
        if (sql!=null){
            List<Map<String,Object>> list = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sql);
            return StandardResultTranslate.getQueryResult(true,"Action_Query",list);
        }else {
            return null;
        }
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }
}
