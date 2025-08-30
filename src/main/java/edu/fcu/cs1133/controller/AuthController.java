package edu.fcu.cs1133.controller;

// ... imports (AuthenticationManager, SecurityContextHolder, etc.)
import edu.fcu.cs1133.payload.request.LoginRequest;
import edu.fcu.cs1133.payload.response.JwtAuthenticationResponse;
import edu.fcu.cs1133.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtTokenProvider tokenProvider;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

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

    // 返回 JWT
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  // 可以在這裡添加註冊 (/register) 的端點
}