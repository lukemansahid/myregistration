package com.lukish.myregistration.controllers;

import com.lukish.myregistration.models.RegistrationRequest;
import com.lukish.myregistration.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public String register(@RequestBody RegistrationRequest registrationRequest){

      return registrationService.register(registrationRequest);
    }

}
