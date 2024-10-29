package boomerang.template.service;

import boomerang.template.domain.TemplateDomain;
import boomerang.template.exception.TemplateNotFoundException;
import boomerang.template.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<TemplateDomain> getAllTemplateDomains() {
        return templateRepository.findAll();
    }

    public TemplateDomain getTemplateDomainById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(TemplateNotFoundException::new);
    }

    public TemplateDomain createTemplateDomain(TemplateDomain templateDomain) {
        return templateRepository.save(templateDomain);
    }

    public TemplateDomain updateTemplateDomain(TemplateDomain templateDomain) {
        validateTemplateDomainExists(templateDomain.getId());
        return templateRepository.save(templateDomain);
    }

    public void deleteTemplateDomain(Long id) {
        validateTemplateDomainExists(id);
        templateRepository.deleteById(id);
    }

    private void validateTemplateDomainExists(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new TemplateNotFoundException();
        }
    }
}
