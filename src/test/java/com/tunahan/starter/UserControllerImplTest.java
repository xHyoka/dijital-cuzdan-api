package com.tunahan.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunahan.starter.DTO.UserPasswordDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import com.tunahan.starter.controller.AccountControllerImpl.AccountControllerImpl;
import com.tunahan.starter.controller.AuthControllerImpl.AuthControllerImpl;
import com.tunahan.starter.controller.UserControllerImpl.UserControllerImpl;
import com.tunahan.starter.repository.AccountRepository;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.security.CustomUserDetailService;
import com.tunahan.starter.security.JwtAuthFilter;
import com.tunahan.starter.security.SecurityConfig;
import com.tunahan.starter.service.AccountServiceImpl.AccountServiceImpl;
import com.tunahan.starter.service.IUserService;
import com.tunahan.starter.service.TokenBlacklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountControllerImpl.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        AuthControllerImpl.class,
                        JwtAuthFilter.class,
                        CustomUserDetailService.class,
                        TokenBlacklistService.class
                }
        ))


@AutoConfigureMockMvc(addFilters = false)
public class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IUserService userService;

    @MockitoBean
    private AccountServiceImpl accountService;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("register: Kullanıcı kaydedilmeli")
    void register_ShouldReturnUser() throws Exception {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("tunahan");
        dto.setPassword("1234");
        dto.setEmail("tunahan@test.com");
        dto.setTcKimlikNo("12345678901");

        UserResponseDto response = new UserResponseDto();
        response.setUsername("tunahan");

        when(userService.register(any(UserRegisterDto.class))).thenReturn(response);

        mockMvc.perform(post("/user/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tunahan"));
    }

    @Test
    @DisplayName("register: Geçersiz veri gelirse hata dönmeli")
    void register_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
        UserRegisterDto dto = new UserRegisterDto();

        mockMvc.perform(post("/user/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("userInformation: Kullanıcı bilgileri dönmeli")
    void userInformation_ShouldReturnUserInfo() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setUsername("tunahan");

        when(userService.userInformation(1L)).thenReturn(response);

        mockMvc.perform(get("/user/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tunahan"));
    }

    @Test
    @DisplayName("password: Şifre güncellenmeli")
    void password_ShouldUpdatePassword() throws Exception {
        UserPasswordDto dto = new UserPasswordDto();
        dto.setOldPassword("1234");
        dto.setNewPassword("5678");

        doNothing().when(userService).password(eq(1), any(UserPasswordDto.class));

        mockMvc.perform(post("/user/api/password/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}