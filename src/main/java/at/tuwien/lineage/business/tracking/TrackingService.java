package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageNode;
import at.tuwien.lineage.persistence.TrackingRepository;
import at.tuwien.lineage.persistence.entities.LineageNodeEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;
    private final ObjectMapper objectMapper;

    public void persist(LineageNode lineageNode) {
        LineageNodeEntity current = trackingRepository.findById(lineageNode.id()) //
                .orElse(new LineageNodeEntity(lineageNode.id()));
        LineageNodeEntity successor = null;
        if (Objects.nonNull(lineageNode.reference())) {
            successor = trackingRepository.findById(lineageNode.reference()) //
                    .orElse(new LineageNodeEntity(lineageNode.reference()));
        }

        current.setName(lineageNode.name());
        current.setAdditionalInformation(serializeAdditionalInformation(lineageNode));
        current.setReference(successor);

        trackingRepository.save(current);
    }

    private String serializeAdditionalInformation(LineageNode lineageNode) {
        try {
            return objectMapper.writeValueAsString(lineageNode.additionalInformation());
        } catch (JsonProcessingException ignored) {}

        return null;
    }
}
