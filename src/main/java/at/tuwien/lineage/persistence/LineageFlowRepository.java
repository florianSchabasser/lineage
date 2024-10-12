package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface LineageFlowRepository extends Neo4jRepository<LineageFlowEntity, String> {

    @Query("UNWIND $props AS item " +
            "MERGE (current:LineageFlow { flowId: item.flowId }) " +
            "SET current.applicationId = item.appId, " +
            "    current.hashIn = item.hashIn, " +
            "    current.hashOut = item.hashOut, " +
            "    current.value = item.value, " +
            "    current.name = item.name, " +
            "    current.description = item.description " +
            "WITH current, item " +
            "MATCH (pred:LineageFlow { applicationId: item.appId, hashOut: item.hashIn }) " +
            "MERGE (pred)-[:flow]->(current) " +
            "WITH current, item " +
            "MATCH (succ:LineageFlow { applicationId: item.appId, hashIn: item.hashOut }) " +
            "MERGE (current)-[:flow]->(succ)")
    void persistAndLinkMultiple(List<Map<String, String>> props);

}
