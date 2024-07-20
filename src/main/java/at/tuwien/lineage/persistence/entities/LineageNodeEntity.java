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
public class LineageNodeEntity {

    @Id
    private String id;
    private String name;
    @Relationship(type = "REFERENCES", direction = Relationship.Direction.OUTGOING)
    private LineageNodeEntity reference;
    private String additionalInformation;

    public LineageNodeEntity(String id) {
        this.id = id;
    }

}
