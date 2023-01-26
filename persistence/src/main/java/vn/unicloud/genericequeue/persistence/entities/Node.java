package vn.unicloud.genericequeue.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Iterator;

@Document("node")
public class Node {
    @Id
    String id;
    String schemaId;
    byte[] payload;
    Iterator<Header> headers;
}
