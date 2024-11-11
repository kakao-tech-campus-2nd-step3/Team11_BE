package boomerang.consultation.task;

import boomerang.consultation.domain.Consultation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConsultationItemProcessor implements ItemProcessor<Consultation, Consultation> {

    @Override
    public Consultation process(Consultation consultation) throws Exception {
        consultation.start();
        return consultation;
    }
}
