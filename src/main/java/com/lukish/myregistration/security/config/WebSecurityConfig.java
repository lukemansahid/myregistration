package com.lukish.myregistration.security.config;

import com.lukish.myregistration.security.PasswordEncoder;
import com.lukish.myregistration.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService; //constructor dependency injection by lombok
   // private final PasswordEncoder passwordEncoder; //constructor dependency injection by lombok
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //constructor dependency injection by lombok


    // this method is use for authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/*", "/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }


    // this method is use for authentication

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

         auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();



        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;

    }
}
