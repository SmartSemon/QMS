package com.usc.app.action.task;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.action.task.util.TaskActionResult;
import com.usc.obj.api.USCObject;
import com.usc.obj.api.type.task.TaskObject;
import com.usc.obj.api.type.task.TaskUtil;
import com.usc.server.util.SystemTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SubmitTaskAction extends AbstractAction {

    @Override
    public Object executeAction() throws Exception {
        USCObject[] objects = context.getSelectObjs();
        //修改数据和状态
        Map<String, Object> formData = context.getInitData();
        List nameList = new ArrayList();
        for (USCObject uscObject : objects) {
            TaskObject object = (TaskObject) uscObject;
            //获取输出对象遍历
            USCObject[] outObj = TaskUtil.getOutputs(object.getID());
            //先判断任务是否有输出对象，有则必须有输出数据
            if (outObj != null) {
                boolean isAllOutputHasOutputBusinessItems = hasOutputBusinessItems(outObj, object.getID());
                if (!isAllOutputHasOutputBusinessItems) {
                    //有输出对象，但是有输出对象无输出数据，不修改数据，提示;
                    nameList.add(uscObject.getFieldValue("name"));
                } else {
                    // 有输出对象，输出对象都是有输出对象数据，修改数据
                    object.setFieldValue("TSTATE", "D");
                    object.setFieldValue("subDescription", formData.get("subDescription"));
                    object.setFieldValue("SBTIME", SystemTime.getTimestamp());
                    object.save(context);
                }
            } else {
                //无输出对象，直接提交成功
                object.setFieldValue("TSTATE", "D");
                object.setFieldValue("subDescription", formData.get("subDescription"));
                object.setFieldValue("SBTIME", SystemTime.getTimestamp());
                object.save(context);
            }
        }
        return nameList.size() > 0 ? TaskActionResult.getResult(null, "NoOutput_Submit_Task", "D")
                : TaskActionResult.getResult(null, "Submit_Task_Successfully", "D");
    }

    /**
     * 遍历输出对象是否都有输出数据
     * @param outObj 输出对象
     * @param taskId 任务id
     * @return boolean
     */
    private boolean hasOutputBusinessItems(USCObject[] outObj, String taskId) {
        for (USCObject out : outObj) {
            //判断每个输出对象是否有输出数据
            USCObject[] outs = TaskUtil.getOutputBusinessItems(out.getID(), taskId);
            if (outs == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean disable() throws Exception {
        USCObject[] objects = context.getSelectObjs();
        for (USCObject uscObject : objects) {
            TaskObject object = (TaskObject) uscObject;
            if (!object.getTaskState().equals("C")) {
                return true;
            }
            if (!object.getExecutor().equals(context.getUserInformation().getUserName())) {
                return true;
            }
        }
        return false;
    }

}
