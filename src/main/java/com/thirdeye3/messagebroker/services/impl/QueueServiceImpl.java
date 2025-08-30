package com.thirdeye3.messagebroker.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thirdeye3.messagebroker.dtos.Message;
import com.thirdeye3.messagebroker.services.QueueService;

@Service
public class QueueServiceImpl implements QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueServiceImpl.class);
    private final Map<String, ConcurrentLinkedDeque<Message>> queueMap = new ConcurrentHashMap<>();

    @Override
    public void addQueue(String topicName) {
        logger.info("Adding a new queue for topic: {}", topicName);
        queueMap.put(topicName, new ConcurrentLinkedDeque<>());
    }

    @Override
    public Message getMessage(String topicName) {
        logger.info("Fetching a message from topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        Message message = deque.pollFirst();
        if (message == null) {
            logger.warn("No message found in topic: {}", topicName);
        } else {
            logger.info("Message retrieved from topic: {}", topicName);
        }
        return message;
    }

    @Override
    public void setMessage(String topicName, Message message) {
        logger.info("Adding message to topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        deque.addLast(message);
        logger.debug("Queue size for {} after adding: {}", topicName, deque.size());
    }

    @Override
    public void removeAndSetMessage(String topicName, Message message) {
        logger.info("Replacing oldest message in topic: {}", topicName);
        ConcurrentLinkedDeque<Message> deque = queueMap.get(topicName);
        deque.pollFirst();
        deque.addLast(message);
        logger.debug("Queue size for {} after replacement: {}", topicName, deque.size());
    }

    @Override
    public int getSizeOfQueue(String topicName) {
        int size = queueMap.get(topicName).size();
        logger.debug("Queue size for {}: {}", topicName, size);
        return size;
    }

    @Override
    public int getSizeOfMap() {
        int size = queueMap.size();
        logger.debug("Total number of queues: {}", size);
        return size;
    }

    @Override
    public void clearQueue(String topicName) {
        logger.info("Clearing queue for topic: {}", topicName);
        queueMap.put(topicName, new ConcurrentLinkedDeque<>());
    }

    @Override
    public void clearMap() {
        logger.info("Clearing all queues");
        queueMap.clear();
    }

    @Override
    public void removeQueue(String topicName) {
        logger.info("Removing queue for topic: {}", topicName);
        queueMap.remove(topicName);
    }
}
