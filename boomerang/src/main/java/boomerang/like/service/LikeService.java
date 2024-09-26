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

    public List<LikeDomain> getAllLikeDomains() {
        return likeRepository.findAll();
    }

    public List<LikeDomain> getLikeDomainsByBoardId(Long boardId) {
        List<LikeDomain> likes = likeRepository.findByBoardId(boardId);
        if (likes.isEmpty()) {
            throw new LikeNotFoundException();
        }
        return likes;
    }

    public LikeDomain createLikeDomain(LikeDomain likeDomain) {
        return likeRepository.save(likeDomain);
    }

    public LikeDomain updateLikeDomain(LikeDomain likeDomain) {
        validateTemplateDomainExists(likeDomain.getId());
        return likeRepository.save(likeDomain);
    }

    public void deleteLikeDomain(Long id) {
        validateTemplateDomainExists(id);
        likeRepository.deleteById(id);
    }

    private void validateTemplateDomainExists(Long id) {
        if (!likeRepository.existsById(id)) {
            throw new LikeNotFoundException();
        }
    }
}
