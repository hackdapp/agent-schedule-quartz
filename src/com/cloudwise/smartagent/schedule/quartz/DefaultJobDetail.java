package com.cloudwise.smartagent.schedule.quartz;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cloudwise.smartagent.schedule.IScheduleEvent;
import com.cloudwise.smartagent.schedule.quartz.utils.CronTag;

/**
 * 功能简述:〈一句话描述〉 详细描述:〈功能详细描述〉
 * 
 * @author LIFE2014
 * @version 2014-10-20
 * @see DefaultJobDetail
 * @since
 */
public class DefaultJobDetail implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		//1.get the schedule's event.
		IScheduleEvent scheduleEvent = (IScheduleEvent) paramMap.get(CronTag.SCHEDULEEVENT);
		//2.get the schedule'params.
		Map param = (Map) paramMap.get(CronTag.PARAM);
		param.put(CronTag.SCHEDULECODE, paramMap.getString(CronTag.SCHEDULECODE));
		//3.inject the param and execute the schedule's event.
		scheduleEvent.execute(param);
	}
}
