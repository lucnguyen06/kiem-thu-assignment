package com.example.shop.ui;

import com.example.shop.ui.pages.ProductFormPage;
import com.example.shop.ui.pages.ProductsPage;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Lab 4: Page Object Model - Product CRUD Tests
 * 
 * Test CRUD san pham su dung Page Object Pattern
 */
public class Lab4ProductTest extends Lab4BaseTest {

    @Test(groups = {"smoke", "lab4"})
    public void should_CreateProduct_When_FormValid() {
        createAccountAndLogin();
        
        String productName = "Ao thun dep";
        productsPage.clickAddProduct();
        productFormPage.fillForm(productName, "150000", "10");
        
        // Page Object: kiem tra san pham da duoc tao
        assertTrue(productsPage.isProductExists(productName));
        
        // Kiem tra thong tin
        ProductsPage.ProductInfo info = productsPage.getProductInfo(productName);
        assertEquals(info.price(), "150000");
        assertEquals(info.quantity(), "10");
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowFieldErrors_When_FormInvalid() {
        createAccountAndLogin();
        
        productsPage.clickAddProduct();
        productFormPage.fillForm("x", "0", "-1");
        
        // Page Object: kiem tra validation errors
        assertTrue(productFormPage.hasValidationErrors());
        assertTrue(productFormPage.getFieldErrorCount() >= 2);
    }
    
    @Test(groups = {"smoke", "lab4"})
    public void should_UpdateProduct_When_EditSaved() {
        createAccountAndLogin();
        
        // Tao san pham
        String originalName = "Quan jean";
        productsPage.clickAddProduct();
        productFormPage.fillForm(originalName, "300000", "5");
        assertTrue(productsPage.isProductExists(originalName));
        
        // Sua san pham
        productsPage.clickEditProduct(originalName);
        String newName = "Quan jean v2";
        productFormPage.fillForm(newName, "350000", "7");
        
        // Kiem tra san pham da duoc cap nhat
        assertTrue(productsPage.isProductExists(newName));
        assertFalse(productsPage.isProductExists(originalName));
        
        ProductsPage.ProductInfo info = productsPage.getProductInfo(newName);
        assertEquals(info.price(), "350000");
        assertEquals(info.quantity(), "7");
    }
    
    @Test(groups = {"smoke", "lab4"})
    public void should_DeleteProduct_When_DeleteClicked() {
        createAccountAndLogin();
        
        // Tao san pham
        String productName = "San pham xoa";
        productsPage.clickAddProduct();
        productFormPage.fillForm(productName, "1000", "1");
        assertTrue(productsPage.isProductExists(productName));
        
        // Xoa san pham
        productsPage.deleteProduct(productName);
        
        // Kiem tra san pham da bi xoa
        assertFalse(productsPage.isProductExists(productName));
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_NameEmpty() {
        createAccountAndLogin();
        
        productsPage.clickAddProduct();
        productFormPage.enterName("");
        productFormPage.enterPrice("100000");
        productFormPage.enterQuantity("5");
        productFormPage.clickSave();
        
        assertTrue(productFormPage.hasValidationErrors());
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_PriceZero() {
        createAccountAndLogin();
        
        productsPage.clickAddProduct();
        productFormPage.enterName("San pham");
        productFormPage.enterPrice("0");
        productFormPage.enterQuantity("5");
        productFormPage.clickSave();
        
        assertTrue(productFormPage.hasValidationErrors());
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ShowError_When_QuantityNegative() {
        createAccountAndLogin();
        
        productsPage.clickAddProduct();
        productFormPage.enterName("San pham");
        productFormPage.enterPrice("100000");
        productFormPage.enterQuantity("-1");
        productFormPage.clickSave();
        
        assertTrue(productFormPage.hasValidationErrors());
    }
    
    @Test(groups = {"regression", "lab4"})
    public void should_ListMultipleProducts() {
        createAccountAndLogin();
        
        // Tao nhieu san pham
        productsPage.clickAddProduct();
        productFormPage.fillForm("San pham 1", "100000", "10");
        
        productsPage.clickAddProduct();
        productFormPage.fillForm("San pham 2", "200000", "20");
        
        productsPage.clickAddProduct();
        productFormPage.fillForm("San pham 3", "300000", "30");
        
        // Kiem tra so luong san pham
        assertEquals(productsPage.getProductCount(), 3);
        
        // Kiem tra danh sach ten
        assertTrue(productsPage.getAllProductNames().contains("San pham 1"));
        assertTrue(productsPage.getAllProductNames().contains("San pham 2"));
        assertTrue(productsPage.getAllProductNames().contains("San pham 3"));
    }
}
