package boomerang.mentor.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.mentor.dto.MentorCreateRequestDto;
import boomerang.mentor.dto.MentorResponseDto;
import boomerang.mentor.dto.MentorUpdateRequestDto;
import boomerang.mentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentor")
public class MentorController {

    private final MentorService mentorService;

    // 멘토 조회
    @GetMapping
    public ResponseEntity<Page<MentorResponseDto>> getAllMentors(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MentorResponseDto> mentors = mentorService.getAllMentors(pageable);
        return ResponseEntity.ok(mentors);
    }

    // 멘토 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<MentorResponseDto> getMentorById(@PathVariable Long id) {
        MentorResponseDto mentorResponseDto = mentorService.getMentorById(id);
        return ResponseEntity.ok(mentorResponseDto);
    }

    // 멘토 등록
    @PostMapping
    public ResponseEntity<MentorResponseDto> createMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody MentorCreateRequestDto mentorCreateRequestDto){
        MentorResponseDto mentorResponseDto = mentorService.createMentor(principalDetails.getMemberEmail(), mentorCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorResponseDto);
    }

    // 멘토 정보 수정
    @PutMapping
    public ResponseEntity<MentorResponseDto> updateMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody MentorUpdateRequestDto updateRequestDto) {
        MentorResponseDto updatedMentor = mentorService.updateMentor(principalDetails.getMemberEmail(), updateRequestDto);
        return ResponseEntity.ok(updatedMentor);
    }

    // 멘토 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteMentor(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        mentorService.deleteMentor(principalDetails.getMemberEmail());
        return ResponseEntity.noContent().build();
    }
}
