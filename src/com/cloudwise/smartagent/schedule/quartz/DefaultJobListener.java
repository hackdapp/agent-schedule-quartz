package com.cloudwise.smartagent.schedule.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.cloudwise.smartagent.schedule.IScheduleListener;

/**
 * 调度任务缺省监听类
 * 
 * @author LIFE2014
 * @version 2014-11-5
 * @see DefaultJobListener
 * @since
 */
public class DefaultJobListener implements JobListener {
	private IScheduleListener listener = null;

	/**
	 * @param listener
	 */
	public DefaultJobListener(IScheduleListener listener) {
		this.listener = listener;
	}

	@Override
	public String getName() {
		return listener.getName();
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		//TODO do some stuff
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException exceptionContext) {
		//TODO do some stuff
	}
}
