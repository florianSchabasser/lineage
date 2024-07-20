package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.dto.graph.Operation;
import at.tuwien.lineage.presentation.DataOperation;
import at.tuwien.lineage.presentation.ExecutionPlan;
import at.tuwien.lineage.presentation.ReadOperation;
import at.tuwien.lineage.presentation.WriteOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransformationService {

    private final ObjectMapper objectMapper;

    public List<Operation> transform(ExecutionPlan executionPlan) {
        List<Operation> operations = new ArrayList<>();
        DescriptionResolver descriptionResolver = new DescriptionResolver(executionPlan, objectMapper);

        operations.addAll(executionPlan.getOperations().getOther().stream().map(d -> transform(d, descriptionResolver.resolve(d))).toList());
        operations.add(transform(executionPlan.getOperations().getWrite()));
        operations.addAll(executionPlan.getOperations().getReads().stream().flatMap(this::transform).toList());

        return operations;
    }

    private Operation transform(DataOperation dataOperation, String description) {
        return new Operation(dataOperation.getId(), dataOperation.getName(), description, dataOperation.getChildIds(), null, false);
    }

    private Stream<Operation> transform(ReadOperation readOperation) {
        return readOperation.getInputSources().stream() //
                .map(x -> new Operation(readOperation.getId(), "Read", null, List.of(), x, false));
    }

    private Operation transform(WriteOperation writeOperation) {
        return new Operation(writeOperation.getId(), "Write", null, //
                writeOperation.getChildIds(), writeOperation.getOutputSource(), true);
    }

}
