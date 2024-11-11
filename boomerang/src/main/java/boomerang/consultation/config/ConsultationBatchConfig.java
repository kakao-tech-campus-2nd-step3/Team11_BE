package boomerang.consultation.config;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.task.ConsultationItemProcessor;
import boomerang.consultation.task.ConsultationItemWriter;
import boomerang.consultation.task.PendingConsultationItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ConsultationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ConsultationBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job updateConsultationStatusJob() {
        // JobBuilder 생성
        JobBuilder jobBuilder = new JobBuilder("updateConsultationStatusJob", jobRepository);
        return jobBuilder
                .start(updateConsultationStatusStep())
                .build();
    }

    @Bean
    public Step updateConsultationStatusStep() {
        // StepBuilder 생성
        StepBuilder stepBuilder = new StepBuilder("updateConsultationStatusStep", jobRepository);
        return stepBuilder
                .<Consultation, Consultation>chunk(10, transactionManager)
                .reader(consultationReader())
                .processor(consultationProcessor())
                .writer(consultationWriter())
                .build();
    }

    @Bean
    public PendingConsultationItemReader consultationReader() {
        // Reader 구현체 생성 및 반환
        return new PendingConsultationItemReader();
    }

    @Bean
    public ConsultationItemProcessor consultationProcessor() {
        // Processor 구현체 생성 및 반환
        return new ConsultationItemProcessor();
    }

    @Bean
    public ConsultationItemWriter consultationWriter() {
        // Writer 구현체 생성 및 반환
        return new ConsultationItemWriter();
    }
}
