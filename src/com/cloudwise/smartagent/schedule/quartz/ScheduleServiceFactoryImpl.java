/*
 * 文件名：ScheduleServiceFactory.java 版权：Copyright by www.wmccn.com 描述： 修改人：LIFE2014 修改时间：2014-9-26
 * 跟踪单号： 修改单号： 修改内容：
 */

package com.cloudwise.smartagent.schedule.quartz;


import java.util.ArrayList;
import java.util.List;

import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.cloudwise.smartagent.schedule.AbstractScheduleServiceFactory;
import com.cloudwise.smartagent.schedule.ScheduleListener;


/**
 * 调度服务工厂实现类
 * 
 * @author LIFE2014
 * @version 2014-11-5
 * @see ScheduleServiceFactoryImpl
 * @since
 */
public class ScheduleServiceFactoryImpl extends AbstractScheduleServiceFactory {
    private static Scheduler scheduler;

    /**
     * @return
     * @see
     */
    public static List<JobListener> getJobListeners() {
        List<JobListener> list = new ArrayList<JobListener>();
        for (ScheduleListener sl : lisenterList) {
            list.add(new DefaultJobListener(sl));
        }
        return list;
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
            // 实例化调度类
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 注册监听类，用于处理调度通知、日志收集
            for (JobListener tmpJobListener : getJobListeners()) {
                scheduler.getListenerManager().addJobListener(tmpJobListener);
            }
            scheduler.clear();
            scheduler.start();
        } catch (SchedulerException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void destory() {
        try {
            if (scheduler.isStarted()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
