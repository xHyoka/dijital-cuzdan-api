package com.tunahan.starter.controller.AuthControllerImpl;

import com.tunahan.starter.DTO.LoginRequest;
import com.tunahan.starter.DTO.LoginResponse;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.controller.IAuthController;
import com.tunahan.starter.model.User;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements IAuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody UserRegisterDto userRegisterDto){
    if (userRepository.findByUsername(userRegisterDto.getUsername()).isPresent()){
    return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten alınmış");
    }
    User user = User.builder()
            .username(userRegisterDto.getUsername())
            .password(passwordEncoder.encode(userRegisterDto.getPassword()))
            .role("ROLE_USER")
            .email(userRegisterDto.getEmail())
            .tcKimlikNo(userRegisterDto.getTcKimlikNo())
            .build();
    userRepository.save(user);
    return ResponseEntity.ok("Kayıt başarılı");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
    authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));
    String token = jwtUtil.GenerateToken(user.getUsername(),user.getRole());

    return ResponseEntity.ok(new LoginResponse(token));
    }
}
