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
import java.util.Set;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;
    private final GraphService graphService;

    public void persist(LineageFlow lineageFlow) {
        LineageFlowEntity current = createLineageFlowEntity(lineageFlow);

        Set<LineageFlowEntity> predecessor = trackingRepository.findAllByApplicationIdAndHashOut(current.getApplicationId(), current.getHashIn());
        predecessor.forEach(p -> p.getSuccessor().add(current));

        Set<LineageFlowEntity> successor = trackingRepository.findAllByApplicationIdAndHashIn(current.getApplicationId(), current.getHashOut());
        current.setSuccessor(successor);

        List<LineageFlowEntity> toSave = new ArrayList<>(successor);
        toSave.addAll(predecessor);
        toSave.add(current);
        trackingRepository.saveAll(toSave);
    }

    private LineageFlowEntity createLineageFlowEntity(LineageFlow lineageFlow) {
        String[] ids = lineageFlow.flowId().split("#");
        String nodeId = format("%s#%s", ids[0], ids[1]);
        String applicationId = ids[0];
        LineageNodeEntity lineageNodeEntity = graphService.getLineageNodeCached(nodeId);

        LineageFlowEntity lineageFlowEntity = new LineageFlowEntity();
        lineageFlowEntity.setApplicationId(applicationId);
        lineageFlowEntity.setNodeId(nodeId);
        lineageFlowEntity.setFlowId(lineageFlow.flowId());
        lineageFlowEntity.setHashIn(lineageFlow.hashIn());
        lineageFlowEntity.setHashOut(lineageFlow.hashOut());
        lineageFlowEntity.setValue(lineageFlow.value());

        lineageFlowEntity.setName(lineageNodeEntity.getName());
        lineageFlowEntity.setDescription(lineageNodeEntity.getDescription());

        return lineageFlowEntity;
    }
}
