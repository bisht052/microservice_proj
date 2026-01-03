package com.eazybanks.controller;

import com.eazybanks.dto.CustomerDto;
import com.eazybanks.service.IAccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(AccountsController.class)
class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean   // ✅ MUST be MockBean
    private IAccountsService iAccountsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_ShouldReturn201() throws Exception {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("chetanRam");
        customerDto.setEmail("bishtram@gmail.com");
        customerDto.setMobileNumber("7883706919");

        mockMvc.perform(
                        post("/api/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("201"))
                .andExpect(jsonPath("$.statusMsg")
                        .value("Account created successfully"));

        // ✅ Verify service interaction
        verify(iAccountsService, times(1))
                .createAccount(any(CustomerDto.class));
    }
    /*@Test
    void fetchAccount_ShouldReturnCustomerDto() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("chetanRambist");
        customerDto.setEmail("bishtram@gmail.com");
        customerDto.setMobileNumber("7883706919");

        // mock service behavior
        when(iAccountsService.fetchAccount("7883706919"))
                .thenReturn(customerDto);
        mockMvc.perform(
                        get("/api/fetch")
                                .param("mobileNumber", "7883706919")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // 👈 shows JSON in console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("chetanRambist"))
                .andExpect(jsonPath("$.email").value("bishtram@gmail.com"))
                .andExpect(jsonPath("$.mobileNumber").value("7883706919"));
    }*/
}
