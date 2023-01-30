package vn.unicloud.genericequeue.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.unicloud.genericequeue.persistence.entities.Node;

public interface NodeRepository extends MongoRepository<Node, String> {
}
