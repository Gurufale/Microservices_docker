package com.accounts.accounts.service.client;

import com.accounts.accounts.entities.Customer;
import com.accounts.accounts.entities.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "loans")
public interface LoansFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/myLoans", consumes = "application/json")
    List<Loans> getLoansDetails(@RequestHeader("bank-correlation-id") String correlationId, @RequestBody Customer customer);
}
