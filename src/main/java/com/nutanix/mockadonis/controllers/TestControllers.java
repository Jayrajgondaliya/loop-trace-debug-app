package com.nutanix.mockadonis.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class TestControllers {
    @GetMapping("/test")
    public String getTest() {
        return "test";
    }

    @GetMapping("/greet")
    public ResponseEntity<?> test1(@RequestParam Optional<String> name,
                                   @RequestParam Optional<Long> delay,
                                   HttpServletRequest request) throws InterruptedException {
        String nameParam = name.orElse(null);
        methodThatCreatesSpan(delay.orElse(0l));
        String response = nameParam == null ? "Hello" : "Hello, "+nameParam;
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private void methodThatCreatesSpan(long delay){
        try{
            Thread.sleep(delay);
        }catch(Exception exp){
        }
    }
}
