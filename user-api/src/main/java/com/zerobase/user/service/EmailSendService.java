package com.zerobase.user.service;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public String sendEmail(){

        SendMailForm form = SendMailForm.builder()
                .from("februstar11@gmail.com")
                .to("februstar11@gmail.com")
                .subject("Test email from zero base.")
                .text("my text email test").build();

        return mailgunClient.sendEmail(form).getBody();
    }
}
