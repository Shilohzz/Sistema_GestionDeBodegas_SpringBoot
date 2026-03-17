package com.logitrack.logitrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login").permitAll()

                // Solo un ADMIN puede acceder a estas rutas y utilizarlas
                .requestMatchers("/auth/register").hasAuthority("ADMIN").requestMatchers("/api/usuario/**").hasAuthority("ADMIN")

                // Aquí limito la manipulación de endpoints en Cliente y Usuario, únicamente para el ADMIN.
                .requestMatchers(HttpMethod.POST, "/api/cliente/**").hasAuthority("ADMIN").requestMatchers(HttpMethod.PUT, "/api/cliente/**").hasAuthority("ADMIN").requestMatchers(HttpMethod.DELETE, "/api/cliente/**").hasAuthority("ADMIN")
                // Al empleado solo lo dejo ver la info de las personas que tengo en el sistema
                .requestMatchers(HttpMethod.GET, "/api/cliente/**").hasAnyAuthority("ADMIN", "EMPLEADO")

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/", "/index.html", "/css/**", "/js/**", "/view/**").permitAll().requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                // Todo lo demás (Productos, Bodegas, Movimientos, Inventario) queda abierto para ambos
                .anyRequest().authenticated()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "http://localhost:8080",
                "http://172.16.41.82:8080" // tengo que actualizar la ip para poder usar el programa.
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}