package co.suggesty.pageloadtimecheck.member;

import co.suggesty.pageloadtimecheck.config.AppProperties;
import co.suggesty.pageloadtimecheck.mail.EmailMessage;
import co.suggesty.pageloadtimecheck.mail.EmailService;
import co.suggesty.pageloadtimecheck.member.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    @Transactional
    public Member processNewAccount(SignUpForm signUpForm) {
        Member newMember = saveNewMember(signUpForm);
        newMember.generateEmailCheckToken();
        sendSignUpConfirmEmail(newMember);
        return newMember;
    }

    private Member saveNewMember(SignUpForm signUpForm) {

        CompanyName company = null;
        if (signUpForm.getEmail().contains("fromzero")) {
            company = CompanyName.TIL21;
        } else if (signUpForm.getEmail().contains("leenhan")) {
            company = CompanyName.LEENHAN;
        }

        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .name(signUpForm.getName())
                .company(company)
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        Member newMember = memberRepository.save(member);
        return newMember;
    }

    public void sendSignUpConfirmEmail(Member newMember) {
        Context context = new Context();
        context.setVariable("link", "/member/check-email-token?token=" + newMember.getEmailCheckToken() +
                "&email=" + newMember.getEmail());
        context.setVariable("name", newMember.getName());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "TIL21이 제공하는 서비스를 사용하려면 이메일을 인증해야 합니다.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/confirm-mail", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newMember.getEmail())
                .subject("[TIL21] Page Response Time Check - 이메일을 인증해주세요.")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    public void login(Member member) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberAccount(member),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrName) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(emailOrName);
        if (member == null) {
            member = memberRepository.findByName(emailOrName);
        }
        if (member == null) {
            throw new UsernameNotFoundException(emailOrName);
        }

        MemberAccount memberAccount = new MemberAccount(member);

        return memberAccount;
    }

    public void completeSignUp(Member member) {
        member.completeSignUp();
        login(member);
    }
}
