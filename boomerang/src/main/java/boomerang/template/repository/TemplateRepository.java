package boomerang.template.repository;

import boomerang.global.MyCrudRepository;
import boomerang.template.domain.TemplateDomain;

public interface TemplateRepository extends MyCrudRepository<TemplateDomain, Long> {
    boolean existsById(Long id);
}
