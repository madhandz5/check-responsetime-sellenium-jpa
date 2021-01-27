package co.suggesty.pageloadtimecheck.member.validator;

import co.suggesty.pageloadtimecheck.member.MemberRepository;
import co.suggesty.pageloadtimecheck.member.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if (memberRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (!signUpForm.getEmail().contains("fromzero.co.kr") && !signUpForm.getEmail().contains("leenhan.com")) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "사용할 수 없는 이메일 주소입니다.");
        }
    }
}
