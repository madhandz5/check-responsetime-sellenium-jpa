package co.suggesty.pageloadtimecheck.member;

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

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member processNewAccount(SignUpForm signUpForm) {
        Member newMember = saveNewMember(signUpForm);
        newMember.generateEmailCheckToken();
        sendSignUpConfirmEmail(newMember);
        return newMember;
    }

    private Member saveNewMember(SignUpForm signUpForm) {

        CompanyName company = null;
        if(signUpForm.getEmail().contains("fromzero")){
            company = CompanyName.TIL21;
        }else if(signUpForm.getEmail().contains("leenhan")){
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
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newMember.getEmail());
        mailMessage.setSubject("[TIL21] 가입을 위한 이메일을 확인해주세요.");
        mailMessage.setText("/member/check-email-token?token=" + newMember.getEmailCheckToken() + "&email=" + newMember.getEmail());

        javaMailSender.send(mailMessage);
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
