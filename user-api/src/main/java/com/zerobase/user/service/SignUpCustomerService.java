package com.zerobase.user.service;

import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomerException;
import com.zerobase.user.exception.Errorcode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {
    public final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form){
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email){
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code){
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerException(Errorcode.NOT_FOUND_USER));

        if (customer.isVerify()){
            throw new CustomerException(Errorcode.ALREADY_VERIFIED);
        }else if (!customer.getVerificationCode().equals(code)){
            throw new CustomerException(Errorcode.WRONG_VERIFICATION);
        }else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomerException(Errorcode.ALREADY_VERIFIED);
        }
        customer.setVerify(true);
    }

    @Transactional //자동 저장 기능..?
    public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setVerificationCode(verificationCode);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return customer.getVerifyExpiredAt();
        }
        throw new CustomerException(Errorcode.NOT_FOUND_USER);
    }
}
