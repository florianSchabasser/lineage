package at.tuwien.lineage.dto.tracking;

public record LineageFlow(String flowId, String hashIn, String hashOut, String name, String description, String value) {

    public LineageFlow(String flowId, String hashIn, String hashOut) {
        this(flowId, hashIn, hashOut, null, null, null);
    }

}