package at.tuwien.lineage.business;

import at.tuwien.lineage.business.graph.GraphService;
import at.tuwien.lineage.business.graph.TransformationService;
import at.tuwien.lineage.business.tracking.TrackingService;
import at.tuwien.lineage.dto.graph.Operation;
import at.tuwien.lineage.dto.tracking.LineageNode;
import at.tuwien.lineage.presentation.ExecutionPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final GraphService graphService;
    private final TransformationService transformationService;
    private final TrackingService trackingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "lineage-graph", groupId = "consumer-group-id")
    public void listenLineageGraph(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        String entityType = new String(record.headers().lastHeader("ABSA-Spline-Entity-Type").value());

        switch (entityType) {
            case "ExecutionPlan":
                ExecutionPlan executionPlan = objectMapper.readValue(record.value(), ExecutionPlan.class);
                List<Operation> operations = transformationService.transform(executionPlan);
                graphService.persistAsGraph(operations);
                acknowledgment.acknowledge();
                break;
            case "ExecutionEvent":
                acknowledgment.acknowledge();
                break;
            default:
                throw new IllegalArgumentException(format("Unsupported data type %s", entityType));
        }
    }

    @KafkaListener(topics = "lineage-flow", groupId = "consumer-group-id")
    public void listenLineageFlow(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws JsonProcessingException {
        LineageNode lineageNode = objectMapper.readValue(record.value(), LineageNode.class);
        trackingService.persist(lineageNode);
        acknowledgment.acknowledge();
    }
}