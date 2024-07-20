package at.tuwien.lineage.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Data
@AllArgsConstructor
public class OperationEntity {

    @Id
    private String id;
    private String name;
    private String description;
    @Relationship(type = "REFERENCES", direction = Relationship.Direction.INCOMING)
    private List<OperationEntity> references;
    private String source;

    public OperationEntity(String id) {
        this.id = id;
    }
}
