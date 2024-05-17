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
import java.util.Optional;

@Component
@Slf4j
public class MessageReceiver implements Processor {

    @Autowired
    private AuditRepository auditRepository;

    @Override
    @Transactional
    public void process(Exchange exchange) throws Exception {

        log.info(exchange.getExchangeId());

        Optional<Audit> audit = auditRepository.findById(exchange.getIn().getBody(String.class));

        if(audit.isEmpty()){
            log.error("Record not found in database");
            throw new RuntimeException("Record not found in database");
        }
    }
}
