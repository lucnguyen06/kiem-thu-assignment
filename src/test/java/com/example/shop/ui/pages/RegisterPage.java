package com.example.shop.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Lab 4: Page Object Model
 * 
 * Trang dang ky - dai dien cho trang /register
 * 
 * Cac thanh phan:
 * - Locators: dinh nghia cac element tren trang
 * - Actions: cac thao tac co the thuc hien
 * - Page validation: kiem tra trang dang o dung trang thai
 */
public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // ===== Locators =====
    private final By usernameInput = By.id("username");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By registerButton = By.id("register-btn");
    private final By errorMessage = By.id("error");
    private final By successMessage = By.id("msg-registered");
    private final By loginLink = By.linkText("Dang nhap");
    
    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Mo trang dang ky
     */
    public void open(String baseUrl) {
        driver.get(baseUrl + "/register");
    }
    
    /**
     * Nhap thong tin dang ky
     */
    public void enterUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }
    
    public void enterEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
    }
    
    public void enterPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }
    
    /**
     * Nut dang ky
     */
    public void clickRegister() {
        driver.findElement(registerButton).click();
    }
    
    /**
     * Dang ky day du thong tin
     */
    public void register(String username, String email, String password) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        clickRegister();
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
     * Kiem tra co hien thi thong bao thanh cong khong
     */
    public boolean isSuccessDisplayed() {
        try {
            WebElement element = driver.findElement(successMessage);
            return element.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Chuyen sang trang dang nhap
     */
    public void clickLoginLink() {
        driver.findElement(loginLink).click();
    }
    
    /**
     * Lay URL hien tai
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Kiem tra dang o trang dang ky
     */
    public boolean isOnRegisterPage() {
        return getCurrentUrl().contains("/register");
    }
    
    /**
     * Lay gia tri placeholder cua input
     */
    public String getUsernamePlaceholder() {
        return driver.findElement(usernameInput).getAttribute("placeholder");
    }
    
    public String getEmailPlaceholder() {
        return driver.findElement(emailInput).getAttribute("placeholder");
    }
    
    public String getPasswordPlaceholder() {
        return driver.findElement(passwordInput).getAttribute("placeholder");
    }
}
