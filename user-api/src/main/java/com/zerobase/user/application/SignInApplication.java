package com.zerobase.user.application;

import com.zerobase.user.domain.SignInForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.seller.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.exception.Errorcode;
import com.zerobase.user.service.customer.CustomerService;
import com.zerobase.user.service.seller.SellerService;
import com.zerobase.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobase.zerobasedomain.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;
    private final SellerService sellerService;

    public String customerLoginToken(SignInForm form){
        //1. 로그인 가능 여부
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(Errorcode.LOGIN_CHECK_FAIL));
        //2. 토큰을 발행하고
        //3. 토큰을 response한다.
        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }

    public String sellerLoginToken(SignInForm form){
        //1. 로그인 가능 여부
        Seller s = sellerService.findValidSeller(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(Errorcode.LOGIN_CHECK_FAIL));
        //2. 토큰을 발행하고
        //3. 토큰을 response한다.
        return provider.createToken(s.getEmail(), s.getId(), UserType.SELLER);
    }
}
