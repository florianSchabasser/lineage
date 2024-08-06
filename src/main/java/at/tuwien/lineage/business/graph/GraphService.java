package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.LineageNodeLink;
import at.tuwien.lineage.dto.graph.LineageNodeRegistration;
import at.tuwien.lineage.persistence.GraphRepository;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphRepository graphRepository;

    public void persist(LineageNodeRegistration node) {

        LineageNodeEntity toSave = new LineageNodeEntity(node.nodeId(), node.name(), node.description(), null);
        graphRepository.save(toSave);
    }

    public void persist(LineageNodeLink node) {
        LineageNodeEntity srcEntity = graphRepository.findById(node.srcNodeId()).orElseThrow();
        LineageNodeEntity destEntity = graphRepository.findById(node.destNodeId()).orElseThrow();

        srcEntity.setSuccessor(destEntity);
        graphRepository.saveAll(List.of(srcEntity, destEntity));
    }

    @Cacheable("LineageNodeEntityCache")
    public LineageNodeEntity getLineageNodeCached(String id) {
        return graphRepository.findById(id).orElseThrow();
    }
}
