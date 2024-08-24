package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends Neo4jRepository<LineageFlowEntity, String> {

    @Query("MERGE (current:LineageFlowEntity { flowId: $flowId, nodeId: $nodeId, applicationId: $applicationId, " +
            "hashIn: $hashIn, hashOut: $hashOut, value: $value, name: $name, description: $description }) " +
            "WITH current " +
            "MATCH (pred:LineageFlowEntity { applicationId: $applicationId, hashOut: $hashIn }) " +
            "MERGE (pred)-[:flow]->(current) " +
            "WITH current " +
            "MATCH (succ:LineageFlowEntity { applicationId: $applicationId, hashIn: $hashOut }) " +
            "MERGE (current)-[:flow]->(succ)")
    void createAndLinkEntities(String flowId, String nodeId, String applicationId, String hashIn,
                                            String hashOut, String value, String name, String description);
}