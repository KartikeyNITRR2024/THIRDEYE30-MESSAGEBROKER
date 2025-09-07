package com.thirdeye3.messagebroker.utils;

import java.util.concurrent.TimeUnit;

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
    
    @Value("${thirdeye.priority}")
    private Integer priority;
    
    private Boolean isFirstTime = true;
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
    	TimeUnit.SECONDS.sleep(priority * 3);
        machineService.fetchMachines();
        if(testing == 1 && isFirstTime)
        {
        	topicService.addTopic("thresold", 1000L);
        	topicService.addTopic("telegramthresold", 1000L);
        	isFirstTime = false;
        }
        logger.info("Initiatier initialized.");
    }
	
	public void refreshMemory()
	{
		logger.info("Going to refersh memory...");
		topicService.emptyAllTopic();
		logger.info("Memory refreshed.");
	}

}


