package com.eazybanks.controller;

import com.eazybanks.constants.AccountsConstants;
import com.eazybanks.dto.CustomerDto;
import com.eazybanks.dto.ErrorResponseDto;
import com.eazybanks.dto.ResponseDto;
import com.eazybanks.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api",produces={MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AccountsController {

    private IAccountsService iAccountsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccout(@RequestBody CustomerDto customerDto){
            iAccountsService.createAccount(customerDto);

      return  ResponseEntity
              .status(HttpStatus.CREATED)
              .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                           String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }
}
