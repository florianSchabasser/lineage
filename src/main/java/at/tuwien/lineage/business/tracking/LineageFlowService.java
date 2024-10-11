package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageFlow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineageFlowService {

    private final Driver driver;

    public void persist(List<LineageFlow> lineageFlows) {
        try (Session session = driver.session()){
            List<Map<String, String>> listOfObjects = lineageFlows.stream().map(LineageFlow::getAsMap).toList();
            session.executeWriteWithoutResult(tx -> tx.run(
                    "UNWIND $props AS item " +
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
                            "MERGE (current)-[:flow]->(succ)",
                    Map.of("props", listOfObjects)).consume());
        }
    }

}
