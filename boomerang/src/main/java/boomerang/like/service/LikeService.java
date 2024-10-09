package boomerang.like.service;

import boomerang.board.domain.Board;
import boomerang.board.repository.BoardRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.like.domain.Like;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.repository.LikeRepository;
import boomerang.member.domain.Member;
import boomerang.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Page<LikeResponseDto> getLikesByBoardId(PrincipalDetails principalDetails, Long boardId, Pageable pageable) {
        Member loginMember = getMemberOrThrow(principalDetails.getMemberEmail());
        Board board = getBoardOrThrow(boardId);

        Page<Like> likes = likeRepository.findAllByBoardIdAndIsDeletedFalse(board.getId(), pageable);

        List<LikeResponseDto> likeResponseDtos = likes.getContent().stream()
            .map(like -> new LikeResponseDto(like, like.getMember().equals(loginMember)))
            .collect(Collectors.toList());

        return new PageImpl<>(likeResponseDtos, pageable, likes.getTotalElements());
    }

    @Transactional
    public LikeResponseDto createLike(PrincipalDetails principalDetails, Long boardId) {
        Member loginMember = getMemberOrThrow(principalDetails.getMemberEmail());
        Board board = getBoardOrThrow(boardId);

        // 이미 좋아요를 눌렀는지 확인
        if (likeRepository.existsByMemberAndBoardAndIsDeletedFalse(loginMember, board)) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        Like like = new Like(loginMember, board);
        Like savedLike = likeRepository.save(like);

        return new LikeResponseDto(savedLike, true);
    }

    @Transactional
    public void deleteLike(PrincipalDetails principalDetails, Long boardId) {
        Member loginMember = getMemberOrThrow(principalDetails.getMemberEmail());
        Board board = getBoardOrThrow(boardId);

        Like like = likeRepository.findByMemberAndBoardAndIsDeletedFalse(loginMember, board)
            .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        like.delete();
        likeRepository.save(like);
    }

    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    private Board getBoardOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND_ERROR));
    }
}
