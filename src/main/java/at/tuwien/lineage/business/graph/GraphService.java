package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.LineageNodeLink;
import at.tuwien.lineage.dto.graph.LineageNodeRegistration;
import at.tuwien.lineage.persistence.GraphRepository;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphRepository graphRepository;

    public void persist(LineageNodeRegistration node) {
        LineageNodeEntity toSave = new LineageNodeEntity();
        toSave.setNodeId(node.nodeId());
        toSave.setName(node.name());
        toSave.setDescription(node.description());

        graphRepository.save(toSave);
        log.info("Persisted lineage node (nodeId: {})", node.nodeId());
    }

    public void persist(LineageNodeLink node) {
        graphRepository.createRelationship(node.srcNodeId(), node.destNodeId());
        log.info("Established node link (srcNodeId: {}, destNodeId: {})", node.srcNodeId(), node.destNodeId());
    }
}
