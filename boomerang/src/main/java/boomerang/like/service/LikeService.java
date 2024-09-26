package boomerang.like.service;

import boomerang.like.domain.LikeDomain;
import boomerang.like.exception.LikeNotFoundException;
import boomerang.like.repository.LikeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<LikeDomain> getAllTemplateDomains() {
        return likeRepository.findAll();
    }

    public LikeDomain getTemplateDomainById(Long id) {
        return likeRepository.findById(id)
                .orElseThrow(LikeNotFoundException::new);
    }

    public LikeDomain createTemplateDomain(LikeDomain likeDomain) {
        return likeRepository.save(likeDomain);
    }

    public LikeDomain updateTemplateDomain(LikeDomain likeDomain) {
        validateTemplateDomainExists(likeDomain.getId());
        return likeRepository.save(likeDomain);
    }

    public void deleteTemplateDomain(Long id) {
        validateTemplateDomainExists(id);
        likeRepository.deleteById(id);
    }

    private void validateTemplateDomainExists(Long id) {
        if (!likeRepository.existsById(id)) {
            throw new LikeNotFoundException();
        }
    }
}
