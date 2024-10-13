package at.tuwien.lineage.business.tracking;

import at.tuwien.lineage.dto.tracking.LineageFlow;
import at.tuwien.lineage.persistence.LineageFlowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineageFlowService {

    private final LineageFlowRepository flowRepository;

    public void persist(List<LineageFlow> flow) {
        this.flowRepository.saveAll(flow.stream().map(LineageFlow::getAsMap).toList());
    }

}
