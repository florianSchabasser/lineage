package at.tuwien.lineage.business;

import at.tuwien.lineage.business.graph.GraphService;
import at.tuwien.lineage.business.tracking.TrackingService;
import at.tuwien.lineage.dto.graph.LineageNodeLink;
import at.tuwien.lineage.dto.graph.LineageNodeRegistration;
import at.tuwien.lineage.dto.tracking.LineageFlow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final GraphService graphService;
    private final TrackingService trackingService;
    private final ObjectMapper objectMapper;

    // Assuming sequential processing! Concurrency must be 1.
    @KafkaListener(topics = "lineage-node", groupId = "lineage-backend", concurrency = "1")
    public void listenLineageNodeLink(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String entityType = new String(record.headers().lastHeader("type").value());

            switch (entityType) {
                case "LineageNodeLink":
                    LineageNodeLink nodeLink = objectMapper.readValue(record.value(), LineageNodeLink.class);
                    graphService.persist(nodeLink);
                    break;
                case "LineageNodeRegistration":
                    LineageNodeRegistration nodeRegistration = objectMapper.readValue(record.value(), LineageNodeRegistration.class);
                    graphService.persist(nodeRegistration);
                    break;
                default:
                    throw new IllegalArgumentException(format("Unsupported data type %s", entityType));
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    // Higher concurrency -> Higher risk of race conditions in case of a retry
    @KafkaListener(topics = "lineage-flow", groupId = "lineage-backend", concurrency = "100")
    public void listenLineageFlow(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            LineageFlow lineageFlow = objectMapper.readValue(record.value(), LineageFlow.class);
            trackingService.persist(lineageFlow);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}