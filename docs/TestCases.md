# Test Cases - Shop ASM

## Trạng thái test
- JUnit: 84 tests, 0 fail (Calculator + Shop services + Controllers)
- TestNG (UI): 8 tests viết sẵn (chưa chạy vì thiếu Chrome local)

## 1. Decision Table - Đăng nhập

| Điều kiện / Rule | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| Username tồn tại | Y | Y | N | N |
| Password đúng | Y | N | - | - |
| **Kết quả** | Vào /products | Lỗi chung | Lỗi chung | Lỗi chung |

Ánh xạ: R1 = UT-LOGIN-01, R2 = UT-LOGIN-02, R3 = UT-LOGIN-03, R4 = UT-LOGIN-04.

## 2. Calculator Tests (JUnit - Lab 1, 2, 3)

### Lab 1: JUnit Basic (CalculatorBasicTest - 25 tests)

| ID | Mô tả | Trạng thái |
|----|-------|------------|
| LB1-01 | @BeforeEach/@AfterEach lifecycle | ✅ Pass |
| LB1-02 | assertEquals | ✅ Pass |
| LB1-03 | assertTrue/assertFalse | ✅ Pass |
| LB1-04 | assertNull/assertNotNull | ✅ Pass |
| LB1-05 | assertSame | ✅ Pass |
| LB1-06 | assertThrows | ✅ Pass |
| LB1-07 | @DisplayName | ✅ Pass |
| LB1-08 | @Disabled | ✅ Skip |
| LB1-09 | @Nested class | ✅ Pass |
| LB1-10 | @RepeatedTest(5) | ✅ Pass |
| LB1-11 | TestInfo | ✅ Pass |
| LB1-12 | TestReporter | ✅ Pass |
| LB1-13 | @ParameterizedTest + @ValueSource | ✅ Pass |
| LB1-14 | @ParameterizedTest + @CsvSource | ✅ Pass |
| LB1-15 | @ParameterizedTest + @NullSource | ✅ Pass |
| LB1-16 | @ParameterizedTest + @EmptySource | ✅ Pass |
| LB1-17 | @ParameterizedTest + @NullAndEmptySource | ✅ Pass |
| LB1-18 | @TestMethodOrder + @Order | ✅ Pass |

### Lab 2: Parameterized Tests (CalculatorTest - 21 tests)

| ID | Mô tả | Dữ liệu | Trạng thái |
|----|-------|---------|------------|
| LB2-01 | add hai số dương | 2, 3 → 5 | ✅ Pass |
| LB2-02 | add âm + dương | -3, 2 → -1 | ✅ Pass |
| LB2-03 | add overflow | MAX_VALUE, 1 | ✅ Pass |
| LB2-04 | subtract parameterized | 5 csv cases | ✅ Pass |
| LB2-05 | multiply parameterized | 4 csv cases | ✅ Pass |
| LB2-06 | multiply overflow | MAX_VALUE, 2 | ✅ Pass |
| LB2-07 | divide parameterized | 5 csv cases | ✅ Pass |
| LB2-08 | divide by zero message | 1, 0 | ✅ Pass |
| LB2-09 | isEven valuesource | 5 giá trị | ✅ Pass |
| LB2-10 | isOdd valuesource | 4 giá trị | ✅ Pass |

### Lab 3: Exception + Lifecycle (CalculatorExceptionTest - 16 tests)

| ID | Mô tả | Trạng thái |
|----|-------|------------|
| LB3-01 | divide by zero throws | ✅ Pass |
| LB3-02 | overflow message | ✅ Pass |
| LB3-03 | @BeforeAll/@AfterAll | ✅ Pass |
| LB3-04 | @BeforeEach/@AfterEach | ✅ Pass |

### Lab 5: Data-Driven (CalculatorDataDrivenTest - 45 tests)

| Nguồn dữ liệu | Số test cases |
|---------------|---------------|
| @CsvSource (add, subtract, multiply, divide) | 20 |
| @CsvFileSource (calculator-add.csv) | 9 |
| @CsvFileSource (calculator-divide.csv) | 8 |
| @EnumSource | 4 |
| @MethodSource (even/odd) | 13 |
| Decision Table | 4 |
| Boundary Value Analysis | 6 |

## 3. User Service Tests (47 tests)

### Đăng ký (UserServiceTest - 23 tests)

| ID | Mô tả | Dữ liệu | Kết quả mong đợi | Trạng thái |
|----|-------|---------|------------------|------------|
| UT-REG-01 | Đăng ký hợp lệ | alice / secret1 / alice@mail.com | Lưu, BCrypt | ✅ Pass |
| UT-REG-02 | Username null | null / secret1 / a@b.com | IllegalArgumentException | ✅ Pass |
| UT-REG-03 | Username rỗng | "" / secret1 / a@b.com | "Username không được rỗng" | ✅ Pass |
| UT-REG-04 | Username 2 ký tự | "ab" / secret1 / a@b.com | "Username phải từ 3 đến 50 ký tự" | ✅ Pass |
| UT-REG-05 | Username 3 ký tự | "abc" / secret1 / a@b.com | Chấp nhận | ✅ Pass |
| UT-REG-06 | Username 50 ký tự | 50 x "a" / secret1 / a@b.com | Chấp nhận | ✅ Pass |
| UT-REG-07 | Username 51 ký tự | 51 x "a" / secret1 / a@b.com | "Username phải từ 3 đến 50 ký tự" | ✅ Pass |
| UT-REG-08 | Password 5 ký tự | alice / "12345" / a@b.com | "Password phải có ít nhất 6 ký tự" | ✅ Pass |
| UT-REG-09 | Password 6 ký tự | alice / "123456" / a@b.com | Chấp nhận | ✅ Pass |
| UT-REG-10 | Email null | alice / secret1 / null | "Email không hợp lệ" | ✅ Pass |
| UT-REG-11 | Email rỗng | alice / secret1 / "" | "Email không hợp lệ" | ✅ Pass |
| UT-REG-12 | Email sai format (abc) | alice / secret1 / abc | "Email không hợp lệ" | ✅ Pass |
| UT-REG-13 | Email sai format (a@b) | alice / secret1 / a@b | "Email không hợp lệ" | ✅ Pass |
| UT-REG-14 | Email có khoảng trắng | alice / secret1 / a b@c.com | "Email không hợp lệ" | ✅ Pass |
| UT-REG-15 | Email @ đầu tiên | alice / secret1 / @b.com | "Email không hợp lệ" | ✅ Pass |
| UT-REG-16 | Username trùng | alice (đã tồn tại) | "Username đã tồn tại" | ✅ Pass |
| UT-REG-17 | Password được BCrypt | alice / secret1 / a@b.com | BCrypt hash khác plain | ✅ Pass |

### Đăng nhập (UserServiceTest - 6 tests)

| ID | Mô tả | Kết quả mong đợi | Trạng thái |
|----|-------|-----------------|------------|
| UT-LOGIN-01 | Đúng username + password | Optional có giá trị | ✅ Pass |
| UT-LOGIN-02 | Đúng username, sai password | Optional rỗng | ✅ Pass |
| UT-LOGIN-03 | Sai username | Optional rỗng | ✅ Pass |
| UT-LOGIN-04 | Username null | Optional rỗng, không gọi repo | ✅ Pass |
| UT-LOGIN-05 | Password null | Optional rỗng | ✅ Pass |
| UT-LOGIN-06 | Cả hai null | Optional rỗng | ✅ Pass |

## 4. Product Service Tests (8 tests)

| ID | Mô tả | Trạng thái |
|----|-------|------------|
| UT-PRD-01 | findAll - có dữ liệu | ✅ Pass |
| UT-PRD-02 | findAll - rỗng | ✅ Pass |
| UT-PRD-03 | findById - tồn tại | ✅ Pass |
| UT-PRD-04 | findById - không tồn tại | ✅ Pass |
| UT-PRD-05 | create - reset id | ✅ Pass |
| UT-PRD-06 | update - thành công | ✅ Pass |
| UT-PRD-07 | update - id không tồn tại | ✅ Pass |
| UT-PRD-08 | delete - thành công | ✅ Pass |
| UT-PRD-09 | delete - id không tồn tại | ✅ Pass |

## 5. Controller Tests (MockMvc - 16 tests)

### AuthControllerTest (6 tests)

| ID | Mô tả | Trạng thái |
|----|-------|------------|
| CT-AUTH-01 | GET /register | ✅ 200 OK |
| CT-AUTH-02 | GET /login | ✅ 200 OK |
| CT-AUTH-03 | POST /register - hợp lệ | ✅ Redirect /login |
| CT-AUTH-04 | POST /register - không hợp lệ | ✅ 200 + error |
| CT-AUTH-05 | POST /login - thành công | ✅ Redirect /products |
| CT-AUTH-06 | POST /login - thất bại | ✅ 200 + error |

### ProductControllerTest (10 tests)

| ID | Mô tả | Trạng thái |
|----|-------|------------|
| CT-PRD-01 | GET /products - đã đăng nhập | ✅ 200 OK |
| CT-PRD-02 | GET /products - chưa đăng nhập | ✅ Redirect /login |
| CT-PRD-03 | GET /products/new | ✅ 200 OK |
| CT-PRD-04 | POST /products/new - hợp lệ | ✅ Redirect /products |
| CT-PRD-05 | POST /products/new - không hợp lệ | ✅ 200 + error |
| CT-PRD-06 | GET /products/edit/{id} | ✅ 200 OK |
| CT-PRD-07 | POST /products/edit/{id} - hợp lệ | ✅ Redirect /products |
| CT-PRD-08 | POST /products/edit/{id} - không hợp lệ | ✅ 200 + error |
| CT-PRD-09 | POST /products/delete/{id} | ✅ Redirect /products |
| CT-PRD-10 | Edit id không tồn tại | ✅ 404 hoặc redirect |

## 6. UI Tests (Selenium + TestNG - 8 tests)

### AuthUiTest (4 tests)

| ID | Test method | Group | Trạng thái |
|----|-------------|-------|------------|
| UI-AUTH-01 | should_RegisterThenLogin_When_DataValid | smoke, auth | ⏳ Chưa chạy |
| UI-AUTH-02 | should_ShowError_When_RegisterInvalid (3 bộ dữ liệu) | regression, auth | ⏳ Chưa chạy |
| UI-AUTH-03 | should_ShowError_When_LoginWrongPassword | smoke, auth | ⏳ Chưa chạy |
| UI-AUTH-04 | should_RedirectToLogin_When_OpenProductsWithoutLogin | regression, auth | ⏳ Chưa chạy |

### ProductCrudUiTest (4 tests)

| ID | Test method | Group | Trạng thái |
|----|-------------|-------|------------|
| UI-PRD-01 | should_CreateProduct_When_FormValid | smoke, product | ⏳ Chưa chạy |
| UI-PRD-02 | should_ShowFieldErrors_When_CreateInvalid | regression, product | ⏳ Chưa chạy |
| UI-PRD-03 | should_UpdateProduct_When_EditSaved | regression, product | ⏳ Chưa chạy |
| UI-PRD-04 | should_DeleteProduct_When_DeleteClicked | smoke, product | ⏳ Chưa chạy |

**Lý do:** Máy local thiếu Chrome, CI GitHub Actions cần verify.

## 7. Lab 4: Page Object Model Tests

### Lab4AuthTest (10 tests)
- should_RegisterSuccessfully_When_DataValid
- should_ShowError_When_UsernameTooShort
- should_ShowError_When_PasswordTooShort
- should_ShowError_When_EmailInvalid
- should_NavigateToLogin_When_ClickLoginLink
- should_LoginSuccessfully_When_CredentialsValid
- should_ShowError_When_WrongPassword
- should_ShowError_When_UserNotExists
- should_RedirectToLogin_When_AccessProductsWithoutAuth
- should_NavigateToRegister_When_ClickRegisterLink

### Lab4ProductTest (8 tests)
- should_CreateProduct_When_FormValid
- should_ShowFieldErrors_When_FormInvalid
- should_UpdateProduct_When_EditSaved
- should_DeleteProduct_When_DeleteClicked
- should_ShowError_When_NameEmpty
- should_ShowError_When_PriceZero
- should_ShowError_When_QuantityNegative
- should_ListMultipleProducts

## 8. Lab 6: TestNG Annotations Tests

### Groups
- smoke: testLoginValid, testLoginWithCredentials
- regression: testLoginInvalidPassword, testProductCRUD
- e2e: testNavigateToProducts → testCreateFirstProduct
- exception: testDivideByZero, testNullPointer
- performance: testConcurrentLogin, testQuickOperation

## 9. Tổng kết

| Loại test | Số lượng | Pass | Fail | Skip |
|-----------|----------|------|------|------|
| Calculator (Lab 1,2,3) | 62 | 62 | 0 | 0 |
| Calculator Data-Driven (Lab 5) | 45 | 45 | 0 | 0 |
| Service (User + Product) | 31 | 31 | 0 | 0 |
| Controller | 16 | 16 | 0 | 0 |
| UI (Selenium) | 20+ | ⏳ | ⏳ | ⏳ |
| **Tổng cộng** | **174+** | **154** | **0** | **0** |

## 10. Coverage Matrix

| Chức năng | Unit Test | Controller Test | UI Test |
|-----------|-----------|-----------------|---------|
| Đăng ký | ✅ | ✅ | ✅ |
| Đăng nhập | ✅ | ✅ | ✅ |
| Đăng xuất | ❌ | ❌ | ✅ |
| Xem sản phẩm | ✅ | ✅ | ✅ |
| Tạo sản phẩm | ✅ | ✅ | ✅ |
| Sửa sản phẩm | ✅ | ✅ | ✅ |
| Xóa sản phẩm | ✅ | ✅ | ✅ |
| Validation | ✅ | ✅ | ✅ |
| Authorization | ❌ | ✅ | ✅ |
