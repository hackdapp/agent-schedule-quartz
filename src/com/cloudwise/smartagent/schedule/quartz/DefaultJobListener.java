package com.cloudwise.smartagent.schedule.quartz;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.cloudwise.smartagent.schedule.ScheduleListener;


/**
 * 调度任务缺省监听类
 * 
 * @author LIFE2014
 * @version 2014-11-5
 * @see DefaultJobListener
 * @since
 */
public class DefaultJobListener implements JobListener {
    private ScheduleListener listener = null;

    /**
     * @param listener
     */
    public DefaultJobListener(ScheduleListener listener) {
        this.listener = listener;
    }

    @Override
    public String getName() {
        return listener.getName();
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext arg0) {}

    @Override
    public void jobToBeExecuted(JobExecutionContext arg0) {
        this.listener.beginEvent(arg0.getFireInstanceId());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
        this.listener.finishEvent(arg0.getFireInstanceId());
    }
}
