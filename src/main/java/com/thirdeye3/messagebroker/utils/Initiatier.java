package com.thirdeye3.messagebroker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdeye3.messagebroker.services.TopicService;

import jakarta.annotation.PostConstruct;

@Component
public class Initiatier {
	
    private static final Logger logger = LoggerFactory.getLogger(Initiatier.class);
 
    @Autowired
    private TopicService topicService;
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
        topicService.emptyAllTopic();
        logger.info("Initiatier initialized.");
    }

}


