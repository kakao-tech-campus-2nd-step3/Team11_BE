package boomerang.consultation.task;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.repository.ConsultationRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PendingConsultationItemReader implements ItemReader<Consultation>, ItemStream {

    @Autowired
    private ConsultationRepository consultationRepository;

    private List<Consultation> consultations;
    private int currentIndex = 0;

    @Override
    public Consultation read() {
        // 리스트 내 모든 상담을 순차적으로 반환
        if (consultations != null && currentIndex < consultations.size()) {
            return consultations.get(currentIndex++);
        }
        // 모든 상담을 읽었으면 null 반환
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        LocalDateTime now = LocalDateTime.now();
        this.consultations = consultationRepository.findPendingConsultationsForTime(now);
        this.currentIndex = 0;
    }

    @Override
    public void update(ExecutionContext executionContext) {
        // 업데이트할 내용이 없으면 비워둠
    }

    @Override
    public void close() {
        this.consultations = null;
        this.currentIndex = 0;
    }
}


