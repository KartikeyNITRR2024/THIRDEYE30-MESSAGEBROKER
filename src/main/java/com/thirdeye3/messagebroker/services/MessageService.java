package com.thirdeye3.messagebroker.services;

import java.util.List;

import com.thirdeye3.messagebroker.dtos.Message;

public interface MessageService {

	Message getMessage(String topicName, String topicKey);
	
	List<Message> getMessages(String topicName, String topicKey, Long count);

	void setMessage(String topicName, String topicKey, Object message);
	
	void setMessages(String topicName, String topicKey, List<Object> messages);

	List<Message> getMessagesForTelegramBot(Integer telegramBotId, String telegramBotCode, String topicName, String topicKey,
			Long count);

	Message getMessageForTelegramBot(Integer telegramBotId, String telegramBotCode, String topicName, String topicKey);

}
