package com.thirdeye3.messagebroker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye3.messagebroker.dtos.Response;
import com.thirdeye3.messagebroker.dtos.Topic;
import com.thirdeye3.messagebroker.services.TopicService;

@RestController
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;
    
    @PostMapping("/{topicname}/{maxlength}")
    public Response<Topic> createTopic(@PathVariable("topicname") String topicName, @PathVariable("maxlength") Long maxLength) {
    	return new Response<>(true, 0, null, topicService.addTopic(topicName, maxLength));
    }
    
    @DeleteMapping("/{topicname}/{topickey}")
    public Response<String> deleteTopic(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey) {
    	topicService.removeTopic(topicName, topicKey);
    	return new Response<>(true, 0, null, "Topic removed successfully");
    }
    
    @PostMapping("remove/{topicname}/{topickey}")
    public Response<String> removeTopic(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey) {
    	topicService.emptyTopic(topicName, topicKey);
    	return new Response<>(true, 0, null, "Topic emptied successfully");
    }
    
}
