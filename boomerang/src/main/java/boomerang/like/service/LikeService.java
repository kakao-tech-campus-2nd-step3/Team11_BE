package boomerang.like.service;

import boomerang.like.domain.LikeDomain;
import boomerang.like.exception.DuplicateLikeException;
import boomerang.like.exception.LikeNotFoundException;
import boomerang.like.repository.LikeRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteLikeDomain(String email, Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFoundException();
        }

        Member member = memberRepository.findByEmail(email);

        LikeDomain like = likeRepository.findByMemberIdAndBoardId(member.getId(), boardId)
            .orElseThrow(() -> new LikeNotFoundException());

        likeRepository.delete(like);
    }

    private void validateTemplateDomainExists(Long id) {
        if (!likeRepository.existsById(id)) {
            throw new LikeNotFoundException();
        }
    }
}
