package boomerang.like.service;

import boomerang.board.domain.Board;
import boomerang.board.service.BoardService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.like.domain.Like;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.repository.LikeRepository;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final BoardService boardService;


    @Transactional(readOnly = true)
    public List<LikeResponseDto> getLikesByBoardId(PrincipalDetails principalDetails, Long boardId) {
        Board board = boardService.getBoard(boardId);

        List<Like> likes = likeRepository.findAllByBoardIdAndIsDeletedFalse(board.getId());

        boolean isUserLoggedIn = principalDetails != null;
        Member loginMember = isUserLoggedIn ? memberService.getMemberByEmail(principalDetails.getMemberEmail()) : null;

        return likes.stream()
            .map(like -> createLikeResponseDto(like, isUserLoggedIn, loginMember))
            .toList();
    }

    @Transactional
    public LikeResponseDto createLike(PrincipalDetails principalDetails, Long boardId) {
        Member loginMember = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Board board = boardService.getBoard(boardId);

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
        Member loginMember = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Board board = boardService.getBoard(boardId);

        Like like = likeRepository.findByMemberAndBoardAndIsDeletedFalse(loginMember, board)
            .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        like.delete();
    }

    private LikeResponseDto createLikeResponseDto(Like like, boolean isUserLoggedIn, Member loginMember) {
        if (!isUserLoggedIn) {
            return new LikeResponseDto(like, false);
        }
        return new LikeResponseDto(like, like.getMember().equals(loginMember));
    }
}
