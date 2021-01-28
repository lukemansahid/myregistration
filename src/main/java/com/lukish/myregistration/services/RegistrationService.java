package com.lukish.myregistration.services;

import com.lukish.myregistration.models.AppUser;
import com.lukish.myregistration.models.AppUserRole;
import com.lukish.myregistration.models.RegistrationRequest;
import com.lukish.myregistration.models.token.ConfirmationToken;
import com.lukish.myregistration.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest registrationRequest) {

        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        return appUserService.signUpUser( new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        AppUserRole.USER )
        );
    }

    @Transactional
    public String confirmToken(String token){
       ConfirmationToken confirmationToken= confirmationTokenService.getToken(token).orElseThrow(()->
                new  IllegalStateException("Token not found"));

       // check if token is confirmed
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredDate = confirmationToken.getExpiredAt();

        // check if token is not expired yet
        if (expiredDate.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("This token is expired");
        }

        confirmationTokenService.updateConfirmedAt(token);
        appUserService.enableAppuser(confirmationToken.getAppUser().getEmail());

        return "Confirmed";
    }


}
