package com.example.shop.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Y4: CRUD san pham bang Selenium (Create, Read, Update, Delete). */
public class ProductCrudUiTest extends BaseTest {

    @BeforeMethod(dependsOnMethods = "openBrowser", alwaysRun = true)
    public void registerAndLogin() {
        String user = "u" + UUID.randomUUID().toString().substring(0, 8);
        driver.get(baseUrl + "/register");
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("email")).sendKeys(user + "@mail.com");
        driver.findElement(By.id("password")).sendKeys("secret1");
        driver.findElement(By.id("register-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-btn")));
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys("secret1");
        driver.findElement(By.id("login-btn")).click();
        wait.until(ExpectedConditions.urlContains("/products"));
    }

    private void fillForm(String name, String price, String qty) {
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys(price);
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys(qty);
        driver.findElement(By.id("save-btn")).click();
    }

    private By rowOf(String name) {
        return By.cssSelector("tr[data-name='" + name + "']");
    }

    @Test(groups = {"smoke", "product"})
    public void should_CreateProduct_When_FormValid() {
        driver.findElement(By.id("add-link")).click();
        fillForm("Ao thun", "150000", "10");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Ao thun")));
        assertEquals(driver.findElement(rowOf("Ao thun")).findElement(By.className("p-price")).getText(), "150000");
    }

    @Test(groups = {"regression", "product"})
    public void should_ShowFieldErrors_When_CreateInvalid() {
        driver.findElement(By.id("add-link")).click();
        fillForm("x", "0", "-1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("field-error")));
        assertTrue(driver.findElements(By.className("field-error")).size() >= 2);
    }

    @Test(groups = {"regression", "product"})
    public void should_UpdateProduct_When_EditSaved() {
        driver.findElement(By.id("add-link")).click();
        fillForm("Quan jean", "300000", "5");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Quan jean")));

        driver.findElement(rowOf("Quan jean")).findElement(By.className("edit-link")).click();
        fillForm("Quan jean moi", "350000", "7");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("Quan jean moi")));
        assertEquals(driver.findElement(rowOf("Quan jean moi")).findElement(By.className("p-qty")).getText(), "7");
    }

    @Test(groups = {"smoke", "product"})
    public void should_DeleteProduct_When_DeleteClicked() {
        driver.findElement(By.id("add-link")).click();
        fillForm("San pham xoa", "1000", "1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf("San pham xoa")));

        driver.findElement(rowOf("San pham xoa")).findElement(By.className("delete-btn")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowOf("San pham xoa")));
        assertTrue(driver.findElements(rowOf("San pham xoa")).isEmpty());
    }
}
