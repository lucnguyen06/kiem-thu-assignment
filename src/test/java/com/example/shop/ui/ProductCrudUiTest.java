package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Y4: CRUD san pham bang Selenium su dung Page Object Model. */
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

    @Test(groups = {"regression", "product"})
    public void should_ShowFieldErrors_When_CreateInvalid() {
        productsPage.clickAddProduct();
        productFormPage.fillForm("x", "0", "-1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("field-error")));
        assertTrue(productFormPage.getFieldErrorCount() >= 2);
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
        assertTrue(!productsPage.isProductExists("San pham xoa"));
    }
}
