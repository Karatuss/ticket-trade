//package com.example.CapstoneTestCouch.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated())
////                .formLogin((formLogin)-> formLogin
////                        .loginPage("/login")
////                        .defaultSuccessUrl("/"))
////                .logout((logout) -> logout
////                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
////                        .logoutSuccessUrl("/")
////                        .invalidateHttpSession(true))
//                .httpBasic(Customizer.withDefaults());
//        return httpSecurity.build();
//    }
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//
//
//}
