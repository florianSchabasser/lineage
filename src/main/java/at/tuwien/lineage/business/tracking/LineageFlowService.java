package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageFlow;
import at.tuwien.lineage.persistence.LineageFlowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineageFlowService {

    private final LineageFlowRepository lineageFlowRepository;

    public void persist(LineageFlow lineageFlow) {
        String applicationId = lineageFlow.flowId().split("#")[0];

        lineageFlowRepository.createAndLinkEntities(lineageFlow.flowId(), applicationId, lineageFlow.hashIn(),
                lineageFlow.hashOut(), requireNonNullElse(lineageFlow.value(), "NA"),
                requireNonNullElse(lineageFlow.name(), "NA"),
                requireNonNullElse(lineageFlow.description(), "NA"));
        log.info("Persisted lineage flow (flowId: {})", lineageFlow.flowId());
    }
}
