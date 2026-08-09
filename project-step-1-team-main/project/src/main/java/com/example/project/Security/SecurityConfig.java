package com.example.project.Security;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOidcUserService customOidcUserService; 
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

      public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService, CustomOidcUserService customOidcUserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.customOidcUserService = customOidcUserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;

    }
 @Bean
public CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration config = new CorsConfiguration();
  // match any localhost port:
  config.setAllowedOriginPatterns(List.of("http://localhost:*"));
  config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
  config.setAllowedHeaders(List.of("*"));
  config.setAllowCredentials(true);

  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  source.registerCorsConfiguration("/**", config);
  return source;
}
  
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      // 0) enable CORS support, picking up your corsConfigurationSource() bean
      .cors().and()

      // 1) we have only stateless JWT/API auth
      .csrf().disable()
      .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()

      // 2) when an unauthenticated request arrives, send 401 instead of redirecting
      .exceptionHandling()
         .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
      .and()

      // 3) open these endpoints to everyone
      .authorizeHttpRequests(auth -> auth
         .requestMatchers("/auth/register", "/auth/login").permitAll()
         .requestMatchers("/uploads/**").permitAll()
         .requestMatchers("/oauth2/**").permitAll()
         .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
         .anyRequest().authenticated()
      )

      // 4) do OAuth2 login (Google SSO in-browser)
      .oauth2Login(oauth2 -> oauth2
         .loginPage("/login")
         .userInfoEndpoint(u -> u.oidcUserService(customOidcUserService))
         .successHandler(oAuth2AuthenticationSuccessHandler)
         .failureHandler(oAuth2AuthenticationFailureHandler)
      )

      // 5) plug in our JWT filter
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}




    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
