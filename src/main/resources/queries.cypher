MATCH (n) DETACH DELETE n

// To achieve high availability, a database should be created with multiple primaries.
// If high availability is not required, then a database may be created with a single primary for minimum write latency.
// https://neo4j.com/docs/operations-manual/current/clustering/introduction/
CREATE DATABASE lineage TOPOLOGY 1 PRIMARIES 2 SECONDARIES;

CREATE INDEX applicationId_hashIn FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashIn);
CREATE INDEX applicationId_hashOut FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashOut);