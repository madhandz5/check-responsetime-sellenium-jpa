package co.suggesty.pageloadtimecheck.main;

import co.suggesty.pageloadtimecheck.member.CompanyName;
import co.suggesty.pageloadtimecheck.member.MemberRepository;
import co.suggesty.pageloadtimecheck.member.MemberService;
import co.suggesty.pageloadtimecheck.member.form.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void createAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setName("오창영");
        signUpForm.setEmail("ryan@fromzero.co.kr");
        signUpForm.setPassword("12345678");
        memberService.processNewAccount(signUpForm);
    }

    @AfterEach
    void deleteAccount() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일 로그인 성공")
    void loginWithEmail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "ryan@fromzero.co.kr")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("오창영"));
    }

 @Test
    @DisplayName("로그인 실패")
    void loginFail() throws Exception{
        mockMvc.perform(post("/login")
                .param("username", "123123")
                .param("password", "123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃")
    void logout() throws Exception{
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("이름 로그인 성공")
    void loginWithNickname() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "오창영")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("오창영"));
    }
}
