package com.example.shop.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lab 4: Page Object Model
 * 
 * Trang dang nhap - dai dien cho trang /login
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // ===== Locators =====
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-btn");
    private final By errorMessage = By.id("error");
    private final By registerLink = By.linkText("Dang ky");
    
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Mo trang dang nhap
     */
    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");
    }
    
    /**
     * Nhap thong tin dang nhap
     */
    public void enterUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }
    
    public void enterPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }
    
    /**
     * Nut dang nhap
     */
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }
    
    /**
     * Dang nhap day du thong tin
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    
    /**
     * Lay thong bao loi
     */
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
    
    /**
     * Kiem tra co hien thi thong bao loi khong
     */
    public boolean isErrorDisplayed() {
        return driver.findElement(errorMessage).isDisplayed();
    }
    
    /**
     * Chuyen sang trang dang ky
     */
    public void clickRegisterLink() {
        driver.findElement(registerLink).click();
    }
    
    /**
     * Lay URL hien tai
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Kiem tra dang o trang dang nhap
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("/login");
    }
}
