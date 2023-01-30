package vn.unicloud.genericequeue.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.unicloud.genericequeue.persistence.entities.Queue;

public interface QueueRepository extends MongoRepository<Queue, String> {
    Iterable<Queue>  findAllByTopic(String topicId);
}
