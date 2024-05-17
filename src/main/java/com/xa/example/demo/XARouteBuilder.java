package com.xa.example.demo;

import com.atomikos.jms.AtomikosConnectionFactoryBean;
import com.xa.example.demo.processor.MessageProcessor;
import com.xa.example.demo.processor.MessageReceiver;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.XAConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.Policy;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Component
public class XARouteBuilder extends RouteBuilder {



    @Override
    public void configure() throws Exception {

        from("jms:queue:IN?concurrentConsumers=5&connectionFactory=jmsConnectionFactory")
                .transacted("PROPAGATION_REQUIRED")
//                .threads(5)
                .doTry()
                .log("${id}")
                .bean(MessageProcessor.class)
                .doCatch(RuntimeException.class)
                .to("jms:queue:FAILED")
                .endDoTry()
                .end();


        from("jms:queue:OUT?concurrentConsumers=5&connectionFactory=jmsConnectionFactory")
                .transacted("PROPAGATION_REQUIRED")
//                .threads(5)
                .doTry()
                .log("${id}")
                .bean(MessageReceiver.class)
                .doCatch(RuntimeException.class)
                .to("jms:queue:FAILED2")
                .endDoTry()
                .end();

    }

    @Bean("PROPAGATION_REQUIRED")
    public Policy propagation(PlatformTransactionManager transactionManager){

        SpringTransactionPolicy policy = new SpringTransactionPolicy();
        policy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        policy.setTransactionManager(transactionManager);

        return policy;
    }

//    @Bean("readerCF")
//    @Primary
    private XAConnectionFactory readerCF(){
        XAConnectionFactory cf = new ActiveMQXAConnectionFactory();

        return cf;
    }

//    @Bean("writerCF")
    private XAConnectionFactory writerCF(){
        XAConnectionFactory cf = new ActiveMQXAConnectionFactory();

        return cf;
    }
}
