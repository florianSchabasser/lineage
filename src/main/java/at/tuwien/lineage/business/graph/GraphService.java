package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.Operation;
import at.tuwien.lineage.persistence.GraphRepository;
import at.tuwien.lineage.persistence.entities.OperationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final GraphRepository graphRepository;

    public void persistAsGraph(List<Operation> operations) {
        Operation root = operations.stream().filter(Operation::isRoot).findFirst() //
                .orElseThrow(() -> new IllegalStateException("Multiple root nodes"));

        OperationEntity rootEntity = createGraph(root, operations.stream() //
                .collect(Collectors.toMap(Operation::id, Function.identity())));
        graphRepository.save(rootEntity);
    }

    private OperationEntity createGraph(Operation operation, Map<String, Operation> operations) {
        return new OperationEntity(operation.id(), operation.name(), operation.description(), operation.references() //
                .stream().map(r -> createGraph(operations.get(r), operations)).toList(), operation.source());
    }

}
