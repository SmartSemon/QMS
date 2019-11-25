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
 * @DATE: 2019/8/13 09:05
 * @Description:
 **/

public class SelectUserEditor extends AbstractAction
{
    @Override
    public Object executeAction() throws Exception {

        String sdepartmentSql = "SELECT * FROM sdepartment WHERE del = 0 AND itemno = 'SPERSONNEL'";
        String condition = (String) context.getExtendInfo("condition")!=null && !((String) context.getExtendInfo("condition")).equals("") ?
                " AND " +(String) context.getExtendInfo("condition")+" OR pid = '0'":"";
        List<Map<String, Object>> sdepartmentList = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(sdepartmentSql+condition);
        List resultList = new ArrayList<>(sdepartmentList);
        for (Map<String, Object> sdepartment : sdepartmentList) {
            String userSql = "SELECT suser.id AS id, CONCAT(suser.name,CONCAT(CONCAT('<',spersonnel.name),'>')) AS name,suser.password FROM " +
                    "sdepartment,spersonnel,crl_spersonnel_obj,suser,suser_relobj " +
                    "WHERE suser.del = 0 " +
                    "AND sdepartment.id = ? " +
                    "AND crl_spersonnel_obj.nodeid = ? "+
                    "AND spersonnel.id = crl_spersonnel_obj.itemid " +
                    "AND spersonnel.id = suser_relobj.itemaid " +
                    "AND suser.id = suser_relobj.itembid";
            List<Map<String, Object>> userList = new JdbcTemplate(DBConnecter.getDataSource()).
                    queryForList(userSql, new Object[]{sdepartment.get("id"),sdepartment.get("id")});
            //给用户赋值pid方便前台组装成树结构
            for (Map<String, Object> user:  userList){
                user.put("pid",sdepartment.get("id"));
            }
            resultList.addAll(userList);
        }

        return StandardResultTranslate.getQueryResult(true,"Action_Query",resultList);
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }

}
