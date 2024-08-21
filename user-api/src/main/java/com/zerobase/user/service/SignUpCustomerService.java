package com.zerobase.user.service;

import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {
    public final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form){
        return customerRepository.save(Customer.from(form));
    }
}
