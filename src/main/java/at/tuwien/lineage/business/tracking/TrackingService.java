package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.business.graph.GraphService;
import at.tuwien.lineage.dto.tracking.LineageFlow;
import at.tuwien.lineage.persistence.TrackingRepository;
import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;
    private final GraphService graphService;

    public void persist(LineageFlow lineageFlow) {
        LineageNodeEntity lineageNodeEntity = graphService.getLineageNodeCached(lineageFlow.nodeId());
        LineageFlowEntity current = createLineageFlowEntity(lineageNodeEntity, lineageFlow);
        List<LineageFlowEntity> predecessor = new ArrayList<>();

        if (!lineageNodeEntity.isFirstElement()) {
            predecessor.addAll(trackingRepository.findAllByHashOut(lineageFlow.hashIn()));
            predecessor.forEach(p -> p.getSuccessor().add(current));
        }

        List<LineageFlowEntity> toSave = new ArrayList<>(predecessor);
        toSave.add(current);
        trackingRepository.saveAll(toSave);
    }

    private LineageFlowEntity createLineageFlowEntity(LineageNodeEntity lineageNodeEntity, LineageFlow lineageFlow) {

        LineageFlowEntity lineageFlowEntity = new LineageFlowEntity();
        lineageFlowEntity.setNodeId(lineageFlow.nodeId());
        lineageFlowEntity.setId(randomUUID().toString());
        lineageFlowEntity.setHashIn(lineageFlow.hashIn());
        lineageFlowEntity.setHashOut(lineageFlow.hashOut());
        lineageFlowEntity.setValue(lineageFlow.value());

        lineageFlowEntity.setName(lineageNodeEntity.getName());
        lineageFlowEntity.setDescription(lineageNodeEntity.getDescription());

        return lineageFlowEntity;
    }
}
