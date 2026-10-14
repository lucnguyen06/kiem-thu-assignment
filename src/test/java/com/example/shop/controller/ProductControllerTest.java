package com.example.shop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

/** Test case CT-PRD-xx (controller CRUD san pham, can dang nhap). */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private MockHttpSession loggedIn() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(AuthController.SESSION_USER, "alice");
        return session;
    }

    @Test
    @DisplayName("CT-PRD-01: chua dang nhap -> /products chuyen toi /login")
    void should_RedirectToLogin_When_ListWithoutSession() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("CT-PRD-02: da dang nhap -> hien danh sach san pham")
    void should_ShowProducts_When_LoggedIn() throws Exception {
        when(productService.findAll()).thenReturn(List.of(new Product("Ao", 100, 3)));

        mockMvc.perform(get("/products").session(loggedIn()))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("currentUser", "alice"));
    }

    @Test
    @DisplayName("CT-PRD-03: tao san pham hop le -> luu va chuyen ve danh sach")
    void should_CreateProduct_When_DataValid() throws Exception {
        mockMvc.perform(post("/products").session(loggedIn())
                        .param("name", "Ao").param("price", "100").param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).create(any(Product.class));
    }

    @Test
    @DisplayName("CT-PRD-04: tao san pham ten rong, gia 0, so luong am -> o lai form, khong luu")
    void should_ReturnForm_When_CreateInvalid() throws Exception {
        mockMvc.perform(post("/products").session(loggedIn())
                        .param("name", "").param("price", "0").param("quantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeHasFieldErrors("product", "name", "price", "quantity"));

        verify(productService, never()).create(any());
    }

    @Test
    @DisplayName("CT-PRD-05: chua dang nhap khong duoc tao san pham")
    void should_RedirectToLogin_When_CreateWithoutSession() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Ao").param("price", "100").param("quantity", "3"))
                .andExpect(redirectedUrl("/login"));

        verify(productService, never()).create(any());
    }

    @Test
    @DisplayName("CT-PRD-06: form sua voi id ton tai -> hien form")
    void should_ShowEditForm_When_IdExists() throws Exception {
        when(productService.findById(1L)).thenReturn(new Product("Ao", 100, 3));

        mockMvc.perform(get("/products/1/edit").session(loggedIn()))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"));
    }

    @Test
    @DisplayName("CT-PRD-07: form sua voi id khong ton tai -> ve danh sach")
    void should_RedirectToList_When_EditIdMissing() throws Exception {
        when(productService.findById(99L)).thenThrow(new NoSuchElementException("x"));

        mockMvc.perform(get("/products/99/edit").session(loggedIn()))
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    @DisplayName("CT-PRD-08: cap nhat hop le -> goi service.update")
    void should_UpdateProduct_When_DataValid() throws Exception {
        mockMvc.perform(post("/products/1").session(loggedIn())
                        .param("name", "Ao moi").param("price", "200").param("quantity", "5"))
                .andExpect(redirectedUrl("/products"));

        verify(productService).update(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("CT-PRD-09: xoa san pham -> goi service.delete")
    void should_DeleteProduct_When_LoggedIn() throws Exception {
        mockMvc.perform(post("/products/1/delete").session(loggedIn()))
                .andExpect(redirectedUrl("/products"));

        verify(productService).delete(1L);
    }

    @Test
    @DisplayName("CT-PRD-10: xoa san pham da bi xoa truoc do -> khong loi, ve danh sach")
    void should_Ignore_When_DeleteMissing() throws Exception {
        org.mockito.Mockito.doThrow(new NoSuchElementException("x")).when(productService).delete(7L);

        mockMvc.perform(post("/products/7/delete").session(loggedIn()))
                .andExpect(redirectedUrl("/products"));
    }
}
