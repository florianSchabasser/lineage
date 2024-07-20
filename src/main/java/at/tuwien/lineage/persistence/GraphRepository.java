package at.tuwien.lineage.persistence;

import at.tuwien.lineage.persistence.entities.OperationEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends Neo4jRepository<OperationEntity, String> {

}
