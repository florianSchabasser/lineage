package at.tuwien.lineage.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;
import java.util.Set;

@Node("LineageFlow")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineageFlowEntity {

    // Guaranties fault tolerance - duplicate flows will be overridden
    @Id
    private String flowId; //        = applicationId#rddId#recordId
    private String nodeId; //        = applicationId#rddId
    private String applicationId; // = applicationId

    private String hashIn;
    private String hashOut;
    @Relationship(type = "flow", direction = Relationship.Direction.OUTGOING)
    private Set<LineageFlowEntity> successor;

    private String value;
    private String name;
    private String description;

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
