package com.window.system.api;

import com.window.system.common.Result;
import com.window.system.model.entity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "window-order-customer", path = "/api/customer")
public interface CustomerClient {

    @GetMapping("/phone")
    Result<Customer> getByPhone(@RequestParam("phone") String phone);
    
    @GetMapping("/detail/{id}")
    Result<Customer> getById(@org.springframework.web.bind.annotation.PathVariable("id") Long id);
    
    @PostMapping("/save")
    Result<String> save(@RequestBody com.window.system.model.req.CustomerSaveReq req);
}
