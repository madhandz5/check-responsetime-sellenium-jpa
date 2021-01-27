package co.suggesty.pageloadtimecheck.member;

import co.suggesty.pageloadtimecheck.member.form.SignUpForm;
import co.suggesty.pageloadtimecheck.member.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final SignUpFormValidator signUpFormValidator;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping(value = "/member/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "member/sign-up";
    }

    @PostMapping(value = "/member/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "member/sign-up";
        }
        Member member = memberService.processNewAccount(signUpForm);
        memberService.login(member);

        return "redirect:/";
    }

    @GetMapping(value = "/member/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Member member = memberRepository.findByEmail(email);
        String view = "member/checked-email";
        if (member == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (!member.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        memberService.completeSignUp(member);
        model.addAttribute("numberOfUser", memberRepository.count());
        model.addAttribute("name", member.getName());
        return view;
    }

    @GetMapping(value = "/member/check-email")
    public String checkEmail(@CurrentMember Member member, Model model) {
        model.addAttribute("email", member.getEmail());
        return "member/check-email";
    }

    @GetMapping(value = "/member/resend-confirm-email")
    public String resendConfirmEmail(@CurrentMember Member member, Model model) {
        if (!member.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 30분에 한번만 전송 가능합니다.");
            model.addAttribute("email", member.getEmail());
            return "member/check-email";
        }
        memberService.sendSignUpConfirmEmail(member);
        return "redirect:/";
    }
}
