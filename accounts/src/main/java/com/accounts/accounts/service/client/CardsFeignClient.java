package com.accounts.accounts.service.client;

import com.accounts.accounts.entities.Cards;
import com.accounts.accounts.entities.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
    List<Cards> getCardDetails(@RequestBody Customer customer);
}
