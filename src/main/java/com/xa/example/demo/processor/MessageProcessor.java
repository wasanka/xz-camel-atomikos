package com.xa.example.demo.processor;

import com.xa.example.demo.entity.Audit;
import com.xa.example.demo.repository.AuditRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MessageProcessor implements Processor {

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    @Transactional
    public void process(Exchange exchange) throws Exception {

        log.info(exchange.getExchangeId());

        producerTemplate.sendBody("jms:queue:OUT&connectionFactory=jmsConnectionFactory", exchange.getExchangeId());

        Audit audit = new Audit(exchange.getExchangeId(), new Date());
        auditRepository.save(audit);

        if(exchange.getIn().getBody(String.class).contains("error")){

            throw new RuntimeException("known error...");
        }
    }
}