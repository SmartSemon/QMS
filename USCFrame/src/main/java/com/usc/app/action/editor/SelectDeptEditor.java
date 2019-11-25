/**
 * @Author lwp
 */
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
 * @DATE: 2019/10/09 14:41
 * @Description: 获取组织架构
 **/

public class SelectDeptEditor extends AbstractAction
{
    @Override
    public Object executeAction() throws Exception {

        String sdepartmentSql = "SELECT * FROM sdepartment WHERE del = 0 AND itemno = 'SPERSONNEL'";
        String condition = (String) context.getExtendInfo("condition")!=null && !((String) context.getExtendInfo("condition")).equals("") ?
                " AND " +(String) context.getExtendInfo("condition")+"OR pid = '0'":"";
        List<Map<String, Object>> sdepartmentList = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sdepartmentSql+condition);

        return StandardResultTranslate.getQueryResult(true,"Action_Query",sdepartmentList);
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }

}
