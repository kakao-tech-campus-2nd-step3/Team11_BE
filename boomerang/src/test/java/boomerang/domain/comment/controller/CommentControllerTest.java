package boomerang.domain.comment.controller;

import boomerang.comment.controller.CommentController;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        //서비스목객체는 아무동작도 하지 않도록 설정
        doNothing().when(commentService).createComment(anyString(), anyLong(), any(CommentRequestDto.class));
    }

    @Test
    @DisplayName("댓글 요청 객체 내부 값이 공백이 아닌 경우에는 정상 동작 함.")
    @WithMockUser(username = "testUser", roles = {"USER"}) // 시큐리티의 권한 검사 로직을 통과하도록 설정
    void createCommentWithValidSucceed() throws Exception {
        //given : 값이 들어가있는 댓글 요청 객체 생성
        String jsonRequest = """
                {
                    "comment_text": "빈값이 아니다."
                }
                """;

        //when - then
        mockMvc.perform(post("/api/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))  // CSRF 토큰 추가 (시큐리티로 인한 에러 방지)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 요청 객체 내부 값이 아예 없는 경우 실패함")
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createCommentWithNullFail() throws Exception {
        //given : 값이 없는 댓글 요청 객체 생성
        String jsonRequest = """
                {
                }
                """;


        //when
        MvcResult result = mockMvc.perform(post("/api/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();


        //then : EC002 -> 댓글요청객체에 텍스트값이 없는 상황
        assertThat(result.getResponse().getContentAsString()).contains("\"code\":\"EC002\"");
    }

    @Test
    @DisplayName("댓글 요청 객체 내부 값이 공백인 경우 실패함")
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createCommentWithBlankFail() throws Exception {
        //given : 값이 없는 댓글 요청 객체 생성
        String jsonRequest = """
                {
                    "comment_text": " "
                }
                """;


        //when
        MvcResult result = mockMvc.perform(post("/api/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))  // CSRF 토큰 추가
                .andExpect(status().isBadRequest())  // 기대하는 상태 코드
                .andReturn();


        //then : EC001 -> 댓글에 공백을 허용하지 않는 상황
        assertThat(result.getResponse().getContentAsString()).contains("\"code\":\"EC001\"");

    }
}