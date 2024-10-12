package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageFlow;
import at.tuwien.lineage.persistence.LineageFlowRepository;
import at.tuwien.lineage.persistence.entities.LineageFlowEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineageFlowService {

    private final LineageFlowRepository flowRepository;

    /*
    public void persist(List<LineageFlow> flow) {
        List<LineageFlowEntity> lineageFlowEntities = flow.stream().map(f -> new LineageFlowEntity(
                f.flowId(), f.appId(), f.hashIn(), f.hashOut(), Set.of(), f.value(), f.name(), f.description()))
                .toList();
        this.flowRepository.persist(lineageFlowEntities);
    }
     */

    public void persist(List<LineageFlow> flow) {
        List<Map<String, String>> lineageFlowEntities = flow.stream()
                .map(f -> new LineageFlowEntity(f.flowId(), f.appId(), f.hashIn(), f.hashOut(), Set.of(), f.value(),
                        f.name(), f.description()))
                .map(LineageFlowEntity::getAsMap).toList();

        this.flowRepository.persistAndLinkMultiple(lineageFlowEntities);
    }
}
