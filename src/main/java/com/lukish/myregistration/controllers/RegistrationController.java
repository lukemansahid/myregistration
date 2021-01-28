package com.lukish.myregistration.controllers;

import com.lukish.myregistration.models.RegistrationRequest;
import com.lukish.myregistration.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public String register(@RequestBody RegistrationRequest registrationRequest){

      return registrationService.register(registrationRequest);
    }

    @GetMapping("confirm/{token}")
    public String confirm(@PathVariable(value = "token") String token){
      return registrationService.confirmToken(token);
    }

}
