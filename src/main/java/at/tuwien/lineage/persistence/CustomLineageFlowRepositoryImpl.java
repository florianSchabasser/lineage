package at.tuwien.lineage.persistence;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomLineageFlowRepositoryImpl implements CustomLineageFlowRepository {

    private final Driver driver;

    @Override
    public void createFlowRelationships(String applicationId) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String query =
                        "CALL apoc.periodic.iterate(" +
                                "  \"MATCH (current {applicationId: $applicationId}) RETURN current\"," +
                                "  \"MATCH (successor {applicationId: $applicationId}) " +
                                "   WHERE current.hashOut = successor.hashIn " +
                                "   MERGE (current)-[:flow]->(successor) " +
                                "   WITH current " +
                                "   MATCH (predecessor {applicationId: $applicationId}) " +
                                "   WHERE predecessor.hashOut = current.hashIn " +
                                "   MERGE (predecessor)-[:flow]->(current)\"," +
                                "  {batchSize:10000, parallel:false, params: {applicationId: $applicationId}}" +
                                ")";
                tx.run(query, Values.parameters("applicationId", applicationId));
            });
        }
    }
}
