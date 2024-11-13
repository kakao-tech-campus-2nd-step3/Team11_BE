package boomerang.consultation.scheduler;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.consultation.service.ConsultationService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationStatusScheduler {
    private final ConsultationRepository consultationRepository;
    private final ConsultationService consultationService;

    public ConsultationStatusScheduler(ConsultationRepository consultationRepository, ConsultationService consultationService) {
        this.consultationRepository = consultationRepository;
        this.consultationService = consultationService;
    }

    // 매시간 정각마다 실행
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void updateConsultationStatus() {
        List<Consultation> consultations = consultationRepository.findAll();

        consultationService.updateConsultationStatus(consultations);
    }
}
