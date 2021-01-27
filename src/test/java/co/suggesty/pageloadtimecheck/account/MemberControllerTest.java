package co.suggesty.pageloadtimecheck.account;

import co.suggesty.pageloadtimecheck.member.CompanyName;
import co.suggesty.pageloadtimecheck.member.Member;
import co.suggesty.pageloadtimecheck.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("인증메일 확인 - 입력값 오류")
    @Test
    void checkEmailTokenWithWrongInput() throws Exception {
        mockMvc.perform(get("/member/check-email-token")
                .param("token", "sdfsdfsdf")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("member/checked-email"))
                .andExpect(unauthenticated());
    }

    @DisplayName("인증메일 확인 - 입력값 정상")
    @Test
    void checkEmailToken() throws Exception {
        Member account = Member.builder()
                .name("Ryan")
                .email("test@email.com")
                .password("12345678")
                .company(CompanyName.TIL21)
                .build();

        Member newAccount = memberRepository.save(account); // persitstance
        newAccount.generateEmailCheckToken(); //

        mockMvc.perform(get("/member/check-email-token")
                .param("token", newAccount.getEmailCheckToken())
                .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("member/checked-email"))
                .andExpect(authenticated().withUsername("Ryan"));

    }

    @Test
    @DisplayName("회원가입 페이지 호출")
    void signUpForm() throws Exception {
        mockMvc.perform(get("/member/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입처리 - 입력값 오류")
    @Test
    void signUpSubmitWithWrongInput() throws Exception {
        mockMvc.perform(post("/member/sign-up")
                .param("name", "ryan")
                .param("email", "e.kr")
                .param("password", "123")
                .param("company", String.valueOf(CompanyName.TIL21))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입처리 - 입력값 정상")
    @Test
    void signUpSubmitWithCorrectInput() throws Exception {
        mockMvc.perform(post("/member/sign-up")
                .param("name", "ryan")
                .param("email", "ryan@fromzero.co.kr")
                .param("password", "12345678910")
                .param("company", String.valueOf(CompanyName.TIL21))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("ryan"));

        Member account = memberRepository.findByEmail("ryan@fromzero.co.kr");

        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678910");
        assertTrue(memberRepository.existsByEmail("ryan@fromzero.co.kr"));
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}
