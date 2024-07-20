package at.tuwien.lineage.dto.graph;

import java.util.List;

public record Operation(String id, String name, String description, List<String> references, String source, boolean isRoot) {

}