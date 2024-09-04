package com.zerobase.user.service.customer;

import com.zerobase.user.domain.customer.ChangeBalanceForm;
import com.zerobase.user.domain.model.CustomerBalanceHistory;
import com.zerobase.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.exception.Errorcode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRespository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException{
        CustomerBalanceHistory customerBalanceHistory =
                customerBalanceHistoryRespository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                        .orElse(CustomerBalanceHistory.builder()
                                .changeMoney(0)
                                .currentMoney(0)
                                .customer(customerRepository.findById(customerId)
                                        .orElseThrow(()-> new CustomException(Errorcode.NOT_FOUND_USER)))
                                .build());
        if (customerBalanceHistory.getCurrentMoney()+ form.getMoney() < 0){
            throw new CustomException(Errorcode.NOT_ENOUGH_BALANCE);
        }

        customerBalanceHistory = CustomerBalanceHistory.builder()
                .changeMoney(form.getMoney())
                .currentMoney(customerBalanceHistory.getCurrentMoney()+ form.getMoney())
                .description(form.getMessage())
                .fromMessage(form.getFrom())
                .customer(customerBalanceHistory.getCustomer())
                .build();
        customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

       return customerBalanceHistoryRespository.save(customerBalanceHistory);
    }
}
