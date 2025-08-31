package com.thirdeye3.messagebroker.services.impl;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messagebroker.dtos.Message;
import com.thirdeye3.messagebroker.exceptions.MessageException;
import com.thirdeye3.messagebroker.services.MessageService;
import com.thirdeye3.messagebroker.services.QueueService;
import com.thirdeye3.messagebroker.services.TopicService;
import com.thirdeye3.messagebroker.utils.TimeManager;
import com.thirdeye3.messagebroker.services.MachineService;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    QueueService queueService;

    @Autowired
    TopicService topicService;

    @Autowired
    TimeManager timeManager;
    
	@Autowired
	MachineService machineService;

    @Override
    public Message getMessage(String topicName, String topicKey) {
        logger.info("Fetching a message from topic: {}, with key: {}", topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            logger.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new MessageException("Invalid message request");
        }
        if (topicService.isTopicEmpty(topicName)) {
            logger.warn("Topic {} is empty", topicName);
            throw new MessageException("Topic is empty");
        }
        Message message = queueService.getMessage(topicName);
        logger.info("Retrieved message from topic: {}", topicName);
        return message;
    }

    @Override
    public void setMessage(String topicName, String topicKey, Object message) {
        logger.info("Adding a message to topic: {}, key: {}", topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            logger.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new MessageException("Invalid message request");
        }
        Message newMessage = new Message(timeManager.getCurrentTime(), message);
        if (topicService.isTopicFull(topicName)) {
            logger.warn("Topic {} is full, removing oldest message", topicName);
            queueService.removeAndSetMessage(topicName, newMessage);
        }
        queueService.setMessage(topicName, newMessage);
        logger.info("Message added to topic: {}", topicName);
    }

    @Override
    public List<Message> getMessages(String topicName, String topicKey, Long count) {
        logger.info("Fetching {} messages from topic: {}, key: {}", count, topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            logger.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new MessageException("Invalid message request");
        }
        if (topicService.isTopicEmpty(topicName)) {
            logger.warn("Topic {} is empty", topicName);
            throw new MessageException("Topic is empty");
        }
        List<Message> messages = new ArrayList<>();
        long present = Math.min(count, queueService.getSizeOfQueue(topicName));
        for (long i = 0; i < present; i++) {
            messages.add(queueService.getMessage(topicName));
        }
        logger.info("Retrieved {} messages from topic: {}", messages.size(), topicName);
        return messages;
    }

    @Override
    public void setMessages(String topicName, String topicKey, List<Object> messages) {
        logger.info("Adding {} messages to topic: {}, key: {}", messages.size(), topicName, topicKey);
        if (!topicService.isTopicPresent(topicName, topicKey)) {
            logger.error("Invalid topic request: {}, key: {}", topicName, topicKey);
            throw new MessageException("Invalid message request");
        }
        for (Object message : messages) {
            Message newMessage = new Message(timeManager.getCurrentTime(), message);
            if (topicService.isTopicFull(topicName)) {
                logger.warn("Topic {} is full, removing oldest message", topicName);
                queueService.removeAndSetMessage(topicName, newMessage);
            }
            queueService.setMessage(topicName, newMessage);
        }
        logger.info("Added {} messages to topic: {}", messages.size(), topicName);
    }

	@Override
	public List<Message> getMessagesForTelegramBot(Integer telegramBotId, String telegramBotCode, String topicName,
			String topicKey, Long count) {
	    machineService.validateMachine(telegramBotId, telegramBotCode);
		return getMessages(topicName, topicKey, count);
	}

	@Override
	public Message getMessageForTelegramBot(Integer telegramBotId, String telegramBotCode, String topicName,
			String topicKey) {
	    machineService.validateMachine(telegramBotId, telegramBotCode);
		return getMessage(topicName, topicKey);
	}
}
