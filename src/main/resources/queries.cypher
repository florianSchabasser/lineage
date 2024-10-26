CREATE INDEX applicationId_hashIn FOR (n:LineageFlow) ON (n.applicationId, n.hashIn);
CREATE INDEX flow_id FOR (n:LineageFlow) ON (n.flowId);

DROP INDEX applicationId_hashIn;
DROP INDEX flow_id;

// Determine read rows of previous partitions
MATCH (n:LineageFlow)
WHERE n.applicationId = 'local-1724487096906' and n.hashIn =~ 'read#0#.*'
WITH n, toInteger(SPLIT(n.hashIn, '#')[2]) AS numValue
ORDER BY numValue DESC
RETURN n, numValue
LIMIT 1

// Forward tracing for specific row
MATCH (start:LineageFlow)
WHERE start.applicationId = 'local-1728591828955' AND start.hashIn = 'read#0#1'
MATCH path = (start)-[*0..]->(flow)
RETURN path
LIMIT 1000

// Backward tracing for a specific row
MATCH (end:LineageFlow)
WHERE end.applicationId = 'local-1728630860520' AND end.hashOut = 'write#0#119'
MATCH path = (end)<-[*0..]-(flow)
RETURN path
LIMIT 1000

CALL apoc.periodic.iterate(
  "MATCH (current:LineageFlow {applicationId: $applicationId}) RETURN current",
  "MATCH (successor:LineageFlow {applicationId: $applicationId, hashIn: current.hashOut})
   MERGE (current)-[:flow]->(successor)",
  {batchSize:10000, params: {applicationId: $applicationId}}
)

CALL apoc.periodic.iterate(
  "MATCH (current:LineageFlow {applicationId: $applicationId}) RETURN current",
  "MATCH (meta:LineageNode {applicationId: $applicationId, nodeId: current.nodeId})
   SET current.name = meta.name, current.description = meta.description",
  {batchSize:10000, parallel: true, params: {applicationId: $applicationId}}
)