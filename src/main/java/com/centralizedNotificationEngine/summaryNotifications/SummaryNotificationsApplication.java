package com.centralizedNotificationEngine.summaryNotifications;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SummaryNotificationsApplication{

	public static void main(String[] args) {

		SpringApplication.run(SummaryNotificationsApplication.class, args);
	}
}
