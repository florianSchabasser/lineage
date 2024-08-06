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
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final Pattern UUID_REGEX = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private final TrackingRepository trackingRepository;
    private final GraphService graphService;

    public void persist(LineageFlow lineageFlow) {
        LineageFlowEntity current = createLineageFlowEntity(lineageFlow);
        List<LineageFlowEntity> predecessor = UUID_REGEX.matcher(lineageFlow.hashIn()).matches() ?
                trackingRepository.findAllByHashOut(lineageFlow.hashIn()) : List.of();
        Optional<LineageFlowEntity> successor = UUID_REGEX.matcher(lineageFlow.hashOut()).matches() ?
                trackingRepository.findByHashIn(lineageFlow.hashOut()) : Optional.empty();

        successor.ifPresent(current::setSuccessor);
        predecessor.forEach(p -> p.setSuccessor(current));

        List<LineageFlowEntity> toSave = new ArrayList<>(predecessor);
        successor.ifPresent(toSave::add);
        toSave.add(current);

        trackingRepository.saveAll(toSave);
    }

    private LineageFlowEntity createLineageFlowEntity(LineageFlow lineageFlow) {
        LineageNodeEntity lineageNodeEntity = graphService.getLineageNodeCached(lineageFlow.nodeId());

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
