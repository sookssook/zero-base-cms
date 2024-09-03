package com.zerobase.user.controller;

import com.zerobase.user.domain.customer.CustomerDto;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomerException;
import com.zerobase.user.exception.Errorcode;
import com.zerobase.user.service.CustomerService;
import com.zerobase.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobase.zerobasedomain.domain.common.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN")
                                               String token){
        UserVo vo = provider.getUserVo(token);
        Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
                ()->new CustomerException(Errorcode.NOT_FOUND_USER)
        );
        return ResponseEntity.ok(CustomerDto.from(c));
    }
}
