package ch.zhaw.deeplearningjava.mdm_project_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logger.info("Starting MDM Project 2...!");
		SpringApplication.run(Application.class, args);
	}

}
