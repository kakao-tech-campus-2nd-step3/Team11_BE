package boomerang.like.service;

import boomerang.board.domain.Board;
import boomerang.board.service.BoardService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.like.domain.Like;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.dto.LikeSummaryResponseDto;
import boomerang.like.repository.LikeRepository;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
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
    public LikeSummaryResponseDto getLikeSummary(PrincipalDetails principalDetails, Long boardId) {
        Board board = boardService.getBoard(boardId);
        int likeCount = likeRepository.countByBoardIdAndIsDeletedFalse(board.getId());

        boolean isLiked = false;
        if (principalDetails != null) {
            Member loginMember = memberService.getMemberByEmail(principalDetails.getMemberEmail());
            isLiked = likeRepository.existsByMemberAndBoardAndIsDeletedFalse(loginMember, board);
        }

        return new LikeSummaryResponseDto(likeCount, isLiked);
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
        board.increaseLikeCount();

        return new LikeResponseDto(savedLike, true);
    }

    @Transactional
    public void deleteLike(PrincipalDetails principalDetails, Long boardId) {
        Member loginMember = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Board board = boardService.getBoard(boardId);

        Like like = likeRepository.findByMemberAndBoardAndIsDeletedFalse(loginMember, board)
            .orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

        like.delete();
        board.decreaseLikeCount();
    }

    private LikeResponseDto createLikeResponseDto(Like like, boolean isUserLoggedIn, Member loginMember) {
        if (!isUserLoggedIn) {
            return new LikeResponseDto(like, false);
        }
        return new LikeResponseDto(like, like.getMember().equals(loginMember));
    }

    public boolean isLikedByMember(Board board, Member member) {
        return likeRepository.existsByMemberAndBoardAndIsDeletedFalse(member, board);
    }
}
