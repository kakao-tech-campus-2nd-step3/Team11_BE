package boomerang.member.service;

import boomerang.file.service.FileService;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import boomerang.kakao.domain.KakaoMember;
import boomerang.member.domain.Member;
import boomerang.member.domain.RandomNickname;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.exception.MemberNotFoundException;
import boomerang.member.repository.MemberRepository;
import java.net.URL;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RandomNickname randomNicknameGenerator;
    private final FileService fileService;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil,
        RandomNickname randomNicknameGenerator, FileService fileService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.randomNicknameGenerator = randomNicknameGenerator;
        this.fileService = fileService;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }


    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    public String createMember(MemberServiceDto memberCreateServiceDto) {
        Member member = memberRepository.save(new Member(memberCreateServiceDto));
        return jwtUtil.generateToken(member.getId(), member.getEmail());
    }

    public Member loginKakaoMember(KakaoMember kakaoMember) {
        String email = kakaoMember.email(); //카카오에서 받은 이메일을 emailString으로 저장
        return memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(new Member(kakaoMember)));
    }

    public String loginMember(MemberServiceDto memberCreateServiceDto) {
        String email = memberCreateServiceDto.getEmail();
        String nickName = memberCreateServiceDto.getNickname();
        Member member = memberRepository.findByEmailAndNickname(email, nickName)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
        return jwtUtil.generateToken(member.getId(), member.getEmail());
    }

//    안쓰는 로직 : 팀원들과 상의 후 삭제 예정
//    public Member updateMember(Long id,MemberCreateRequestDto memberCreateRequestDTO) {
//        Member member = getMember(id);
//        member.update(memberCreateRequestDTO);
//        return memberRepository.save(member);
//    }
//
//    public void deleteMember(Long id) {
//        validateMemberExists(id);
//        memberRepository.deleteById(id);
//    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

    // 중복되지 않는 유니크한 닉네임을 생성하는 메서드
    public String generateUniqueNickname() {
        // 기본 랜덤 닉네임 생성
        return randomNicknameGenerator.generateRandomNickname();
    }

    public Member updateNickname(String email, String newNickname) {
        Member member = getMemberByEmail(email);
        if (memberRepository.existsByNickname(newNickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME_ERROR);
        }

        member.updateNickname(newNickname);
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public Member updateProfileImage(String email, MultipartFile image) {
        log.info("Starting profile image update for email: {}", email);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));

        try {
            log.info("Attempting to upload image. Original filename: {}", image.getOriginalFilename());

            // S3에 이미지 업로드
            URL imageUrl = fileService.upload(email, image);
            log.info("Successfully uploaded image to S3. URL: {}", imageUrl);

            member.updateProfileImage(imageUrl.toString());

            return member;
        } catch (Exception e) {
            log.error("Error during image upload/update process", e);
            throw new BusinessException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }
}
