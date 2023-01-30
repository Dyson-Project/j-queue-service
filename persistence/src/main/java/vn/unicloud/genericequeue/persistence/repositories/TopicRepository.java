package vn.unicloud.genericequeue.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.unicloud.genericequeue.persistence.entities.Topic;

public interface TopicRepository extends MongoRepository<Topic, String> {
}
