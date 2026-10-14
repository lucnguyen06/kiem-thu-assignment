package com.example.shop.ui;

import org.testng.annotations.DataProvider;

/**
 * Lab 6: Data Provider cho Annotation Test
 * 
 * Cung cap du lieu cho cac @Test methods
 */
public class Lab6DataProvider {

    /**
     * Lab 6 - Bai 7: User credentials data provider
     */
    @DataProvider(name = "userCredentials")
    public static Object[][] userCredentials() {
        return new Object[][] {
            // username, password, shouldPass
            {"alice", "secret1", true},
            {"bob", "password123", true},
            {"charlie", "wrongpass", false},
            {"", "", false},
            {"admin", "admin123", false}  // User not exist
        };
    }
    
    /**
     * Lab 6 - Bai 8: Product data provider
     */
    @DataProvider(name = "productData")
    public static Object[][] productData() {
        return new Object[][] {
            // name, price, quantity, operation
            {"Ao thun", 150000, 10, "CREATE"},
            {"Quan jean", 300000, 5, "CREATE"},
            {"Giay the thao", 500000, 3, "CREATE"},
            {"Mu non", 100000, 20, "CREATE"}
        };
    }
    
    /**
     * Lab 6 - Bai 9: Invalid data provider
     */
    @DataProvider(name = "invalidProductData")
    public static Object[][] invalidProductData() {
        return new Object[][] {
            // name, price, quantity, expectedError
            {"", 100000, 10, "Name cannot be empty"},
            {"San pham", 0, 10, "Price must be >= 1"},
            {"San pham", 100000, -1, "Quantity must be >= 0"},
            {"x", 100000, 10, "Name too short"}
        };
    }
    
    /**
     * Lab 6 - Bai 10: Cross-browser testing data
     */
    @DataProvider(name = "browsers")
    public static Object[][] browsers() {
        return new Object[][] {
            {"chrome", "latest", "Windows"},
            {"firefox", "latest", "Windows"},
            {"edge", "latest", "Windows"}
        };
    }
}
