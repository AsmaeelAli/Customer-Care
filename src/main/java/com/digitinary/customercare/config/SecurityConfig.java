package com.digitinary.customercare.config;

import com.digitinary.customercare.security.CustomerDetails;
import com.digitinary.customercare.security.JwtAuthFilter;
import com.digitinary.customercare.security.JwtService;
import com.digitinary.customercare.security.SystemUserDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtService jwtService,
                                                   @Qualifier("customerDetails") UserDetailsService customerDetails,
                                                   @Qualifier("systemUserDetails") UserDetailsService systemUserDetails) throws Exception {

        // Created with "new" on purpose; see the note in JwtAuthFilter
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(customerDetails, systemUserDetails, jwtService);

        http

                .csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // عمووو سواجر خليه
                        // عملي عقدة نفسية في الكود كله وهو اخرني يومين كاملين لحالهم ! "/error"
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**", "/error").permitAll()

                        // فقط هذول لازم نعملهم تخطي في السلسلة لانه معمهش توكن من الاساس
                        .requestMatchers("/auth/customers/**","/auth/system-users/login").permitAll()

                        // الاشي الويحد الي بقدر يعمله الكستمر في الكنترولر تبعه هو تعديل بياناته على حسب اسمه في نفس التوكن
                        .requestMatchers(HttpMethod.PATCH,"/api/tickets","/api/orders/**").hasAnyRole("ADMIN", "CUSTOMER")
                        // صلاحيات الادمن
                        .requestMatchers("/api/system-users/**", "/auth/system-users", "/api/customers/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .exceptionHandling(e -> e.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    /**
     * DAO = Data Access Object
     * Authentication = عملية تسجيل الدخول والتحقق
     * Provider = الجهة التي تنفذ عملية التحقق
     * */

    @Bean
    @Primary
    public AuthenticationManager customerAuthenticationManager(
            CustomerDetails customerDetails,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(customerDetails);

        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationManager systemUserAuthenticationManager(
            SystemUserDetails systemUserDetails,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(passwordEncoder);

        provider.setUserDetailsService(systemUserDetails);

        return  new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
