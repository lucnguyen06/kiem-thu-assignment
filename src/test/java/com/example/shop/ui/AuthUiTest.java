package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** Y4: dang ky / dang nhap bang Selenium. */
public class AuthUiTest extends BaseTest {

    private void register(String user, String email, String pass) {
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("register-btn")).click();
    }

    private void login(String user, String pass) {
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("login-btn")).click();
    }

    @Test(groups = {"smoke", "auth"})
    public void should_RegisterThenLogin_When_DataValid() {
        String user = "u" + UUID.randomUUID().toString().substring(0, 8);
        register(user, user + "@mail.com", "secret1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg-registered")));

        login(user, "secret1");
        wait.until(ExpectedConditions.urlContains("/products"));
        assertEquals(driver.findElement(By.id("current-user")).getText(), user);
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
        register(u, e, p);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(driver.findElement(By.id("error")).getText(), expected);
    }

    @Test(groups = {"smoke", "auth"})
    public void should_ShowError_When_LoginWrongPassword() {
        login("nouser", "wrongpass");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals(driver.findElement(By.id("error")).getText(), "Dang nhap khong thanh cong");
    }

    @Test(groups = {"regression", "auth"})
    public void should_RedirectToLogin_When_OpenProductsWithoutLogin() {
        driver.get(baseUrl + "/products");
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
