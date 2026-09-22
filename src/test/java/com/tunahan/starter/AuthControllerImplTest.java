package com.tunahan.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunahan.starter.DTO.LoginRequest;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.model.User;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.security.JwtUtil;
import com.tunahan.starter.service.TokenBlacklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("register: Kayıt başarılı olmalı")
    void register_ShouldReturnSuccess() throws Exception {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("tunahan");
        dto.setPassword("1234");
        dto.setEmail("tunahan@mail.com");

        when(userRepository.findByUsername("tunahan")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("hashedPassword");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Kayıt başarılı"));
    }

    @Test
    @DisplayName("register: Kullanıcı adı alınmışsa 400 dönmeli")
    void register_WhenUsernameExists_ShouldReturn400() throws Exception {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("tunahan");
        dto.setPassword("1234");

        User existingUser = new User();
        existingUser.setUsername("tunahan");

        when(userRepository.findByUsername("tunahan")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bu kullanıcı adı zaten alınmış"));
    }

    @Test
    @DisplayName("login: Token dönmeli")
    void login_ShouldReturnToken() throws Exception {
        // Mock nesnelerini hazırla
        User user = new User();
        user.setUsername("tunahan");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("tunahan")).thenReturn(Optional.of(user));

        // generateToken 2 tane String aldığı için iki tane matcher veriyoruz:
        when(jwtUtil.generateToken(any(), any())).thenReturn("mockedToken");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("tunahan", "1234"));

        LoginRequest request = new LoginRequest();
        request.setUsername("tunahan");
        request.setPassword("1234");

        String jsonBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }

    @Test
    @DisplayName("logout: Çıkış yapmalı")
    void logout_ShouldReturnSuccess() throws Exception {
        String token = "mockedToken";
        long expiration = 60000L;

        when(jwtUtil.extractExpiration(token)).thenReturn(System.currentTimeMillis() + expiration);

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Çıkış yapıldı"));
    }
}