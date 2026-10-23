package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** Y4: dang ky / dang nhap bang Selenium su dung Page Object Model. */
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

    @DataProvider(name = "badRegister")
    public Object[][] badRegister() {
        return new Object[][] {
            {"ab", "a@b.com", "secret1", "Username phai tu 3 den 50 ky tu"},
            {"validuser1", "a@b.com", "123", "Password phai co it nhat 6 ky tu"},
            {"validuser2", "not-an-email", "secret1", "Email khong hop le"},
        };
    }

    @Test(groups = {"regression", "auth"}, dataProvider = "badRegister")
    public void should_ShowError_When_RegisterInvalid(String u, String e, String p, String expected) {
        registerPage.open(baseUrl);
        registerPage.register(u, e, p);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(registerPage.getErrorMessage(), expected);
    }

    @Test(groups = {"smoke", "auth"})
    public void should_ShowError_When_LoginWrongPassword() {
        loginPage.open(baseUrl);
        loginPage.login("nouser", "wrongpass");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(loginPage.getErrorMessage(), "Dang nhap khong thanh cong");
    }

    @Test(groups = {"regression", "auth"})
    public void should_RedirectToLogin_When_OpenProductsWithoutLogin() {
        productsPage.open(baseUrl);
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(loginPage.isOnLoginPage());
    }
}
