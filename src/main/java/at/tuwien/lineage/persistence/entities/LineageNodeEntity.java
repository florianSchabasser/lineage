package at.tuwien.lineage.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;

@Node("LineageNode")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineageNodeEntity {

    @Id
    private String nodeId;
    private String applicationId;

    private String name;
    private String description;
    @Relationship(type = "sendsTo", direction = Relationship.Direction.OUTGOING)
    private LineageNodeEntity successor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageNodeEntity that = (LineageNodeEntity) o;
        return Objects.equals(nodeId, that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nodeId);
    }
}
