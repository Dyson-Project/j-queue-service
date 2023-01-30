package vn.unicloud.genericequeue.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Iterator;
import java.util.List;

@Document("node")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    @Id
    String id;
    String schemaId;
    byte[] payload;
    List<Header> headers;
    byte[] replayId;

    String queueId;
}
