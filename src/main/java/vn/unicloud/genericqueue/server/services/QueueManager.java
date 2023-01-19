package vn.unicloud.genericqueue.server.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueueManager {
    private Map<String, Queue> queues = new ConcurrentHashMap<>();

}
