package shop.campustable.campustablebeclone.global.config;

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

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
