CREATE INDEX applicationId_hashIn FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashIn)
CREATE INDEX applicationId_hashOut FOR (n:LineageFlowEntity) ON (n.applicationId, n.hashOut)