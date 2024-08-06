package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends Neo4jRepository<LineageFlowEntity, String> {

    List<LineageFlowEntity> findAllByHashOut(String hash);

    @Query("MATCH (n:LineageNodeEntity) WHERE n.hashIn = $hash RETURN n")
    Optional<LineageFlowEntity> findByHashIn(String hash);

}