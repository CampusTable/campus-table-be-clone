package shop.campustable.campustablebeclone.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.campustable.campustablebeclone.domain.auth.provider.JwtTokenProvider;
import shop.campustable.campustablebeclone.domain.auth.security.CustomAccessDeniedHandler;
import shop.campustable.campustablebeclone.domain.auth.security.JwtAuthenticationEntryPoint;
import shop.campustable.campustablebeclone.domain.auth.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(jwtTokenProvider,objectMapper);

    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .exceptionHandling(handling -> handling
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler))

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/docs/swagger-ui/**",
                "/swagger-ui/**"
            ).permitAll()
            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
            .requestMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
            .requestMatchers("/error").permitAll()
            .anyRequest().authenticated())

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
