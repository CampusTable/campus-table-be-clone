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
        new JwtAuthenticationFilter(jwtTokenProvider,objectMapper); // 필터 생성

    http
        .csrf(AbstractHttpConfigurer::disable)
        // CSRF 공격 방지 기능 끔
        // REST API는 보통 토큰 쓰기 땜에 보통 비활성화
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // 세션 정책 STATELESS
        // 서버에 세션 저장 x , 모든 인증 토큰(JWT)로 하겠다

        .exceptionHandling(handling -> handling
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 로그인 안했을 경우
            .accessDeniedHandler(customAccessDeniedHandler)) // 권환이 없을 경우

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
            .anyRequest().authenticated()) // 나머지 요청은 로그인을 해야만 ㄱㄴ

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    // JWT 필터를 시큐리티 기본 로그인 필터 (UsernamePasswordAuthenticationFilter) 보다 앞에 배치
    // == 아이디/비번 찾기 전에 토큰이 있는지 부터 검사

    return http.build();
  }

}
