//package com.zerobase.user.client;
//
//import com.zerobase.user.client.mailgun.SendMailForm;
//import feign.Response;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.cloud.openfeign.SpringQueryMap;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@FeignClient(name = "mailgun", url = "http://api.mailgun.net/v3/")
//@Qualifier("mailgun")
//public interface MailgunClient {
//
//    @PostMapping("sandboxa/messages")
//    ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm form);
//}
