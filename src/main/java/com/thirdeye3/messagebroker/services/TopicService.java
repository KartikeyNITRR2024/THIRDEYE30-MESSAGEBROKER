package com.thirdeye3.messagebroker.services;

import com.thirdeye3.messagebroker.dtos.Topic;

public interface TopicService {

	void removeTopic(String topicName, String topicKey);

	void emptyMap();

	void emptyTopic(String topicName, String topicKey);

	boolean isTopicEmpty(String topicName);

	boolean isTopicFull(String topicName);

	Topic addTopic(String topicName, Long maxLength);

	boolean isTopicPresent(String topicName, String topicKey);

}
