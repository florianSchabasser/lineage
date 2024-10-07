package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.LineageNodeLink;
import at.tuwien.lineage.dto.graph.LineageNodeRegistration;
import at.tuwien.lineage.persistence.LineageNodeRepository;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineageNodeService {

    private final LineageNodeRepository lineageNodeRepository;

    public void persist(LineageNodeRegistration node) {
        LineageNodeEntity toSave = new LineageNodeEntity();
        toSave.setNodeId(node.nodeId());
        toSave.setApplicationId(node.nodeId().split("#")[0]);
        toSave.setName(node.name());
        toSave.setDescription(node.description());

        lineageNodeRepository.save(toSave);
        log.info("Persisted lineage node (nodeId: {})", node.nodeId());
    }

    public void persist(LineageNodeLink node) {
        lineageNodeRepository.createRelationship(node.srcNodeId(), node.destNodeId());
        log.info("Established node link (srcNodeId: {}, destNodeId: {})", node.srcNodeId(), node.destNodeId());
    }
}
