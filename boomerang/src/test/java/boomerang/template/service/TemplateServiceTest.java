package boomerang.template.service;

import boomerang.template.domain.TemplateDomain;
import boomerang.template.exception.TemplateNotFoundException;
import boomerang.template.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService;

    private TemplateDomain templateDomain;

    @BeforeEach
    void setUp() {
        templateDomain = new TemplateDomain();
        templateDomain.setId(1L);
    }

    @Test
    void testGetAllTemplateDomains() {
        // given
        given(templateRepository.findAll()).willReturn(List.of(templateDomain));

        // when
        List<TemplateDomain> result = templateService.getAllTemplateDomains();

        // then
        assertThat(result).containsExactly(templateDomain);
        then(templateRepository).should(times(1)).findAll();
    }

    @Test
    void testGetTemplateDomainById() {
        // given
        given(templateRepository.findById(1L)).willReturn(Optional.of(templateDomain));

        // when
        TemplateDomain result = templateService.getTemplateDomainById(1L);

        // then
        assertThat(result).isEqualTo(templateDomain);
        then(templateRepository).should(times(1)).findById(1L);
    }

    @Test
    void testGetTemplateDomainById_NotFound() {
        // given
        given(templateRepository.findById(1L)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> templateService.getTemplateDomainById(1L))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void testCreateTemplateDomain() {
        // given
        given(templateRepository.save(any(TemplateDomain.class))).willReturn(templateDomain);

        // when
        TemplateDomain result = templateService.createTemplateDomain(templateDomain);

        // then
        assertThat(result).isEqualTo(templateDomain);
        then(templateRepository).should(times(1)).save(templateDomain);
    }

    @Test
    void testUpdateTemplateDomain() {
        // given
        given(templateRepository.existsById(1L)).willReturn(true);
        given(templateRepository.save(any(TemplateDomain.class))).willReturn(templateDomain);

        // when
        TemplateDomain result = templateService.updateTemplateDomain(templateDomain);

        // then
        assertThat(result).isEqualTo(templateDomain);
        then(templateRepository).should(times(1)).existsById(1L);
        then(templateRepository).should(times(1)).save(templateDomain);
    }

    @Test
    void testUpdateTemplateDomain_NotFound() {
        // given
        given(templateRepository.existsById(1L)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> templateService.updateTemplateDomain(templateDomain))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void testDeleteTemplateDomain() {
        // given
        given(templateRepository.existsById(1L)).willReturn(true);

        // when
        templateService.deleteTemplateDomain(1L);

        // then
        then(templateRepository).should(times(1)).existsById(1L);
        then(templateRepository).should(times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTemplateDomain_NotFound() {
        // given
        given(templateRepository.existsById(1L)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> templateService.deleteTemplateDomain(1L))
                .isInstanceOf(TemplateNotFoundException.class);
    }
}