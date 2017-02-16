package com.justice;

import com.justice.model.JusticeClientApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class DemojusticeApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemojusticeApplication.class, args);
	}

	//@Scheduled(fixedDelay = 15000)
	public void startClient(){
		JusticeClientApplication.main();
	}
}
