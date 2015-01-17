package com.cloudwise.smartagent.schedule.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.cloudwise.smartagent.schedule.CronTag;
import com.cloudwise.smartagent.schedule.IScheduleEvent;
import com.cloudwise.smartagent.schedule.ScheduleService;

public class ScheduleServiceImpl implements ScheduleService {
	private Scheduler scheduler;

	public ScheduleServiceImpl() {
		scheduler = ScheduleServiceFactoryImpl.getScheduler();
	}

	@Override
	public boolean exist(String scheduleId) {
		try {
			return scheduler.checkExists(new JobKey(scheduleId));
		} catch (SchedulerException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}

	}

	@Override
	public void runNow(String scheduleId, String name, String cronExp,
			IScheduleEvent event, Map param) {
		if (exist(scheduleId)) {
			try {
				scheduler.triggerJob(new JobKey(scheduleId));
			} catch (SchedulerException e) {
				e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
}
