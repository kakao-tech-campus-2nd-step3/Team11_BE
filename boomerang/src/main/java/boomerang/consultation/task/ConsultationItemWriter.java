package boomerang.consultation.task;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.repository.ConsultationRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultationItemWriter implements ItemWriter<Consultation> {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Override
    public void write(Chunk<? extends Consultation> consultations) throws Exception {
        consultationRepository.saveAll(consultations);
    }
}


