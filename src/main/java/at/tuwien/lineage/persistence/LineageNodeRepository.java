package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LineageNodeRepository extends Neo4jRepository<LineageNodeEntity, String> {

    @Query("MATCH (src:LineageNodeEntity { nodeId: $srcNodeId }) " +
            "MATCH (dest:LineageNodeEntity { nodeId: $destNodeId }) " +
            "MERGE (src)-[:sendsTo]->(dest)")
    void createRelationship(String srcNodeId, String destNodeId);
}
