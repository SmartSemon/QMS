package com.usc.app.action.editor;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.DBConnecter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/9/5 17:15
 * @Description:
 **/
public class OnSelector extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        String codestandardSql = "SELECT * FROM sys_codestandard WHERE del = 0";
        String condition = (String) context.getExtendInfo("condition")!=null && !((String) context.getExtendInfo("condition")).equals("") ?
                " AND " +(String) context.getExtendInfo("condition"):"";
        List<Map<String, Object>> OnList = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(codestandardSql+condition);
        return StandardResultTranslate.getQueryResult(true,"Action_Query",OnList);
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }

}
