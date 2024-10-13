CREATE INDEX applicationId FOR (n:LineageFlow) ON (n.applicationId)
CREATE INDEX hashOut FOR (n:LineageFlow) ON (n.hashOut)
CREATE INDEX hashIn FOR (n:LineageFlow) ON (n.hashIn)
CREATE INDEX flowId FOR (n:LineageFlow) ON (n.flowId);

DROP INDEX applicationId_hashIn;
DROP INDEX applicationId_hashOut;
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
  "MATCH (current {applicationId: $applicationId}) RETURN current",
  "MATCH (successor {applicationId: $applicationId})
   WHERE current.hashOut = successor.hashIn
   MERGE (current)-[:flow]->(successor)
   WITH current
   MATCH (predecessor {applicationId: $applicationId})
   WHERE predecessor.hashOut = current.hashIn
   MERGE (predecessor)-[:flow]->(current)",
  {batchSize:10000, parallel:false, params: {applicationId: $applicationId}}
)