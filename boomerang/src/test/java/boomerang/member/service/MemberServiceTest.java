package boomerang.member.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.repository.MemberRepository;
import boomerang.file.service.FileService;
import boomerang.global.utils.JwtUtil;
import boomerang.member.domain.RandomNickname;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RandomNickname randomNicknameGenerator;

    @Mock
    private FileService fileService;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberServiceDto memberServiceDto;

    @BeforeEach
    void setUp() {
        member = new Member(new MemberServiceDto("test@example.com", "TestNickname"));
        memberServiceDto = new MemberServiceDto("test@example.com", "TestNickname");
    }

    @Test
    void testCreateMember_Success() {
        // given
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(jwtUtil.generateToken(any(Long.class), any(String.class))).thenReturn("test-token");

        // when
        String token = memberService.createMember(memberServiceDto);

        // then
        assertThat(token).isEqualTo("test-token");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testLoginMember_Success() {
        // given
        when(memberRepository.findByEmailAndNickname(any(String.class), any(String.class))).thenReturn(Optional.of(member));
        when(jwtUtil.generateToken(any(Long.class), any(String.class))).thenReturn("test-token");

        // when
        String token = memberService.loginMember(memberServiceDto);

        // then
        assertThat(token).isEqualTo("test-token");
    }

    @Test
    void testLoginMember_MemberNotFound() {
        // given
        when(memberRepository.findByEmailAndNickname(any(String.class), any(String.class))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.loginMember(memberServiceDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.MEMBER_NON_EXISTENT.getMessage());
    }

    @Test
    void testUpdateNickname_Success() {
        // given
        String newNickname = "NewNickname";
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.of(member));
        when(memberRepository.existsByNickname(newNickname)).thenReturn(false);

        // when
        Member updatedMember = memberService.updateNickname(member.getEmail(), newNickname);

        // then
        assertThat(updatedMember.getNickname()).isEqualTo(newNickname);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateNickname_DuplicateNickname() {
        // given
        String newNickname = "DuplicateNickname";
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.of(member));
        when(memberRepository.existsByNickname(newNickname)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.updateNickname(member.getEmail(), newNickname))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_NICKNAME_ERROR.getMessage());
    }

    @Test
    void testUpdateProfileImage_Success() {
        // given
        String email = "test@example.com";
        MultipartFile image = mock(MultipartFile.class);
        URL imageUrl = mock(URL.class);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(fileService.upload(eq(email), any(MultipartFile.class))).thenReturn(imageUrl);

        // when
        Member updatedMember = memberService.updateProfileImage(email, image);

        // then
        assertThat(updatedMember.getProfileImage()).isEqualTo(imageUrl.toString());
        verify(fileService, times(1)).upload(eq(email), any(MultipartFile.class));
    }

    @Test
    void testUpdateProfileImage_MemberNotFound() {
        // given
        String email = "test@example.com";
        MultipartFile image = mock(MultipartFile.class);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.updateProfileImage(email, image))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.MEMBER_NON_EXISTENT.getMessage());
    }

    @Test
    void testUpdateProfileImage_S3UploadError() {
        // given
        String email = "test@example.com";
        MultipartFile image = mock(MultipartFile.class);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(fileService.upload(eq(email), any(MultipartFile.class))).thenThrow(new RuntimeException("S3 upload error"));

        // when & then
        assertThatThrownBy(() -> memberService.updateProfileImage(email, image))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.S3_UPLOAD_ERROR.getMessage());
    }

    @Test
    void testGenerateUniqueNickname() {
        // given
        when(randomNicknameGenerator.generateRandomNickname()).thenReturn("RandomNickname123");

        // when
        String nickname = memberService.generateUniqueNickname();

        // then
        assertThat(nickname).isEqualTo("RandomNickname123");
    }
}
