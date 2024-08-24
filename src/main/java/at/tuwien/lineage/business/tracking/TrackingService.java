package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageFlow;
import at.tuwien.lineage.persistence.TrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public void persist(LineageFlow lineageFlow) {
        String[] ids = lineageFlow.flowId().split("#");
        String nodeId = format("%s#%s", ids[0], ids[1]);
        String applicationId = ids[0];

        trackingRepository.createAndLinkEntities(lineageFlow.flowId(), nodeId, applicationId, lineageFlow.hashIn(),
                lineageFlow.hashOut(), requireNonNullElse(lineageFlow.value(), "NA"),
                requireNonNullElse(lineageFlow.name(), "NA"),
                requireNonNullElse(lineageFlow.description(), "NA"));
        log.info("Persisted lineage flow (flowId: {})", lineageFlow.flowId());
    }
}
