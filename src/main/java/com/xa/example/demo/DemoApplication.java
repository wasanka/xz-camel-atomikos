package com.xa.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan
@ComponentScan
@EnableTransactionManagement
@EnableAutoConfiguration
public class DemoApplication {

	public static void main(String[] args) throws Exception {

		/*BrokerService broker = new BrokerService();

		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61616"));
		broker.setPersistent(false);
		broker.addConnector(connector);
		broker.start();*/


		SpringApplication.run(DemoApplication.class, args);
	}

}
