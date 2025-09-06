package com.thirdeye3.messagebroker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye3.messagebroker.services.MachineService;
import com.thirdeye3.messagebroker.services.TopicService;

import jakarta.annotation.PostConstruct;

@Component
public class Initiatier {
	
    private static final Logger logger = LoggerFactory.getLogger(Initiatier.class);
 
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private MachineService machineService;
    
    @Value("${thirdeye.istesting}")
    private Integer testing;
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
        topicService.emptyAllTopic();
        machineService.fetchMachines();
        if(testing == 1)
        {
        	topicService.addTopic("thresold", 1000L);
        	topicService.addTopic("telegramthresold", 1000L);
        }
        logger.info("Initiatier initialized.");
    }

}


