package mx.santander.h2h.monitoreoapi.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  // MODO ABIERTO (DEV) – default
  @Bean
  @ConditionalOnProperty(name = "app.security.open", havingValue = "true", matchIfMissing = true)
  SecurityFilterChain openChain(HttpSecurity http) throws Exception {
    return http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(a -> a
            .requestMatchers("/", "/api/status", "/actuator/health/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().permitAll()
        )
        .build();
  }

  // MODO SEGURO (PRE/PROD simple con BASIC) – se activa con env
  @Bean
  @ConditionalOnProperty(name = "app.security.open", havingValue = "false")
  SecurityFilterChain basicSecureChain(HttpSecurity http) throws Exception {
    return http
        .cors(c -> {})
        .csrf(csrf -> csrf.disable())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(a -> a
            .requestMatchers("/", "/api/status", "/actuator/health/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .build();
  }
}
