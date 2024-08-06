package at.tuwien.lineage.dto.tracking;

public record LineageFlow(String nodeId, String hashIn, String hashOut, String value) {}