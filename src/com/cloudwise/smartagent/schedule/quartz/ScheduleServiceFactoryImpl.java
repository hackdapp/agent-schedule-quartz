package com.cloudwise.smartagent.schedule.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.cloudwise.smartagent.schedule.AbstractScheduleServiceFactory;
import com.cloudwise.smartagent.schedule.IScheduleListener;
import com.cloudwise.smartagent.schedule.IScheduleService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author Life2014
 * 
 */
public class ScheduleServiceFactoryImpl extends AbstractScheduleServiceFactory {
	private Logger logger = Logger.getLogger(getClass());

	private static Scheduler scheduler;

	/**
	 * @return
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public static List<JobListener> getJobListeners() {
		return Lists.transform(lisenterList, new Function() {
			@Override
			public Object apply(Object joblistener) {
				return new DefaultJobListener((IScheduleListener) joblistener);
			}
		});
	}

	/**
	 * @return
	 * @see
	 */
	public static Scheduler getScheduler() {
		return scheduler;
	}

	@Override
	public void startup() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();

			for (JobListener tmpJobListener : getJobListeners()) {
				scheduler.getListenerManager().addJobListener(tmpJobListener);
			}
			scheduler.clear();
			scheduler.start();

			logger.info("start up the schedule factory success.");
		} catch (SchedulerException e1) {
			logger.error("start up the schedule factory error.", e1);
		}
	}

	@Override
	public void destory() {
		try {
			if (scheduler.isStarted()) {
				scheduler.shutdown();
			}

			logger.info("destory the schedule factory success.");
		} catch (SchedulerException e) {
			logger.error("destory the schedule factory error.", e);
		}
	}

	@Override
	public void doAfterStartService() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStarted() {
		try {
			return scheduler.isStarted();
		} catch (SchedulerException e) {
			return false;
		}
	}

	@Override
	public IScheduleService getIScheduleService() {
		return new ScheduleServiceImpl(this.scheduler);
	}
}
