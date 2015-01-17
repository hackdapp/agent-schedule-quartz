package com.cloudwise.smartagent.schedule.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.cloudwise.smartagent.schedule.IScheduleEvent;
import com.cloudwise.smartagent.schedule.IScheduleService;
import com.cloudwise.smartagent.schedule.quartz.utils.CronTag;

public class ScheduleServiceImpl implements IScheduleService {
	private Logger logger = Logger.getLogger(getClass());

	private Scheduler scheduler;

	public ScheduleServiceImpl(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public boolean exist(String scheduleId) {
		try {
			return scheduler.checkExists(new JobKey(scheduleId));
		} catch (SchedulerException e) {
			logger.error("invoke the schedule service method[exist(id)]  error.", e);
		}
		return false;
	}

	@Override
	public void addSchedule(String scheduleId, String name, String cronExp,
			IScheduleEvent event, Map param) {
		JobDataMap dataBean = new JobDataMap();
		dataBean.put(CronTag.PARAM, param);
		dataBean.put(CronTag.SCHEDULECODE, scheduleId);
		dataBean.put(CronTag.SCHEDULEEVENT, event);

		JobDetail taskJobImpl = newJob(DefaultJobDetail.class)
				.withIdentity(new JobKey(scheduleId)).usingJobData(dataBean)
				.build();
		Trigger taskTrigger = null;
		if (cronExp != null && cronExp.trim().length() > 0) {
			taskTrigger = newTrigger().withIdentity(new TriggerKey(scheduleId))
					.withSchedule(cronSchedule(cronExp)).build();
		} else {
			taskTrigger = newTrigger().withIdentity(new TriggerKey(scheduleId))
					.startNow().build();
		}
		try {
			scheduler.scheduleJob(taskJobImpl, taskTrigger);
		} catch (SchedulerException e) {
			logger.error("invoke the schedule service method[addSchedule]  error.", e);
		}
	}

	@Override
	public void updateSchedule(String scheduleId, String name, String cronExp,
			IScheduleEvent event, Map param) {
		try {
			if (scheduler.checkExists(new JobKey(scheduleId))) {
				JobDetail jd = scheduler.getJobDetail(new JobKey(scheduleId));
				JobDataMap dataBean = jd.getJobDataMap();
				dataBean.put(CronTag.SCHEDULECODE, scheduleId);
				dataBean.put(CronTag.SCHEDULEEVENT, event);
				dataBean.put(CronTag.PARAM, param);

				scheduler.pauseTrigger(new TriggerKey(scheduleId));
				scheduler.unscheduleJob(new TriggerKey(scheduleId));
				scheduler.deleteJob(new JobKey(scheduleId));

				Trigger trg = scheduler.getTrigger(new TriggerKey(scheduleId));
				trg = newTrigger().withIdentity(new TriggerKey(scheduleId))
						.withSchedule(cronSchedule(cronExp)).build();
				scheduler.scheduleJob(jd, trg);
			}
		} catch (SchedulerException e) {
			logger.error("invoke the schedule service method[updateSchedule]  error.", e);
		}

	}

	@Override
	public void deleteSchedule(String scheduleId) {
		try {
			if (scheduler.checkExists(new JobKey(scheduleId))) {
				scheduler.pauseTrigger(new TriggerKey(scheduleId));
				scheduler.unscheduleJob(new TriggerKey(scheduleId));
				scheduler.deleteJob(new JobKey(scheduleId));
			}
		} catch (SchedulerException e) {
			logger.error("invoke the schedule service method[deleteSchedule]  error.", e);
		}

	}

	@Override
	public void runNow(String scheduleId, String name, String cronExp,
			IScheduleEvent event, Map param) {
		if (exist(scheduleId)) {
			try {
				scheduler.triggerJob(new JobKey(scheduleId));
			} catch (SchedulerException e) {
				logger.error("invoke the schedule service method[runNow]  error.", e);
			}
		} else {
			JobDataMap dataBean = new JobDataMap();
			dataBean.put(CronTag.SCHEDULECODE, scheduleId);
			dataBean.put(CronTag.SCHEDULEEVENT, event);
			dataBean.put(CronTag.PARAM, param);

			JobDetail taskJobImpl = newJob(DefaultJobDetail.class)
					.withIdentity(new JobKey(scheduleId))
					.usingJobData(dataBean).build();
			Trigger taskTrigger = newTrigger()
					.withIdentity(new TriggerKey(scheduleId)).startNow()
					.build();
			try {
				scheduler.scheduleJob(taskJobImpl, taskTrigger);
			} catch (SchedulerException e) {
				logger.error("invoke the schedule service method[runNow]  error.", e);
			}
		}
	}
}
