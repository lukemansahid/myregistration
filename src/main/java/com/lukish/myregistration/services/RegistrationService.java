package com.lukish.myregistration.services;

import com.lukish.myregistration.models.EmailValidator;
import com.lukish.myregistration.models.RegistrationRequest;
import com.lukish.myregistration.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest registrationRequest) {

        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        return "It works";
    }
}
