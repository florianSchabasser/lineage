package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.LineageNodeLink;
import at.tuwien.lineage.dto.graph.LineageNodeRegistration;
import at.tuwien.lineage.persistence.GraphRepository;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        graphRepository.save(srcEntity);
    }
}
