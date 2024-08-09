package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TrackingRepository extends Neo4jRepository<LineageFlowEntity, String> {

    Set<LineageFlowEntity> findAllByApplicationIdAndHashOut(String applicationId, String hash);

    @Query("MATCH (n:LineageNodeEntity) WHERE n.hashIn = $hash and n.applicationId = $applicationId RETURN n")
    Set<LineageFlowEntity> findAllByApplicationIdAndHashIn(String applicationId, String hash);

}