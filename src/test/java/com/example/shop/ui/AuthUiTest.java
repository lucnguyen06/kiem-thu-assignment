package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Y4: Kiem thu tu dong Dang ky, Dang nhap va Dang xuat bang Selenium WebDriver (Page Object Model).
 */
public class AuthUiTest extends BaseTest {

    @Test(groups = {"smoke", "auth"})
    public void should_RegisterThenLogin_When_DataValid() {
        String user = "u" + UUID.randomUUID().toString().substring(0, 8);
        registerPage.open(baseUrl);
        registerPage.register(user, user + "@mail.com", "secret1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg-registered")));

        loginPage.login(user, "secret1");
        wait.until(ExpectedConditions.urlContains("/products"));
        assertEquals(productsPage.getCurrentUser(), user);
    }

    @Test(groups = {"smoke", "auth"})
    public void should_LoginSuccessfully_WithSeededAdminAccount() {
        loginPage.open(baseUrl);
        loginPage.login("admin", "admin123");
        wait.until(ExpectedConditions.urlContains("/products"));
        assertEquals(productsPage.getCurrentUser(), "admin");
    }

    @Test(groups = {"smoke", "auth"})
    public void should_LoginSuccessfully_WithSeededAliceAccount() {
        loginPage.open(baseUrl);
        loginPage.login("alice", "secret1");
        wait.until(ExpectedConditions.urlContains("/products"));
        assertEquals(productsPage.getCurrentUser(), "alice");
    }

    @Test(groups = {"smoke", "auth"})
    public void should_LogoutSuccessfully_When_ClickLogout() {
        loginPage.open(baseUrl);
        loginPage.login("admin", "admin123");
        wait.until(ExpectedConditions.urlContains("/products"));

        // Thuc hien dang xuat
        productsPage.logout();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(loginPage.isOnLoginPage());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg-logout")));

        // Kiem tra thu truy cap lai /products sau khi dang xuat
        productsPage.open(baseUrl);
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(loginPage.isOnLoginPage());
    }

    @Test(groups = {"smoke", "auth"})
    public void should_ShowError_When_LoginWrongPassword() {
        loginPage.open(baseUrl);
        loginPage.login("admin", "wrongpassword123");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(loginPage.getErrorMessage(), "Đăng nhập không thành công");
    }

    @Test(groups = {"regression", "auth"})
    public void should_ShowError_When_LoginNonExistentUser() {
        loginPage.open(baseUrl);
        loginPage.login("nonexistent_user_999", "secret1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(loginPage.getErrorMessage(), "Đăng nhập không thành công");
    }

    @Test(groups = {"regression", "auth"})
    public void should_RedirectToLogin_When_OpenProductsWithoutLogin() {
        productsPage.open(baseUrl);
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(loginPage.isOnLoginPage());
    }

    @Test(groups = {"regression", "auth"})
    public void should_NavigateBetweenLoginAndRegisterPages() {
        loginPage.open(baseUrl);
        loginPage.clickRegisterLink();
        wait.until(ExpectedConditions.urlContains("/register"));
        assertTrue(registerPage.isOnRegisterPage());

        registerPage.clickLoginLink();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(loginPage.isOnLoginPage());
    }

    @DataProvider(name = "badRegister")
    public Object[][] badRegister() {
        return new Object[][] {
            {"ab", "a@b.com", "secret1", "Username phai tu 3 den 50 ky tu"},
            {"a".repeat(51), "a@b.com", "secret1", "Username phai tu 3 den 50 ky tu"},
            {"validuser1", "a@b.com", "123", "Password phai co it nhat 6 ky tu"},
            {"validuser2", "not-an-email", "secret1", "Email khong hop le"},
            {"validuser3", "user@", "secret1", "Email khong hop le"},
        };
    }

    @Test(groups = {"regression", "auth"}, dataProvider = "badRegister")
    public void should_ShowError_When_RegisterInvalid(String u, String e, String p, String expected) {
        registerPage.open(baseUrl);
        registerPage.register(u, e, p);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(registerPage.getErrorMessage(), expected);
    }
}
