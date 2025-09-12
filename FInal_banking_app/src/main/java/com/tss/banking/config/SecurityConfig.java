package com.tss.banking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the banking application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	
	
	
//	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//	
//	  @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .csrf(csrf -> csrf.disable())
//	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	            .authorizeHttpRequests(auth -> auth
//	                // swagger docs
//	                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//	                // public endpoints
//	                .requestMatchers("/api/auth/**", "/api/customers/register").permitAll()
//	                // h2 console (if needed)
//	                .requestMatchers("/h2-console/**").permitAll()
//	                // everything else
//	                .anyRequest().authenticated()
//	            )
//	            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // For H2 console
//	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//	        return http.build();
//	    }
//
//	    @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//	        return configuration.getAuthenticationManager();
//	    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/customers/register").permitAll().requestMatchers("/h2-console/**")
						.permitAll().anyRequest().authenticated())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // For H2 console
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
