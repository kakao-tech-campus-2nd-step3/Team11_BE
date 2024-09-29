package boomerang.domain.member.service;

import boomerang.domain.member.domain.MemberDomain;
import boomerang.domain.member.dto.MemberServiceDto;
import boomerang.domain.member.exception.MemberNotFoundException;
import boomerang.domain.member.repository.MemberRepository;
import boomerang.global.utils.JwtUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<MemberDomain> getAllMemberDomains() {
        return memberRepository.findAll();
    }

    public MemberDomain getMemberDomainById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public MemberDomain createMemberDomain(MemberServiceDto memberCreateServiceDto) {
        return memberRepository.save(memberCreateServiceDto.toMemberDomain());
    }

    public MemberDomain updateMemberDomain(MemberServiceDto memberCreateServiceDto) {
        validateMemberDomainExists(memberCreateServiceDto.getId());
        return memberRepository.save(memberCreateServiceDto.toMemberDomain());
    }

    public void deleteMemberDomain(Long id) {
        validateMemberDomainExists(id);
        memberRepository.deleteById(id);
    }

    private void validateMemberDomainExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }
}
