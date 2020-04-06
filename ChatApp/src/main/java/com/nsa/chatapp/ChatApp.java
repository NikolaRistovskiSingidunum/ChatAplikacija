package com.nsa.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nsa.chatapp.websocket.SocketConnector;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//@EnableJpaRepositories(basePackages={"com.nsa.demo"})
@ComponentScan("com.nsa.chatapp")
public class ChatApp implements ApplicationListener<ContextRefreshedEvent> {

	SocketConnector socketConnector;
	
	
	
	public static void main(String[] args) {
		//System.out.println("Ulazna tacka u aplikaciju");
		//new Thread(new SocketConnector()).start();
		SpringApplication.run(ChatApp.class, args);
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.toString());
		System.out.println("Ulazna tacka u aplikaciju");
		
		if(socketConnector == null)
		{
			socketConnector = new SocketConnector();
			new Thread(socketConnector).start();
		}
		
	}


	

}
