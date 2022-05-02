package com.anudeep.onlineshop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * @author anude
 *
 */
@SpringBootApplication
public class OnlineShopApplication implements ApplicationRunner {

	
	 private static final Logger logger = LogManager.getLogger(OnlineShopApplication.class);
	 
	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}
	
	@Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
    }

	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
}
