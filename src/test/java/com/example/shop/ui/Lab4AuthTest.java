package com.example.shop.ui;

import com.example.shop.ui.pages.RegisterPage;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Lab 4: Page Object Model - Auth Tests
 * 
 * Test dang ky va dang nhap su dung Page Object Pattern
 */
public class Lab4AuthTest extends Lab4BaseTest {

    // ==================== DANG KY ====================
    
    @Test(groups = {"smoke", "lab4"})
    public void should_RegisterSuccessfully_When_DataValid() {
        String username = "user" + System.currentTimeMillis();
        
        registerPage.open(baseUrl);
        registerPage.register(username, username + "@mail.com", "secret1");
        
        // Page Object: kiem tra trang thai
        assertTrue(registerPage.isSuccessDisplayed(), 
                "Thong bao thanh cong phai hien thi sau khi dang ky");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_UsernameTooShort() {
        registerPage.open(baseUrl);
        registerPage.register("ab", "a@b.com", "secret1");
        
        assertTrue(registerPage.isErrorDisplayed());
        assertEquals(registerPage.getErrorMessage(), "Username phai tu 3 den 50 ky tu");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_PasswordTooShort() {
        registerPage.open(baseUrl);
        registerPage.register("validuser", "a@b.com", "123");
        
        assertTrue(registerPage.isErrorDisplayed());
        assertEquals(registerPage.getErrorMessage(), "Password phai co it nhat 6 ky tu");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_EmailInvalid() {
        registerPage.open(baseUrl);
        registerPage.register("validuser", "not-an-email", "secret1");
        
        assertTrue(registerPage.isErrorDisplayed());
        assertEquals(registerPage.getErrorMessage(), "Email khong hop le");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_NavigateToLogin_When_ClickLoginLink() {
        registerPage.open(baseUrl);
        registerPage.clickLoginLink();
        
        assertTrue(loginPage.isOnLoginPage());
    }
    
    // ==================== DANG NHAP ====================
    
    @Test(groups = {"smoke", "lab4"})
    public void should_LoginSuccessfully_When_CredentialsValid() {
        // Tao tai khoan truoc
        String username = createAccountAndLogin();
        
        // Kiem tra da o trang products
        assertTrue(productsPage.isOnProductsPage());
        assertEquals(productsPage.getCurrentUser(), username);
    }
    
    @Test(groups = {"smoke", "lab4"})
    public void should_ShowError_When_WrongPassword() {
        String username = createAccountAndLogin();
        
        // Dang xuat (refresh trang se quay ve login vi chua co session)
        loginPage.open(baseUrl);
        loginPage.login(username, "wrongpassword");
        
        assertTrue(loginPage.isErrorDisplayed());
        assertEquals(loginPage.getErrorMessage(), "Dang nhap khong thanh cong");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_UserNotExists() {
        loginPage.open(baseUrl);
        loginPage.login("nonexistent_user_12345", "anypassword");
        
        assertTrue(loginPage.isErrorDisplayed());
        assertEquals(loginPage.getErrorMessage(), "Dang nhap khong thanh cong");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_RedirectToLogin_When_AccessProductsWithoutAuth() {
        productsPage.open(baseUrl);
        
        assertTrue(productsPage.isRedirectedToLogin());
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_NavigateToRegister_When_ClickRegisterLink() {
        loginPage.open(baseUrl);
        loginPage.clickRegisterLink();
        
        assertTrue(registerPage.isOnRegisterPage());
    }
    
    // ==================== DATA PROVIDER ====================
    
    @DataProvider(name = "invalidRegisterData")
    public Object[][] invalidRegisterData() {
        return new Object[][] {
            // username, email, password, expectedError
            {"ab", "a@b.com", "secret1", "Username phai tu 3 den 50 ky tu"},
            {"validuser1", "a@b.com", "12345", "Password phai co it nhat 6 ky tu"},
            {"validuser2", "notemail", "secret1", "Email khong hop le"},
            {"", "a@b.com", "secret1", "Username khong duoc rong"},
            {"   ", "a@b.com", "secret1", "Username khong duoc rong"},
        };
    }
    
    @Test(groups = {"regression", "lab4"}, dataProvider = "invalidRegisterData")
    public void should_ShowError_When_RegisterDataInvalid(String username, String email, 
            String password, String expectedError) {
        registerPage.open(baseUrl);
        registerPage.register(username, email, password);
        
        assertTrue(registerPage.isErrorDisplayed());
        assertEquals(registerPage.getErrorMessage(), expectedError);
    }
}
