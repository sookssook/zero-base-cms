package com.zerobase.user.service.seller;

import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.SellerRepository;
import com.zerobase.user.domain.seller.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.exception.Errorcode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(Long id, String email){
        return sellerRepository.findByIdAndEmail(id, email);
    }

    public Optional<Seller> findValidSeller(String email, String password){
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
    }

    public Seller signUp(SignUpForm form){
        return sellerRepository.save(Seller.from(form));
    }

    public boolean isEmailExist(String email){
        return sellerRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code){
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(Errorcode.NOT_FOUND_USER));

        if (seller.isVerify()){
            throw new CustomException(Errorcode.ALREADY_VERIFIED);
        } else if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(Errorcode.WRONG_VERIFICATION);
        } else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(Errorcode.ALREADY_EXPIRED_USER);
        }
        seller.setVerify(true);
    }

    @Transactional //자동 저장 기능..?
    public LocalDateTime changeSellerValidateEmail(Long sellerId, String verificationCode){
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);

        if (sellerOptional.isPresent()){
            Seller seller = sellerOptional.get();
            seller.setVerificationCode(verificationCode);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return seller.getVerifyExpiredAt();
        }
        throw new CustomException(Errorcode.NOT_FOUND_USER);
    }

}
