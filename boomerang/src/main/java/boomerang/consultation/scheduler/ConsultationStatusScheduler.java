package boomerang.consultation.scheduler;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.consultation.service.ConsultationService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        updateConsultationStatus(consultations);
    }

    private void updateConsultationStatus(List<Consultation> consultations) {

        // 현재 시간을 가져와 조회된 consultations의 상태를 확인 후 업데이트
        LocalDateTime now = LocalDateTime.now();
        consultations.forEach(consultation -> {
            if (consultation.getConsultationDateTime().isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.RECEIVED) {
                consultation.complete(); // 상태를 FINISHED으로 변경, 상담 시간이 되어도 확정되지 않으면 확정전 -> 상담완료
            }
            if (consultation.getConsultationDateTime().isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.PENDING) {
                consultation.start(); // 상태를 ONGOING으로 변경, 확정된 상담이 신청 시간이 지나면 진행전 -> 진행중
            }
            if (consultation.getConsultationDateTime().plusHours(1).plusMinutes(30).isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.ONGOING) {
                consultation.complete(); // 상태를 FINISHED으로 변경, 진행중인 상담이 상담시간보다 1시간 30분이 지나면 진행중 -> 상담완료
            }
        });
    }
}
