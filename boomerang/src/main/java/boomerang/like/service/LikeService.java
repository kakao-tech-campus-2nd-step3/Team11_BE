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
        Board board = getBoardOrThrow(boardId);

        //로그인 여부
        boolean isUserLoggedIn = principalDetails != null;
        Member loginMember = isUserLoggedIn ? getMemberOrThrow(principalDetails.getMemberEmail()) : null;

        Page<Like> likes = likeRepository.findAllByBoardIdAndIsDeletedFalse(board.getId(), pageable);

        List<LikeResponseDto> likeResponseDtos = likes.getContent().stream()
            .map(like -> new LikeResponseDto(like, like.getMember().equals(loginMember)))
            .collect(Collectors.toList());

        return new PageImpl<>(likeResponseDtos, pageable, likes.getTotalElements());
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
