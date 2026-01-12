package com.example.aichat.controller;

import com.example.aichat.dto.UserRequest;
import com.example.aichat.entity.User;
import com.example.aichat.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testListUsers() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("users", Collections.emptyList());
        mockResponse.put("total", 0);

        Mockito.when(userService.list(1, 10)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.users").isArray())
                .andExpect(jsonPath("$.data.total").value(0));
    }

    @Test
    void testCreateUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Mockito.when(userService.create(Mockito.any(UserRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    // Additional tests for update, delete, and detail endpoints can be added here.
}
