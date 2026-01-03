package com.eazybanks.service;

import com.eazybanks.dto.AccountsDto;
import com.eazybanks.dto.CustomerDto;
import com.eazybanks.entity.Accounts;
import com.eazybanks.entity.Customer;
import com.eazybanks.repository.AccountsRepository;
import com.eazybanks.repository.CustomerRepository;
import com.eazybanks.service.impl.AccountsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class AccountsServiceImplTest {

    @InjectMocks
    AccountsServiceImpl accountsServiceImpl;

    @Mock
    AccountsRepository accountsRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ModelMapper modelMapper;

    private String mobileNumber;
    private Customer customer;
    private Accounts accounts;
    private CustomerDto customerDto;
    private AccountsDto accountsDto;

    @BeforeEach
    void setUp() {
        mobileNumber = "7983706916";
        customer = createCustomerStub();
        accounts = createAccountsStub();
        customerDto = new CustomerDto();
        accountsDto = new AccountsDto();
    }

    @Test
    void fetchAccountTest() {
        when(customerRepository.findByMobileNumber(mobileNumber))
                .thenReturn(Optional.of(customer));

        when(accountsRepository.findByCustomerId(customer.getCustomerId()))
                .thenReturn(Optional.of(accounts));

        when(modelMapper.map(customer, CustomerDto.class))
                .thenReturn(customerDto);

        when(modelMapper.map(accounts, AccountsDto.class))
                .thenReturn(accountsDto);

        CustomerDto result = accountsServiceImpl.fetchAccount(mobileNumber);
        assertEquals(customerDto, result);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finished");
    }

    // stub methods
    private Customer createCustomerStub() {
        return Customer.builder()
                .customerId(1L)
                .name("decode")
                .mobileNumber("9999999999")
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