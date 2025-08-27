package com.thirdeye3.messagebroker.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.thirdeye3.messagebroker.dtos.Topic;
import com.thirdeye3.messagebroker.exceptions.TopicException;
import com.thirdeye3.messagebroker.services.QueueService;
import com.thirdeye3.messagebroker.services.TopicService;
import com.thirdeye3.messagebroker.utils.CodeGenerator;

@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);
    private final Map<String, Topic> topicMap = new HashMap<>();

    @Autowired
    QueueService queueService;

    @Value("${thirdeye.maximumqueuesize}")
    private Long maximumQueueSize;

    @Value("${thirdeye.maximumqueues}")
    private Long maximumQueues;

    @Override
    public Topic addTopic(String topicName, Long maxLength) {
        logger.info("Attempting to add topic: {}, with maxLength: {}", topicName, maxLength);
        if (topicMap.size() >= maximumQueues) {
            logger.error("Cannot add topic {}: Maximum number of topics ({}) exceeded", topicName, maximumQueues);
            throw new TopicException("Maximum topic exceeds");
        }
        if (maxLength > maximumQueueSize) {
            logger.error("Invalid maxLength {} for topic {}: Cannot exceed {}", maxLength, topicName, maximumQueueSize);
            throw new TopicException("Maximum size is invalid. Not greater than " + maximumQueueSize + ".");
        }
        if (topicMap.containsKey(topicName)) {
            logger.error("Topic {} already exists", topicName);
            throw new TopicException("Topic allready exists");
        }
        String topicKey = CodeGenerator.generateUniqueCode(8);
        queueService.addQueue(topicName);
        Topic topic = new Topic(topicName, topicKey, maxLength);
        topicMap.put(topicName, topic);
        logger.info("Topic {} added successfully with key {}", topicName, topicKey);
        return topic;
    }

    @Override
    public boolean isTopicPresent(String topicName, String topicKey) {
        boolean present = topicMap.containsKey(topicName) && topicMap.get(topicName).getTopicKey().equals(topicKey);
        logger.debug("Checking if topic {} with key {} is present: {}", topicName, topicKey, present);
        return present;
    }

    @Override
    public boolean isTopicFull(String topicName) {
        int currentSize = queueService.getSizeOfQueue(topicName);
        boolean full = currentSize >= topicMap.get(topicName).getMaxSize();
        logger.debug("Checking if topic {} is full: {}", topicName, full);
        return full;
    }

    @Override
    public boolean isTopicEmpty(String topicName) {
        boolean empty = queueService.getSizeOfQueue(topicName) == 0;
        logger.debug("Checking if topic {} is empty: {}", topicName, empty);
        return empty;
    }

    @Override
    public void emptyTopic(String topicName, String topicKey) {
        logger.info("Clearing topic: {}, key: {}", topicName, topicKey);
        if (!isTopicPresent(topicName, topicKey)) {
            logger.error("Failed to clear topic {}: Invalid key {}", topicName, topicKey);
            throw new TopicException("Invalid topic request");
        }
        queueService.clearQueue(topicName);
        logger.info("Topic {} cleared successfully", topicName);
    }

    @Override
    public void emptyMap() {
        logger.info("Clearing all topics and their queues");
        queueService.clearMap();
        topicMap.clear();
        logger.info("All topics cleared successfully");
    }

    @Override
    public void removeTopic(String topicName, String topicKey) {
        logger.info("Removing topic: {}, key: {}", topicName, topicKey);
        if (!isTopicPresent(topicName, topicKey)) {
            logger.error("Failed to remove topic {}: Invalid key {}", topicName, topicKey);
            throw new TopicException("Invalid topic request");
        }
        queueService.removeQueue(topicName);
        topicMap.remove(topicName);
        logger.info("Topic {} removed successfully", topicName);
    }
}
