package com.usc.app.action.editor;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.mate.MateFactory;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.DBConnecter;
import com.usc.server.md.ItemField;
import com.usc.server.md.ItemGridField;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lwp
 * @DATE: 2019/8/19 16:23
 * @Description:
 **/
public class ItemSelector extends AbstractAction {
    @Override
    public Object executeAction() throws Exception {
        Map map = new HashMap();
        //获取gridFieldList
        String itemNo = (String) context.getExtendInfo("itemNo");
        List<ItemGridField> gridFieldList = MateFactory.getItemInfo(itemNo).getDefaultItemGrid().getGridFieldList();
        map.put("gridFieldList", gridFieldList);
        String itemName = MateFactory.getItemInfo(itemNo).getName();
        map.put("itemName", itemName);
        //获取对象数据
        String condition = (String) context.getExtendInfo("condition");
        String tableName = MateFactory.getItemInfo(itemNo).getTableName();
        String dataListSql = condition != null && !"".equals(condition) ? "SELECT * FROM " + tableName + " WHERE del = 0 AND " + condition :
                "SELECT * FROM " + tableName + " WHERE del = 0";
        List<Map<String, Object>> list = new JdbcTemplate(DBConnecter.getDataSource()).queryForList(dataListSql);
        map.put("dataList", list);
        return StandardResultTranslate.getResult("Action_Query", map);
    }

    @Override
    public boolean disable() throws Exception {
        return true;
    }
}
