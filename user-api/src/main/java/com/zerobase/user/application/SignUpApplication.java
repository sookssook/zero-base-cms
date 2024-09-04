package com.zerobase.user.application;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.mailgun.SendMailForm;
import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.seller.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.exception.Errorcode;
import com.zerobase.user.service.customer.CustomerService;
import com.zerobase.user.service.customer.SignUpCustomerService;
import com.zerobase.user.service.seller.SellerService;
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
    private final SellerService sellerService;
    private final CustomerService customerService;

    public void customerVerify(String email, String code){
        signUpCustomerService.verifyEmail(email, code);
    }
    public String customerSignUp(SignUpForm form){
        if (signUpCustomerService.isEmailExist(form.getEmail())){
            //exception
            throw new CustomException(Errorcode.ALREADY_REGISTERED_USER);
        }else {
            Customer c = signUpCustomerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@februstar11.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(c.getEmail(), c.getName(), "customer", code))
                    .build();
            log.info("Send email result : "+mailgunClient.sendEmail(sendMailForm).getBody());
            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
            return "회원가입에 성공하였습니다!";
        }
    }

    public void sellerVerify(String email, String code){
        sellerService.verifyEmail(email, code);
    }

    public String sellerSignUp(SignUpForm form){
        if (sellerService.isEmailExist(form.getEmail())){
            //exception
            throw new CustomException(Errorcode.ALREADY_REGISTERED_USER);
        }else {
            Seller s = sellerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tester@februstar11.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(s.getEmail(), s.getName(), "seller", code))
                    .build();
            log.info("Send email result : "+mailgunClient.sendEmail(sendMailForm).getBody());
            sellerService.changeSellerValidateEmail(s.getId(), code);
            return "회원가입에 성공하였습니다!";
        }
    }

    private String getRandomCode(){
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String type, String code){
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ").append(name).append("! Please click link for verification.\n\n")
                .append("http://localhost:8081/signup/"+type+"/verify/signup?email=")
                .append(email).append("&code=")
                .append(code).toString();
    }

}
