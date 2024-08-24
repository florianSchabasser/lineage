MATCH (n) DETACH DELETE n

// To achieve high availability, a database should be created with multiple primaries.
// If high availability is not required, then a database may be created with a single primary for minimum write latency.
// https://neo4j.com/docs/operations-manual/current/clustering/introduction/
CREATE DATABASE lineage TOPOLOGY 1 PRIMARIES 2 SECONDARIES;

CREATE INDEX applicationId_hashIn FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashIn);
CREATE INDEX applicationId_hashOut FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashOut);


// Determine read rows of previous partitions
MATCH (n:LineageFlowEntity)
WHERE n.applicationId = 'local-1724487096906' and n.hashIn =~ 'read#0#.*'
WITH n, toInteger(SPLIT(n.hashIn, '#')[2]) AS numValue
ORDER BY numValue DESC
RETURN n, numValue
LIMIT 1

// Forward tracing for specific row
MATCH (start:LineageFlowEntity)
WHERE start.applicationId = 'local-1724487096906' AND start.hashIn = 'read#0#1'
MATCH path = (start)-[*0..]-(flow)
RETURN path

// Backward tracing for a specific row
MATCH (end:LineageFlowEntity)
WHERE end.applicationId = 'local-1724487096906' AND end.hashOut = 'write#0#1'
MATCH path = (end)-[*0..]-(flow)
RETURN path