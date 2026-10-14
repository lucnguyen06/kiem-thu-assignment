package com.example.shop.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Lab 4: Page Object Model
 * 
 * Trang form san pham - dai dien cho trang /products/new va /products/edit/{id}
 */
public class ProductFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // ===== Locators =====
    private final By nameInput = By.id("name");
    private final By priceInput = By.id("price");
    private final By quantityInput = By.id("quantity");
    private final By saveButton = By.id("save-btn");
    private final By cancelButton = By.id("cancel-btn");
    private final By fieldErrors = By.className("field-error");
    private final By formTitle = By.cssSelector("h2");
    
    public ProductFormPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Mo trang form (tao moi hoac chinh sua)
     */
    public void open(String baseUrl, String path) {
        driver.get(baseUrl + path);
    }
    
    /**
     * Nhap thong tin san pham
     */
    public void enterName(String name) {
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
    }
    
    public void enterPrice(String price) {
        driver.findElement(priceInput).clear();
        driver.findElement(priceInput).sendKeys(price);
    }
    
    public void enterQuantity(String quantity) {
        driver.findElement(quantityInput).clear();
        driver.findElement(quantityInput).sendKeys(quantity);
    }
    
    /**
     * Nut luu
     */
    public void clickSave() {
        driver.findElement(saveButton).click();
    }
    
    /**
     * Nut huy
     */
    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }
    
    /**
     * Dien day du thong tin san pham
     */
    public void fillForm(String name, String price, String quantity) {
        enterName(name);
        enterPrice(price);
        enterQuantity(quantity);
        clickSave();
    }
    
    /**
     * Lay loi cua truong name
     */
    public String getNameError() {
        try {
            WebElement nameField = driver.findElement(nameInput);
            WebElement parent = nameField.findElement(By.xpath(".."));
            WebElement error = parent.findElement(fieldErrors);
            return error.getText();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }
    
    /**
     * Lay loi cua truong price
     */
    public String getPriceError() {
        try {
            WebElement priceField = driver.findElement(priceInput);
            WebElement parent = priceField.findElement(By.xpath(".."));
            WebElement error = parent.findElement(fieldErrors);
            return error.getText();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }
    
    /**
     * Lay loi cua truong quantity
     */
    public String getQuantityError() {
        try {
            WebElement qtyField = driver.findElement(quantityInput);
            WebElement parent = qtyField.findElement(By.xpath(".."));
            WebElement error = parent.findElement(fieldErrors);
            return error.getText();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return "";
        }
    }
    
    /**
     * Dem so luong loi tren form
     */
    public int getFieldErrorCount() {
        return driver.findElements(fieldErrors).size();
    }
    
    /**
     * Kiem tra co loi validation khong
     */
    public boolean hasValidationErrors() {
        return getFieldErrorCount() > 0;
    }
    
    /**
     * Lay URL hien tai
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Kiem tra co phai trang tao moi khong
     */
    public boolean isCreatePage() {
        return getCurrentUrl().contains("/products/new") || getCurrentUrl().endsWith("/products");
    }
    
    /**
     * Kiem tra co phai trang chinh sua khong
     */
    public boolean isEditPage() {
        return getCurrentUrl().contains("/products/edit");
    }
    
    /**
     * Lay gia tri hien tai cua truong name
     */
    public String getNameValue() {
        return driver.findElement(nameInput).getAttribute("value");
    }
    
    /**
     * Lay gia tri hien tai cua truong price
     */
    public String getPriceValue() {
        return driver.findElement(priceInput).getAttribute("value");
    }
    
    /**
     * Lay gia tri hien tai cua truong quantity
     */
    public String getQuantityValue() {
        return driver.findElement(quantityInput).getAttribute("value");
    }
}
