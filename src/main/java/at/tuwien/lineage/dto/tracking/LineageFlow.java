package at.tuwien.lineage.dto.tracking;

import java.util.HashMap;
import java.util.Map;

public record LineageFlow(String flowId, String appId, String hashIn, String hashOut, String name,
                          String description, String value) {

    public LineageFlow(String flowId, String hashIn, String hashOut, String name, String description, String value) {
        this(flowId, flowId.split("#")[0], hashIn, hashOut, name, description, value);
    }

    public LineageFlow(String flowId, String hashIn, String hashOut) {
        this(flowId, flowId.split("#")[0], hashIn, hashOut, null, null, null);
    }

}