package edu.fcu.cs1133.config;

import edu.fcu.cs1133.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  // 注入 JWT 相關的 Bean (JwtAuthenticationEntryPoint, JwtAuthenticationFilter)

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      // 允許的前端來源
      configuration.setAllowedOrigins(List.of("http://localhost:5173"));
      // 允許的 HTTP 方法
      configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      // 允許的標頭
      configuration.setAllowedHeaders(List.of("*"));
      // 是否允許憑證
      configuration.setAllowCredentials(true);
      
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      // 對所有路徑套用此設定
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults()) // <-- 啟用 CORS 並使用預設設定 (會自動尋找 corsConfigurationSource Bean)
        .csrf(csrf -> csrf.disable()) // 禁用 CSRF，因為我們使用 JWT
        // .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 設置為無狀態 Session
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() // 允許所有對 /api/auth/** 的請求
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // 允許 Swagger
            .anyRequest().authenticated() // 其他所有請求都需要認證
        );

    // 添加我們的自訂 JWT filter
    // http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

