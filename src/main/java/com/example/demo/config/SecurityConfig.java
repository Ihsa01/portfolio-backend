package com.example.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define your custom security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf ->csrf.disable())
                .cors(withDefaults())
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()  // Public Endpoints
                                .requestMatchers("/admin", "/dashboard").authenticated()
                .anyRequest().permitAll()
                )
                .formLogin(withDefaults())// Disable form login, we are using HTTP Basic authentication
                .httpBasic(withDefaults());   // Enable Basic Authentication
        return http.build();
    }

    // Define a password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
