package com.lukish.myregistration.services;

import com.lukish.myregistration.models.AppUser;
import com.lukish.myregistration.models.token.ConfirmationToken;
import com.lukish.myregistration.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService  implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG= "User with email %s not found";

    // Dependency injection
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    // this method get the user by username for authentication process.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
    }

    // this method signup the user and send confirmation email
    public String signUpUser(AppUser appUser){

        // check the user first if already exist before signup
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists){
            throw new IllegalStateException("email already taken");
        }

        // encript the password of the new user before signup
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        // save user to the database
        appUserRepository.save(appUser);

        // this generate random token
        String generatedToken = UUID.randomUUID().toString();

        // instance of the ConfirmationToken class with variables
        ConfirmationToken confirmationToken = new ConfirmationToken(
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        // save the confirmation token in the database
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: SEND EMAIL WITH TOKEN

        return generatedToken; // return the generated token
    }


    public int enableAppuser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
