package com.eazybanks.service;

import com.eazybanks.dto.AccountsDto;
import com.eazybanks.dto.CustomerDto;
import com.eazybanks.entity.Accounts;
import com.eazybanks.entity.Customer;
import com.eazybanks.repository.AccountsRepository;
import com.eazybanks.repository.CustomerRepository;
import com.eazybanks.service.impl.AccountsServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AccountsServiceImplTest {

    @InjectMocks
    AccountsServiceImpl accountsServiceImpl;

     @Mock
    AccountsRepository accountsRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void fetchAccountTest(){

        String mobileNumber = "7983706916";

        Customer customer = createCustomerStub();
        Accounts accounts = createAccountsStub();

        CustomerDto customerDto = new CustomerDto();
        AccountsDto accountsDto = new AccountsDto();

        // repository mocks
        when(customerRepository.findByMobileNumber(mobileNumber))
                .thenReturn(Optional.of(customer));

        when(accountsRepository.findByCustomerId(customer.getCustomerId()))
                .thenReturn(Optional.of(accounts));

      // mapper mocks
        when(modelMapper.map(customer, CustomerDto.class))
                .thenReturn(customerDto);

        when(modelMapper.map(accounts, AccountsDto.class))
                .thenReturn(accountsDto);

       // call method
        CustomerDto result = accountsServiceImpl.fetchAccount(mobileNumber);
        assertEquals(customerDto, result);
    }

    private Customer createCustomerStub() {
        return Customer.builder()
                .customerId(1L)              // Java field name
                .name("decode")              // Java field name
                .mobileNumber("9999999999")  // Java field name
                .email("test@gmail.com")
                .build();
    }

    private Accounts createAccountsStub() {
        return Accounts.builder()
                .customerId(1L)
                .accountNumber(123456789L)
                .accountType("SAVINGS")
                .branchAddress("Delhi")
                .build();
    }
    }
