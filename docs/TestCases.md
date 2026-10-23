# Test Cases - Shop ASM

## Trạng thái kiểm thử
- **JUnit 5 (Service & Controller)**: 47 tests, 0 fail (100% Pass trên Java 25 LTS)
- **TestNG (Selenium UI với Page Object Model)**: 10 tests, 0 fail (100% Pass trên Java 25 LTS)

## 1. Decision Table - Đăng nhập

| Điều kiện / Rule | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| Username tồn tại | Y | Y | N | N |
| Password đúng | Y | N | - | - |
| **Kết quả** | Vào /products | Lỗi chung | Lỗi chung | Lỗi chung |

Ánh xạ: R1 = UT-LOGIN-01, R2 = UT-LOGIN-02, R3 = UT-LOGIN-03, R4 = UT-LOGIN-04.

## 2. User Service Tests (JUnit 5 + Mockito)

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

## 3. Product Service Tests (JUnit 5 + Mockito - 8 tests)

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

## 4. Controller Tests (JUnit 5 + MockMvc - 16 tests)

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

## 5. UI Tests (Selenium + TestNG với Page Object Model - 10 tests)

### AuthUiTest (Page Objects: RegisterPage, LoginPage, ProductsPage)

| ID | Test method | Group | Kỹ thuật áp dụng | Trạng thái |
|----|-------------|-------|-------------------|------------|
| UI-AUTH-01 | should_RegisterThenLogin_When_DataValid | smoke, auth | Page Object Model, End-to-end flow | ✅ Pass |
| UI-AUTH-02 | should_ShowError_When_RegisterInvalid (3 bộ dữ liệu) | regression, auth | Page Object Model, DataProvider | ✅ Pass |
| UI-AUTH-03 | should_ShowError_When_LoginWrongPassword | smoke, auth | Page Object Model, Error validation | ✅ Pass |
| UI-AUTH-04 | should_RedirectToLogin_When_OpenProductsWithoutLogin | regression, auth | Page Object Model, Auth Guard | ✅ Pass |

### ProductCrudUiTest (Page Objects: ProductsPage, ProductFormPage, RegisterPage, LoginPage)

| ID | Test method | Group | Kỹ thuật áp dụng | Trạng thái |
|----|-------------|-------|-------------------|------------|
| UI-PRD-01 | should_CreateProduct_When_FormValid | smoke, product | Page Object Model, Create | ✅ Pass |
| UI-PRD-02 | should_ShowFieldErrors_When_CreateInvalid | regression, product | Page Object Model, Field validation | ✅ Pass |
| UI-PRD-03 | should_UpdateProduct_When_EditSaved | regression, product | Page Object Model, Update | ✅ Pass |
| UI-PRD-04 | should_DeleteProduct_When_DeleteClicked | smoke, product | Page Object Model, Delete | ✅ Pass |

## 6. Tổng kết

| Loại test | Công cụ | Số lượng | Pass | Fail | Skip | Tỷ lệ |
|-----------|---------|:--------:|:----:|:----:|:----:|:-----:|
| Unit Test (Service) | JUnit 5 + Mockito | 31 | 31 | 0 | 0 | 100% |
| Controller Test | JUnit 5 + MockMvc | 16 | 16 | 0 | 0 | 100% |
| UI Automation Test | Selenium + TestNG (POM) | 10 | 10 | 0 | 0 | 100% |
| **Tổng cộng** | | **57** | **57** | **0** | **0** | **100%** |

## 7. Coverage Matrix

| Chức năng | Unit Test | Controller Test | UI Test (POM) |
|-----------|:---------:|:---------------:|:-------------:|
| Đăng ký | ✅ | ✅ | ✅ |
| Đăng nhập | ✅ | ✅ | ✅ |
| Đăng xuất | ❌ | ❌ | ✅ |
| Xem danh sách sản phẩm | ✅ | ✅ | ✅ |
| Thêm mới sản phẩm | ✅ | ✅ | ✅ |
| Chỉnh sửa sản phẩm | ✅ | ✅ | ✅ |
| Xóa sản phẩm | ✅ | ✅ | ✅ |
| Validation dữ liệu | ✅ | ✅ | ✅ |
| Phân quyền / Authorization | ❌ | ✅ | ✅ |

