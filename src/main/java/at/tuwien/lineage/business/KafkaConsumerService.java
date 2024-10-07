package at.tuwien.lineage.business;

import at.tuwien.lineage.business.graph.LineageNodeService;
import at.tuwien.lineage.business.tracking.LineageFlowService;
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

    private final LineageNodeService lineageNodeService;
    private final LineageFlowService lineageFlowService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "lineage-node", groupId = "lineage-backend", concurrency = "1")
    public void listenLineageNodeLink(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String entityType = new String(record.headers().lastHeader("type").value());
            log.info("Received new {}", entityType);

            switch (entityType) {
                case "LineageNodeLink":
                    LineageNodeLink nodeLink = objectMapper.readValue(record.value(), LineageNodeLink.class);
                    lineageNodeService.persist(nodeLink);
                    break;
                case "LineageNodeRegistration":
                    LineageNodeRegistration nodeRegistration = objectMapper.readValue(record.value(), LineageNodeRegistration.class);
                    lineageNodeService.persist(nodeRegistration);
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

    @KafkaListener(topics = "lineage-flow", groupId = "lineage-backend", concurrency = "10")
    public void listenLineageFlow(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        LineageFlow lineageFlow;
        String[] values = record.value().split(",");

        if (values.length == 3) {
            lineageFlow = new LineageFlow(values[0], values[1], values[2]);
        } else if (values.length == 6) {
            lineageFlow = new LineageFlow(values[0], values[1], values[2], values[3], values[4], values[5]);
        } else {
            log.error("Unsupported message type {}", record.value());
            return;
        }

        lineageFlowService.persist(lineageFlow);
        acknowledgment.acknowledge();
    }
}