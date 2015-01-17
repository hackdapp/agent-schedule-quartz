package com.cloudwise.smartagent.schedule.quartz.bundle;

import java.util.Dictionary;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.cloudwise.smartagent.schedule.AbstractScheduleServiceFactory;
import com.cloudwise.smartagent.schedule.IScheduleService;
import com.cloudwise.smartagent.schedule.quartz.ScheduleServiceFactoryImpl;

/**
 * @author Life2014
 *
 */
public class Activator implements BundleActivator {
	private Logger logger = Logger.getLogger(getClass());
	
	private AbstractScheduleServiceFactory factory;

	public Activator() {
		factory = new ScheduleServiceFactoryImpl();
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		// 启动服务应放在agent-service中，自动检测服务状态，如若调度出问题，应该自动重启调度并发布告警消息
		factory.startup();
		factory.doAfterStartService();

		if (factory.isStarted()) {
			Dictionary dic = new Properties();
			bundleContext.registerService(IScheduleService.class,factory.getIScheduleService(), dic);
			logger.info("register the schedule serivce. [success]");
		}
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ServiceReference srf = bundleContext.getServiceReference(IScheduleService.class);
		bundleContext.ungetService(srf);
		logger.info("unregister the schedule serivce. [success]");
		factory.destory();
	}
}
