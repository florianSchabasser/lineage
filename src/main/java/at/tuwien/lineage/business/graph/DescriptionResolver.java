package at.tuwien.lineage.business.graph;

import at.tuwien.lineage.presentation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class DescriptionResolver {

    private final Map<String, Object> expressions = new HashMap<>();
    private final ObjectMapper objectMapper;

    public DescriptionResolver(ExecutionPlan executionPlan, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        executionPlan.getAttributes().forEach(a -> expressions.put(a.getId(), a));
        executionPlan.getExpressions().getConstants().forEach(c -> expressions.put(c.getId(), c));
        executionPlan.getExpressions().getFunctions().forEach(f -> expressions.put(f.getId(), f));
    }

    public String resolve(DataOperation dataOperation) {
        JsonNode node;
        try {
            node = objectMapper.readTree(objectMapper.writeValueAsString(dataOperation.getParams()));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        return node.findValuesAsText("__exprId").stream().map(expressions::get)
                .map(this::getDescription).collect(Collectors.joining(" "));
    }

    private String getDescription(Attribute attribute) {
        return format("%s", attribute.getName());
    }

    private String getDescription(Literal literal) {
        return format("%s", literal.getValue());
    }

    private String getDescription(FunctionalExpression expression) {
        return format("%s %s", expression.getExtra().get("simpleClassName"), expression.getChildRefs().stream()
                .map(r -> Objects.requireNonNullElse(r.getAttrId(), r.getExprId())).map(expressions::get)
                .map(this::getDescription).collect(Collectors.joining(" ")));
    }

    private String getDescription(Object object) {
        if (object instanceof Attribute attribute) {
            return getDescription(attribute);
        } else if (object instanceof Literal literal) {
            return getDescription(literal);
        } else if (object instanceof FunctionalExpression expression) {
            return getDescription(expression);
        }
        return null;
    }
}
