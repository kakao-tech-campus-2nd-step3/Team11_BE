package boomerang.member.service;

import boomerang.file.service.FileService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Member;
import boomerang.member.domain.RandomNickname;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        memberServiceDto = new MemberServiceDto("test@example.com", "testNickname");
        member = new Member(memberServiceDto);
    }

    @Test
    void testGetAllMembers() {
        // given
        List<Member> members = List.of(member);
        given(memberRepository.findAll()).willReturn(members);

        // when
        List<Member> result = memberService.getAllMembers();

        // then
        assertThat(result).containsExactly(member);
        then(memberRepository).should(times(1)).findAll();
    }

    @Test
    void testGetMember() {
        // given
        Long id = 1L;
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        // when
        Member result = memberService.getMember(id);

        // then
        assertThat(result).isEqualTo(member);
        then(memberRepository).should(times(1)).findById(id);
    }

    @Test
    void testGetMember_NotFound() {
        // given
        Long id = 1L;
        given(memberRepository.findById(id)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> memberService.getMember(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.MEMBER_NON_EXISTENT.getMessage());
    }

    @Test
    void testCreateMember() {
        // given
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(jwtUtil.generateToken(any(), anyString())).willReturn("token");

        // when
        String token = memberService.createMember(memberServiceDto);

        // then
        assertThat(token).isEqualTo("token");
        then(memberRepository).should(times(1)).save(any(Member.class));
        then(jwtUtil).should(times(1)).generateToken(any(), anyString());
    }

    @Test
    void testLoginKakaoMember_ExistingMember() {
        // given
        KakaoMember kakaoMember = mock(KakaoMember.class);
        when(kakaoMember.email()).thenReturn("test@example.com");
        given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.of(member));

        // when
        Member result = memberService.loginKakaoMember(kakaoMember);

        // then
        assertThat(result).isEqualTo(member);
        then(memberRepository).should(times(1)).findByEmail(anyString());
        then(memberRepository).should(never()).save(any(Member.class));
    }

    @Test
    void testLoginKakaoMember_NewMember() {
        // given
        KakaoMember kakaoMember = mock(KakaoMember.class);
        when(kakaoMember.email()).thenReturn("test@example.com");
        given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        Member result = memberService.loginKakaoMember(kakaoMember);

        // then
        assertThat(result).isEqualTo(member);
        then(memberRepository).should(times(1)).findByEmail(anyString());
        then(memberRepository).should(times(1)).save(any(Member.class));
    }

    @Test
    void testLoginMember() {
        // given
        given(memberRepository.findByEmailAndNickname(anyString(), anyString()))
                .willReturn(Optional.of(member));
        given(jwtUtil.generateToken(any(), anyString())).willReturn("token");

        // when
        String token = memberService.loginMember(memberServiceDto);

        // then
        assertThat(token).isEqualTo("token");
        then(memberRepository).should(times(1))
                .findByEmailAndNickname(anyString(), anyString());
        then(jwtUtil).should(times(1)).generateToken(any(), anyString());
    }

    @Test
    void testGenerateUniqueNickname() {
        // given
        String expectedNickname = "randomNick";
        given(randomNicknameGenerator.generateRandomNickname()).willReturn(expectedNickname);

        // when
        String result = memberService.generateUniqueNickname();

        // then
        assertThat(result).isEqualTo(expectedNickname);
        then(randomNicknameGenerator).should(times(1)).generateRandomNickname();
    }

    @Test
    void testUpdateNickname() {
        // given
        String email = "test@example.com";
        String newNickname = "newNickname";
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(memberRepository.existsByNickname(newNickname)).willReturn(false);

        // when
        Member result = memberService.updateNickname(email, newNickname);

        // then
        assertThat(result.getNickname()).isEqualTo(newNickname);
        then(memberRepository).should(times(1)).findByEmail(email);
        then(memberRepository).should(times(1)).existsByNickname(newNickname);
        then(memberRepository).should(times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateNickname_DuplicateNickname() {
        // given
        String email = "test@example.com";
        String newNickname = "newNickname";
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(memberRepository.existsByNickname(newNickname)).willReturn(true);

        // when/then
        assertThatThrownBy(() -> memberService.updateNickname(email, newNickname))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_NICKNAME_ERROR.getMessage());
    }

    @Test
    void testUpdateProfileImage_MemberNotFound() {
        // given
        String email = "test@example.com";
        MultipartFile image = mock(MultipartFile.class);
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> memberService.updateProfileImage(email, image))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.MEMBER_NON_EXISTENT.getMessage());
    }
}