package boomerang.mentor.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.mentor.dto.MentorCreateRequestDto;
import boomerang.mentor.dto.MentorResponseDto;
import boomerang.mentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentor")
public class MentorController {

    private final MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponseDto> createMentor(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody MentorCreateRequestDto mentorCreateRequestDto){
        MentorResponseDto mentorResponseDto = mentorService.createMentor(principalDetails.getMemberEmail(), mentorCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorResponseDto);
    }
}
