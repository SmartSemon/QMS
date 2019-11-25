package com.usc.app.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author: lwp
 * @DATE: 2019/9/2 15:21
 * @Description: 线条上的监听 ExecutionListener线条执行监听 TaskListener任务监听
 **/
public class TestListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.err.println("执行");
        System.err.println(execution.getVariable("itemNo"));
        execution.setVariable("pass",true);
        System.err.println(execution);
    }
}
