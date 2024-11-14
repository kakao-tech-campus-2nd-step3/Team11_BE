package boomerang.mentor.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.PageResponseDto;
import boomerang.mentor.dto.*;
import boomerang.mentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentor")
public class MentorController {

    private final MentorService mentorService;

    // 멘토 초기 조회
    @GetMapping("/initial")
    public ResponseEntity<MentorInitialListResponseDto> getInitialMentors(
            @ModelAttribute MentorInitialListRequestDto mentorInitialListRequestDto) {

        MentorInitialListResponseDto responseDto =
                mentorService.getInitialMentorPage(mentorInitialListRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    // 멘토 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<MentorResponseDto>> getAllMentors(
            @ModelAttribute MentorListRequestDto mentorListRequestDto) {

        return ResponseEntity.ok(mentorService.getAllMentors(mentorListRequestDto));
    }

    // 멘토 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<MentorResponseDto> getMentorById(@PathVariable Long id) {
        MentorResponseDto mentorResponseDto = mentorService.getMentorProfile(id);
        return ResponseEntity.ok(mentorResponseDto);
    }

    // 멘토 등록
    @PostMapping
    public ResponseEntity<MentorResponseDto> createMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody MentorCreateRequestDto mentorCreateRequestDto) {
        MentorResponseDto mentorResponseDto = mentorService.createMentor(
            principalDetails.getMemberEmail(), mentorCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorResponseDto);
    }

    // 멘토 정보 수정
    @PutMapping
    public ResponseEntity<MentorResponseDto> updateMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody MentorUpdateRequestDto updateRequestDto) {
        MentorResponseDto updatedMentor = mentorService.updateMentor(
            principalDetails.getMemberEmail(), updateRequestDto);
        return ResponseEntity.ok(updatedMentor);
    }

    // 멘토 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        mentorService.deleteMentor(principalDetails.getMemberEmail());
        return ResponseEntity.noContent().build();
    }
}
