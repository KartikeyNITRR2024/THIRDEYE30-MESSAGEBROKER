package com.thirdeye3.messagebroker.services;

import com.thirdeye3.messagebroker.dtos.Message;

public interface QueueService {

	void addQueue(String topicName);

	Message getMessage(String topicName);

	void setMessage(String topicName, Message message);

	int getSizeOfQueue(String topicName);

	int getSizeOfMap();

	void clearQueue(String topicName);

	void clearMap();

	void removeAndSetMessage(String topicName, Message message);

	void removeQueue(String topicName);

}
