package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LineageFlowRepository extends Neo4jRepository<LineageFlowEntity, String> {

    @Query("MERGE (current:LineageFlowEntity { flowId: $flowId }) " +
            "ON CREATE SET current.applicationId = $applicationId, current.hashIn = $hashIn, current.hashOut = $hashOut, " +
            "current.value = $value, current.name = $name, current.description = $description " +
            "ON MATCH SET current.applicationId = $applicationId, current.hashIn = $hashIn, current.hashOut = $hashOut, " +
            "current.value = $value, current.name = $name, current.description = $description " +
            "WITH current " +
            "OPTIONAL MATCH (pred:LineageFlowEntity { applicationId: $applicationId, hashOut: $hashIn }) " +
            "MERGE (pred)-[:flow]->(current) " +
            "WITH current " +
            "OPTIONAL MATCH (succ:LineageFlowEntity { applicationId: $applicationId, hashIn: $hashOut }) " +
            "MERGE (current)-[:flow]->(succ)")
    void createAndLinkEntities(String flowId, String applicationId, String hashIn, String hashOut, String value,
                               String name, String description);

}