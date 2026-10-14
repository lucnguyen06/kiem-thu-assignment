package com.example.shop.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Lab 4: Page Object Model
 * 
 * Trang danh sach san pham - dai dien cho trang /products
 */
public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // ===== Locators =====
    private final By addLink = By.id("add-link");
    private final By logoutLink = By.id("logout-link");
    private final By currentUser = By.id("current-user");
    private final By productTable = By.cssSelector("table tbody");
    private final By productRows = By.cssSelector("tr[data-name]");
    
    // Product row locators
    private final By productNameCell = By.className("p-name");
    private final By productPriceCell = By.className("p-price");
    private final By productQtyCell = By.className("p-qty");
    private final By editLink = By.className("edit-link");
    private final By deleteButton = By.className("delete-btn");
    
    public ProductsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Mo trang san pham
     */
    public void open(String baseUrl) {
        driver.get(baseUrl + "/products");
    }
    
    /**
     * Mo trang tao san pham moi
     */
    public void clickAddProduct() {
        driver.findElement(addLink).click();
    }
    
    /**
     * Dang xuat
     */
    public void logout() {
        driver.findElement(logoutLink).click();
    }
    
    /**
     * Lay ten nguoi dung hien tai
     */
    public String getCurrentUser() {
        return driver.findElement(currentUser).getText();
    }
    
    /**
     * Lay URL hien tai
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Kiem tra co dang o trang products khong
     */
    public boolean isOnProductsPage() {
        return getCurrentUrl().contains("/products");
    }
    
    /**
     * Kiem tra co dang o trang login khong (redirect)
     */
    public boolean isRedirectedToLogin() {
        return getCurrentUrl().contains("/login");
    }
    
    /**
     * Lay so luong san pham hien tai
     */
    public int getProductCount() {
        return driver.findElements(productRows).size();
    }
    
    /**
     * Lay danh sach ten tat ca san pham
     */
    public List<String> getAllProductNames() {
        List<WebElement> rows = driver.findElements(productRows);
        return rows.stream()
                .map(row -> row.findElement(productNameCell).getText())
                .toList();
    }
    
    /**
     * Tim hang chua san pham theo ten
     */
    private WebElement findRowByName(String name) {
        return driver.findElement(By.cssSelector("tr[data-name='" + name + "']"));
    }
    
    /**
     * Kiem tra san pham co ton tai khong
     */
    public boolean isProductExists(String name) {
        try {
            findRowByName(name);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Lay thong tin san pham theo ten
     */
    public ProductInfo getProductInfo(String name) {
        WebElement row = findRowByName(name);
        return new ProductInfo(
                name,
                row.findElement(productPriceCell).getText(),
                row.findElement(productQtyCell).getText()
        );
    }
    
    /**
     * Click nut sua san pham
     */
    public void clickEditProduct(String name) {
        WebElement row = findRowByName(name);
        row.findElement(editLink).click();
    }
    
    /**
     * Click nut xoa san pham
     */
    public void clickDeleteProduct(String name) {
        WebElement row = findRowByName(name);
        row.findElement(deleteButton).click();
    }
    
    /**
     * Xoa san pham bang ten
     */
    public void deleteProduct(String name) {
        clickDeleteProduct(name);
    }
    
    /**
     * Class luu thong tin san pham
     */
    public record ProductInfo(String name, String price, String quantity) {}
}
