package org.springframework.batch.admin.statusnotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "StatusNotificationRunnable")
public class StatusNotificationRunnable implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(StatusNotificationRunnable.class);

	@Autowired
	@Qualifier("StatusNotificationCheckTask")
	StatusNotificationCheckTask statusNotificationCheckTask;

	public void run() {
		logger.info("StatusNotificationRunnable Thread started...");

		statusNotificationCheckTask.sendJobStatusNotification();

		logger.info("StatusNotificationRunnable Thread completed...");
	}

}
