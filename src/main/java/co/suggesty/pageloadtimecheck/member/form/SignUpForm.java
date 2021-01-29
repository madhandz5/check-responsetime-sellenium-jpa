package co.suggesty.pageloadtimecheck.member.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{2,20}$")
    private String name;


    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
}
