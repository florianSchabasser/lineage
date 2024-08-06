package at.tuwien.lineage.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineageFlowEntity {

    @Id
    private String id;
    private String nodeId;
    private String hashIn;
    private String hashOut;
    @Relationship(type = "flow", direction = Relationship.Direction.OUTGOING)
    private LineageFlowEntity successor;
    private String value;
    private Integer rowNum;

    private String name;
    private String description;
    private String fileName;
}
