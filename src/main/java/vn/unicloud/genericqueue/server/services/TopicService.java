package vn.unicloud.genericqueue.server.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.unicloud.genericqueue.protobuf.TopicInfo;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TopicService {
    @Autowired
    private QueueManager queueManager;

}
