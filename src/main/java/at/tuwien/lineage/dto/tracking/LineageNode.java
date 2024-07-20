package at.tuwien.lineage.dto.tracking;

import java.util.Map;

public record LineageNode(String id, String name, String reference, Map<String, String> additionalInformation) {

}
