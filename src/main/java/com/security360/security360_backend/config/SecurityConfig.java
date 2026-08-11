package com.security360.security360_backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.security360.security360_backend.security.JwtAuthenticationFilter;
import com.security360.security360_backend.security.OAuth2SuccessHandler;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final OAuth2SuccessHandler oauth2SuccessHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            OAuth2SuccessHandler oauth2SuccessHandler) {

        this.jwtFilter = jwtFilter;
        this.oauth2SuccessHandler = oauth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .cors(Customizer.withDefaults())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                    // 🟢 PUBLIC ENDPOINTS (React can fetch data WITHOUT Google Login redirect)
                    .requestMatchers(
                            "/api/auth/**",
                            "/api/dashboard/**",
                            "/api/employees/**",   // ✅ Allows Employees page to load
                            "/api/shifts/**",      // ✅ Allows Shift Management to load
                            "/api/attendance/**",  // ✅ Allows Attendance page to load
                            "/api/patrol/**",      // ✅ Allows Patrol Monitoring to load
                            "/api/tracking/**",    // ✅ Allows Live Tracking to load
                            "/api/guard/**",       // ✅ Allows Guard API access
                            "/api/incidents/**",
                            "/api/billing/**",
                            "/api/clients/**",
                            "/oauth2/**",
                            "/login/**",
                            "/error"
                    ).permitAll()
                    
                    // 🟢 PROTECTED ENDPOINTS (Only ADMIN can access these in the future)
                    .requestMatchers(
                            "/api/users/**",
                            "/api/settings/**"
                    ).hasRole("ADMIN")

                    // 🟢 ALL OTHER REQUESTS REQUIRE LOGIN
                    .anyRequest().authenticated()
            )

            .oauth2Login(oauth -> oauth
                    .successHandler(oauth2SuccessHandler))

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:8081"));

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}