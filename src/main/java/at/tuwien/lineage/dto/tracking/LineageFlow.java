package at.tuwien.lineage.dto.tracking;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public record LineageFlow(String flowId, String applicationId, String nodeId, String hashIn, String hashOut, String value) {

    public LineageFlow(String flowId, String hashIn, String hashOut, String value) {
        this(flowId, flowId.split("#")[0], format("%s#%s", flowId.split("#")[0], flowId.split("#")[1]), 
                hashIn, hashOut, value);
    }

    public LineageFlow(String flowId, String hashIn, String hashOut) {
        this(flowId, flowId.split("#")[0], format("%s#%s", flowId.split("#")[0], flowId.split("#")[1]),
                hashIn, hashOut, null);    }

    public Map<String, String> getAsMap() {
        Map<String, String> map =  new HashMap<>();
        map.put("flowId", flowId);
        map.put("nodeId", nodeId);
        map.put("applicationId", applicationId);
        map.put("hashIn", hashIn);
        map.put("hashOut", hashOut);
        map.put("value", value);

        return map;
    }
}