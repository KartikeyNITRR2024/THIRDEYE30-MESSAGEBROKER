package com.thirdeye3.messagebroker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye3.messagebroker.dtos.Message;
import com.thirdeye3.messagebroker.dtos.Response;
import com.thirdeye3.messagebroker.services.MessageService;

@RestController
@RequestMapping("/mb/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{topicname}/{topickey}")
    public Response<Message> getMessage(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey) {
        return new Response<>(true, 0, null, messageService.getMessage(topicName, topicKey));
    }
    
    @GetMapping("/multiple/{topicname}/{topickey}/{count}")
    public Response<List<Message>> getMultipleMessage(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey, @PathVariable("count") Long count) {
        return new Response<>(true, 0, null, messageService.getMessages(topicName, topicKey, count));
    }
    
    @GetMapping("/telegrambot/{id}/{code}/{topicname}/{topickey}")
    public Response<Message> getMessageForTelegramBot(@PathVariable("id") Integer telegramBotId, @PathVariable("code") String telegramBotCode, @PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey) {
        return new Response<>(true, 0, null, messageService.getMessageForTelegramBot(telegramBotId, telegramBotCode, topicName, topicKey));
    }
    
    @GetMapping("/telegrambot/multiple/{id}/{code}/{topicname}/{topickey}/{count}")
    public Response<List<Message>> getMultipleMessageForTelegramBot(@PathVariable("id") Integer telegramBotId, @PathVariable("code") String telegramBotCode, @PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey, @PathVariable("count") Long count) {
        return new Response<>(true, 0, null, messageService.getMessagesForTelegramBot(telegramBotId, telegramBotCode, topicName, topicKey, count));
    }
    
    @PostMapping("/{topicname}/{topickey}")
    public Response<String> setMessage(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey, @RequestBody Object message) {
    	messageService.setMessage(topicName, topicKey, message);
    	return new Response<>(true, 0, null, "Message is added in topic");
    }
    
    @PostMapping("/multiple/{topicname}/{topickey}")
    public Response<String> setMessages(@PathVariable("topicname") String topicName, @PathVariable("topickey") String topicKey, @RequestBody List<Object> messages) {
    	messageService.setMessages(topicName, topicKey, messages);
    	return new Response<>(true, 0, null, "Messages are added in topic");
    }
}
