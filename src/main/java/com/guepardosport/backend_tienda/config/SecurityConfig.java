package com.guepardosport.backend_tienda.config;

import com.guepardosport.backend_tienda.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Agregamos ambas variaciones (con y sin /api) para evitar bloqueos 403
                        .requestMatchers("/api/auth/**", "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/prendas/**", "/prendas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/deportes/**", "/deportes/**").permitAll()
                        .requestMatchers("/api/pedidos/**", "/pedidos/**").permitAll()
                        .requestMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/prendas/**", "/prendas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prendas/**", "/prendas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prendas/**", "/prendas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/deportes/**", "/deportes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/deportes/**", "/deportes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/deportes/**", "/deportes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/configuracion", "/configuracion").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/configuracion", "/configuracion").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/banner-mensajes/**", "/banner-mensajes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/banner-mensajes/**", "/banner-mensajes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/banner-mensajes/**", "/banner-mensajes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/banner-mensajes/**", "/banner-mensajes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/imagenes-hero/**", "/imagenes-hero/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/imagenes-hero/**", "/imagenes-hero/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/imagenes-hero/**", "/imagenes-hero/**").hasRole("ADMIN")
                        .requestMatchers("/api/webhooks/**", "/webhooks/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // AQUÍ ESTÁ LA MAGIA: Permitimos Localhost Y tus enlaces de Netlify
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://verdant-concha-3f250e.netlify.app",
                "https://guepardo-sport-tienda.netlify.app"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}