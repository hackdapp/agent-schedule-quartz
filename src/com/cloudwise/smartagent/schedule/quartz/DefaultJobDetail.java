package com.cloudwise.smartagent.schedule.quartz;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cloudwise.smartagent.schedule.CronTag;
import com.cloudwise.smartagent.schedule.IScheduleEvent;

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
		IScheduleEvent scheduleEvent = (IScheduleEvent) paramMap.get(CronTag.SCHEDULEEVENT);
		Map param = (Map) paramMap.get(CronTag.PARAM);
		param.put(CronTag.SCHEDULECODE, paramMap.getString(CronTag.SCHEDULECODE));
		scheduleEvent.execute(param);
	}
}
