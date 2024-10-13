package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface LineageFlowRepository extends Neo4jRepository<LineageFlowEntity, String>, CustomLineageFlowRepository {

    @Query("UNWIND $serializedEntities AS item " +
            "MERGE (current:LineageFlow { flowId: item.flowId }) " +
            "SET current.applicationId = item.appId, " +
            "    current.hashIn = item.hashIn, " +
            "    current.hashOut = item.hashOut, " +
            "    current.value = item.value, " +
            "    current.name = item.name, " +
            "    current.description = item.description ")
    void saveAll(List<Map<String, String>> serializedEntities);

}
