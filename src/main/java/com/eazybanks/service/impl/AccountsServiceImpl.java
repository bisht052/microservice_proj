package com.eazybanks.service.impl;

import com.eazybanks.constants.AccountsConstants;
import com.eazybanks.dto.AccountsDto;
import com.eazybanks.dto.CustomerDto;
import com.eazybanks.entity.Accounts;
import com.eazybanks.entity.Customer;
import com.eazybanks.exception.CustomerAlreadyExistsException;
import com.eazybanks.exception.ResourceNotFoundException;
import com.eazybanks.repository.AccountsRepository;
import com.eazybanks.repository.CustomerRepository;
import com.eazybanks.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    private ModelMapper modelMapper;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }

        // ✅ DTO → Entity conversion
        Customer customer = modelMapper.map(customerDto, Customer.class);
   customer.setCreatedAt(LocalDateTime.now());
   customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }




    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");
        return newAccount;
    }
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        // ✅ Entity → DTO mapping
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);

        AccountsDto accountsDto = modelMapper.map(accounts, AccountsDto.class);

        customerDto.setAccountsDto(accountsDto);

        return customerDto;
    }
}
