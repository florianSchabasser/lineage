package at.tuwien.lineage.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;
import java.util.Set;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineageFlowEntity {

    // Prevent race conditions between stages with optimistic locking
    // Emit messages with task unique id as key
    @Version
    private Long version;

    // applicationId#flowId#flowId
    // guaranties fault tolerance - duplicate flows will be overridden
    @Id
    private String flowId;
    private String nodeId;
    private String applicationId;

    private String hashIn;
    private String hashOut;
    @Relationship(type = "flow", direction = Relationship.Direction.OUTGOING)
    private Set<LineageFlowEntity> successor;
    private String value;

    private String name;
    private String description;
    private String fileName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageFlowEntity that = (LineageFlowEntity) o;
        return Objects.equals(flowId, that.flowId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flowId);
    }
}
