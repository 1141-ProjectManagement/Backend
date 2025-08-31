package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.JwtAuthenticationResponse;
import edu.fcu.cs1133.payload.LoginRequest;
import edu.fcu.cs1133.security.CustomUserDetailsService;
import edu.fcu.cs1133.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Autowired
  JwtTokenProvider tokenProvider;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

    // --- 密碼驗證暫時關閉 ---
    // 1. 手動透過 officialId 載入 UserDetails
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getOfficialId());

    // 2. 建立一個已認證的 Authentication 物件 (無需密碼)，這會標記使用者為已登入
    Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

    // 3. 將 Authentication 物件設定到 SecurityContext 中
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 4. 生成 JWT
    String jwt = tokenProvider.generateToken(authentication);
    // --- 密碼驗證暫時關閉 ---

    /* --- 原本的驗證邏輯 ---
    // Spring Security 會使用我們的 CustomUserDetailsService 和 PasswordEncoder 來進行驗證
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getOfficialId(), // <-- 使用 officialId
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 生成 JWT
    String jwt = tokenProvider.generateToken(authentication);
    */

    // 返回 JWT
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  // 可以在這裡添加註冊 (/register) 的端點
}