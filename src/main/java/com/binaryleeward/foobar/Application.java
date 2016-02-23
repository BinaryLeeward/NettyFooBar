package com.binaryleeward.foobar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:conf/server.properties")
public class Application {
	
	  public static void main(String[] args) throws Exception {
	    	 ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
	    	 Server server = context.getBean(Server.class);
	    	 server.start();
	    }
}
