package com.zerobase.user.application;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.mailgun.SendMailForm;
import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.exception.CustomerException;
import com.zerobase.user.exception.Errorcode;
import com.zerobase.user.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;

    public void customerVerify(String email, String code){
        signUpCustomerService.verifyEmail(email, code);
    }
    public String customerSignUp(SignUpForm form){
        if (signUpCustomerService.isEmailExist(form.getEmail())){
            //exception
            throw new CustomerException(Errorcode.ALREADY_REGISTERED_USER);
        }else {
            Customer c = signUpCustomerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@februstar11.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(c.getEmail(), c.getName(), code))
                    .build();
            log.info("Send email result : "+mailgunClient.sendEmail(sendMailForm).getBody());

            mailgunClient.sendEmail(sendMailForm);
            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);

            return "회원가입에 성공하였습니다!";
        }
    }

    private String getRandomCode(){
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code){
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ").append(name).append("! Please click link for verification.\n\n")
                .append("http://localhost:8082/customer/verify/signup?email=")
                .append(email).append("&code=")
                .append(code).toString();
    }

}
