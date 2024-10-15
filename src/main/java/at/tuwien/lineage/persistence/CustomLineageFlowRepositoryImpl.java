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
            String query =
                    "CALL apoc.periodic.iterate(" +
                            "  \"MATCH (current:LineageFlow {applicationId: $applicationId}) RETURN current\"," +
                            "  \"MATCH (successor:LineageFlow {applicationId: $applicationId, hashIn: current.hashOut})" +
                            "   MERGE (current)-[:flow]->(successor)\"," +
                            "  {batchSize:10000, params: {applicationId: $applicationId}}" +
                            ")";

            session.run(query, Values.parameters("applicationId", applicationId));
        }
    }
}
