package com.accounts.accounts.controller;

import com.accounts.accounts.config.AccountServiceConfig;
import com.accounts.accounts.dto.Properties;
import com.accounts.accounts.entities.*;
import com.accounts.accounts.repositories.AccountsRepository;
import com.accounts.accounts.service.client.CardsFeignClient;
import com.accounts.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountServiceConfig accountServiceConfig;

    @Autowired
    LoansFeignClient loansFeignClient;
    @Autowired
    CardsFeignClient cardsFeignClient;

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        if (accounts != null) return accounts;
        else return null;
    }

    @GetMapping("/accounts/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountServiceConfig.getMsg(), accountServiceConfig.getBuildVersion(), accountServiceConfig.getMailDetails(), accountServiceConfig.getActiveBranches());

        String jsonStr = objectWriter.writeValueAsString(properties);
        return jsonStr;
    }

    @PostMapping("/myCustomerDetails")
    //@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    @Retry(name="retryForCustomerDetails",fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails myCustomerDetails(@RequestHeader("bank-correlation-id") String correlationId,@RequestBody Customer customer) {

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationId,customer);
        List<Cards> cards = cardsFeignClient.getCardDetails(customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);
        return customerDetails;
    }

    private CustomerDetails myCustomerDetailsFallBack(String correlationId,Customer customer, Throwable t) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationId,customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        return customerDetails;
    }


    @GetMapping("/sayHello")
    @RateLimiter(name="sayHello",fallbackMethod = "sayHelloFallBack")
    public String sayHello()
    {
        return "Hello, Welcome to bank ";
    }

    private String sayHelloFallBack(){
        return "Hello from fallback";
    }


}
