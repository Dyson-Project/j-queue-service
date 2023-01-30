package vn.unicloud.genericequeue.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.unicloud.genericequeue.persistence.entities.GenericSchema;

public interface SchemaRepository extends MongoRepository<GenericSchema, String> {
}
