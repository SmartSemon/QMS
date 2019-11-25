package com.usc.obj.api.type.task;

import java.sql.Types;
import java.util.List;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.obj.util.USCObjectQueryHelper;
import com.usc.server.jdbc.DBUtil;

public class TaskUtil {
    public static List getTaskInOrOutputData(ApplicationContext context, USCObject root, int page) {
        return DBUtil.getSQLResultByConditionLimit(context.getItemInfo(), "del=0 AND taskid=?", new Object[]
                {root.getID()}, new int[]
                {Types.VARCHAR}, page);
    }

    public static TaskObject getTaskObj(String itemNo, String id) {
        return (TaskObject) USCObjectQueryHelper.getObjectByID(itemNo, id);
    }

    public static boolean checkLeader(ApplicationContext context, USCObject root) {
        String cuser = root.getFieldValueToString("cuser");
        String leader = root.getFieldValueToString("LEADER");
        String user = context.getUserInformation().getUserName();
        if (user.equals(cuser) || user.equals(leader)) {
            return true;
        }
        return false;
    }

    public static boolean checkExecutor(ApplicationContext context, USCObject root) {
        String executor = root.getFieldValueToString("EXECUTOR");
        String user = context.getUserInformation().getUserName();
        if (user.equals(executor)) {
            return true;
        }
        return false;
    }

    public static boolean checkTaskState(ApplicationContext context, USCObject root) {
        Object tstate = root.getFieldValue("TSTATE");
        return "A".equals(tstate) || "E".equals(tstate);
    }

    public static USCObject[] getInputs(String taskID) {
        return USCObjectQueryHelper.getObjectsByCondition("TASKINPUT", "del=0 AND taskid='" + taskID + "'");
    }

    public static USCObject[] getInputBusinessItems(String taskID) {
        return USCObjectQueryHelper.getObjectsByCondition("TASKINPUT", "del=0 AND pid='0' AND taskid='" + taskID + "'");
    }

    /**
     * 获取输出对象
     * @param taskID 任务ID
     * @return 返回对象数组
     * @Author: lwp
     */
    public static USCObject[] getOutputs(String taskID) {
        return USCObjectQueryHelper.getObjectsByCondition("TASKOUTPUT", "del=0 AND pid='0' AND taskid='" + taskID + "'");
    }

    /**
     * 获取输出对象数据
     * @param taskID 任务ID
     * @return 返回对象数组
     * @Author: lwp
     */
    public static USCObject[] getOutputBusinessItems(String pid, String taskID) {
        return USCObjectQueryHelper.getObjectsByCondition("TASKOUTPUT", "del=0 AND pid='" + pid + "'" + " AND taskid='" + taskID + "'");
    }
}
