package vn.unicloud.genericequeue.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("topic")
public class Topic {
    @Id
    private String name;
    private String tenantGuid;
    private String schemaId;
    private int queueType;
    private int topicSize;
}
