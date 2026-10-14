package com.example.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.shop.model.User;
import com.example.shop.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/** Test case CT-AUTH-xx (controller dang ky / dang nhap / dang xuat). */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("CT-AUTH-01: GET / chuyen huong sang /products")
    void should_RedirectToProducts_When_OpenRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    @DisplayName("CT-AUTH-02: dang nhap dung -> luu session va chuyen toi /products")
    void should_RedirectToProducts_When_LoginSuccess() throws Exception {
        when(userService.authenticate("alice", "secret1"))
                .thenReturn(Optional.of(new User("alice", "hash", "a@b.com")));

        mockMvc.perform(post("/login").param("username", "alice").param("password", "secret1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(request().sessionAttribute(AuthController.SESSION_USER, "alice"));
    }

    @Test
    @DisplayName("CT-AUTH-03: dang nhap sai -> o lai trang login kem thong bao loi")
    void should_ShowError_When_LoginFails() throws Exception {
        when(userService.authenticate("alice", "bad")).thenReturn(Optional.empty());

        mockMvc.perform(post("/login").param("username", "alice").param("password", "bad"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Dang nhap khong thanh cong"))
                .andExpect(request().sessionAttribute(AuthController.SESSION_USER, (Object) null));
    }

    @Test
    @DisplayName("CT-AUTH-04: dang ky hop le -> chuyen toi /login?registered")
    void should_RedirectToLogin_When_RegisterSuccess() throws Exception {
        when(userService.register("bob", "secret1", "b@c.com"))
                .thenReturn(new User("bob", "hash", "b@c.com"));

        mockMvc.perform(post("/register")
                        .param("username", "bob").param("password", "secret1").param("email", "b@c.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    @DisplayName("CT-AUTH-05: dang ky loi nghiep vu -> o lai form, giu lai username va email")
    void should_ShowError_When_RegisterInvalid() throws Exception {
        when(userService.register(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Username da ton tai"));

        mockMvc.perform(post("/register")
                        .param("username", "bob").param("password", "secret1").param("email", "b@c.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Username da ton tai"))
                .andExpect(model().attribute("username", "bob"))
                .andExpect(model().attribute("email", "b@c.com"));
    }

    @Test
    @DisplayName("CT-AUTH-06: dang xuat -> chuyen toi /login?logout")
    void should_RedirectToLogin_When_Logout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}
