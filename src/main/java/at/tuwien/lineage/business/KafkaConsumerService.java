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

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final GraphService graphService;
    private final TrackingService trackingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "lineage-node-registration", groupId = "lineage-backend")
    public void listenLineageNodeRegistration(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            LineageNodeRegistration nodeRegistration = objectMapper.readValue(record.value(), LineageNodeRegistration.class);
            graphService.persist(nodeRegistration);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "lineage-node-link", groupId = "lineage-backend")
    public void listenLineageNodeLink(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            LineageNodeLink nodeLink = objectMapper.readValue(record.value(), LineageNodeLink.class);
            graphService.persist(nodeLink);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    @KafkaListener(topics = "lineage-flow", groupId = "lineage-backend")
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