package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Y4: Kiem thu tu dong CRUD san pham bang Selenium WebDriver (Page Object Model).
 */
public class ProductCrudUiTest extends BaseTest {

    @BeforeMethod(dependsOnMethods = "openBrowser", alwaysRun = true)
    public void registerAndLogin() {
        String user = "u" + UUID.randomUUID().toString().substring(0, 8);
        registerPage.open(baseUrl);
        registerPage.register(user, user + "@mail.com", "secret1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-btn")));
        loginPage.login(user, "secret1");
        wait.until(ExpectedConditions.urlContains("/products"));
    }

    private By rowOf(String name) {
        return By.cssSelector("tr[data-name='" + name + "']");
    }

    @Test(groups = {"smoke", "product"})
    public void should_CreateProduct_When_FormValid() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("Ao thun", "150000", "10");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Ao thun")));
        assertEquals(productsPage.getProductInfo("Ao thun").price(), "150000");
    }

    @Test(groups = {"smoke", "product"})
    public void should_CreateProduct_WithBoundaryValues() {
        productsPage.clickAddProduct();
        // Bien toi thieu: price = 1, quantity = 0
        productFormPage.fillForm("San pham bien", "1", "0");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("San pham bien")));
        assertEquals(productsPage.getProductInfo("San pham bien").price(), "1");
        assertEquals(productsPage.getProductInfo("San pham bien").quantity(), "0");
    }

    @Test(groups = {"regression", "product"})
    public void should_ShowFieldErrors_When_CreateInvalid() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("x", "0", "-1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("field-error")));
        assertTrue(productFormPage.getFieldErrorCount() >= 2);
    }

    @Test(groups = {"regression", "product"})
    public void should_ShowFieldErrors_When_NameEmpty() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("", "100000", "5");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("field-error")));
        assertTrue(productFormPage.hasValidationErrors());
    }

    @Test(groups = {"regression", "product"})
    public void should_CancelCreateProduct_When_CancelClicked() {
        productsPage.clickAddProduct();
        wait.until(ExpectedConditions.urlContains("/products/new"));
        productFormPage.enterName("San pham huy");
        productFormPage.enterPrice("50000");
        productFormPage.enterQuantity("2");
        productFormPage.clickCancel();

        wait.until(ExpectedConditions.urlContains("/products"));
        assertTrue(productsPage.isOnProductsPage());
        assertFalse(productsPage.isProductExists("San pham huy"));
    }

    @Test(groups = {"regression", "product"})
    public void should_DisplayPreFilledData_When_OpenEditForm() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("SanPhamPrefill", "250000", "12");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("SanPhamPrefill")));

        productsPage.clickEditProduct("SanPhamPrefill");
        wait.until(ExpectedConditions.urlContains("/edit"));
        assertEquals(productFormPage.getNameValue(), "SanPhamPrefill");
        assertEquals(productFormPage.getPriceValue(), "250000");
        assertEquals(productFormPage.getQuantityValue(), "12");
    }

    @Test(groups = {"regression", "product"})
    public void should_UpdateProduct_When_EditSaved() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("Quan jean", "300000", "5");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Quan jean")));

        productsPage.clickEditProduct("Quan jean");
        productFormPage.fillForm("Quan jean moi", "350000", "7");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Quan jean moi")));
        assertEquals(productsPage.getProductInfo("Quan jean moi").quantity(), "7");
    }

    @Test(groups = {"smoke", "product"})
    public void should_DeleteProduct_When_DeleteClicked() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("San pham xoa", "1000", "1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("San pham xoa")));

        productsPage.clickDeleteProduct("San pham xoa");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowOf("San pham xoa")));
        assertFalse(productsPage.isProductExists("San pham xoa"));
    }
}
