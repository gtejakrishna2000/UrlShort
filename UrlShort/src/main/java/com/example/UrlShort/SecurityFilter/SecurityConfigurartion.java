package com.example.UrlShort.SecurityFilter;

import com.example.UrlShort.JwtFilter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfigurartion {
    @Autowired
    JwtAuthFilter jwtAuthFilter;


    // security filter chain authorize the user
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // role based authentication only one user presents
       return   http.csrf().disable() // disable the csrf filter, its not required for us now.mentioned full details about csrf filter in document in security section
                .httpBasic(Customizer.withDefaults())  // basic login option
                .authorizeHttpRequests(req->req
//                        .requestMatchers("/tokenGenarate").permitAll()
//                        .requestMatchers("/AddCustomer").permitAll()
                        .requestMatchers("/customer/**").hasAuthority( "user") // make the authorization according to our  requirements
                        .anyRequest().permitAll()) // any request authenticate

                  .authenticationProvider(authenticationProvider())
                  .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .build();

    }




    @Bean // this bean is used to encode the password using BCryPasswordEncoder
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {


//      Create a new instance of DaoAuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Set the user details service to be used by the authentication provider
        authenticationProvider.setUserDetailsService(userDetailsService());

        // Set the password encoder to be used by the authentication provider
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        // Return the configured authentication provider
        return authenticationProvider;

    }

    UserDetailsServiceImp userDetailsService()
    {
        // Instantiate and return a new instance of the custom UserDetailsService implementation.
        return new UserDetailsServiceImp();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        // Retrieve the AuthenticationManager from the provided AuthenticationConfiguration
        return config.getAuthenticationManager();
    }
}
