package boomerang.like.service;

import boomerang.like.domain.LikeDomain;
import boomerang.like.exception.DuplicateLikeException;
import boomerang.like.exception.LikeNotFoundException;
import boomerang.like.repository.LikeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<LikeDomain> getAllLikeDomains() {
        return likeRepository.findAll();
    }

    public List<LikeDomain> getLikeDomainsByBoardId(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFoundException();
        }

        List<LikeDomain> likes = likeRepository.findByBoardId(boardId);

        if (likes.isEmpty()) {
            throw new LikeNotFoundException();
        }

        return likes;
    }

    public LikeDomain createLikeDomain(String email, Long likeRequestDto) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFoundException();
        }

        Member member = memberRepository.findByEmail(email);

        if (likeRepository.existsByMemberIdAndBoardId(member.getID(), boardId)) {
            throw new DuplicateLikeException();
        }

        return likeRepository.save(likeRequestDto.toLikeDomain(member));
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
