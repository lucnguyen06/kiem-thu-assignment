# Test Cases - Shop ASM

## Trạng thái kiểm thử
- **JUnit 5 (Service & Controller)**: 47 tests, 0 fail (100% Pass trên Java 25 LTS)
- **TestNG (Selenium UI với Page Object Model)**: 21 tests, 0 fail (100% Pass trên Java 25 LTS)
- **Tổng cộng**: 68 tests tự động hoàn chỉnh (100% Pass)

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

## 5. UI Tests (Selenium WebDriver + TestNG với Page Object Model - 21 tests)

### 5.1. AuthUiTest (Page Objects: RegisterPage, LoginPage, ProductsPage - 13 test executions)

| ID | Test method / Dữ liệu | Group | Kỹ thuật áp dụng | Kết quả mong đợi | Trạng thái |
|:---|:---|:---:|:---|:---|:---:|
| UI-AUTH-01 | `should_RegisterThenLogin_When_DataValid` | smoke, auth | Page Object Model, End-to-end flow | Đăng ký thành công, tự động đăng nhập vào `/products`, hiển thị đúng tên người dùng | ✅ Pass |
| UI-AUTH-02 | `should_LoginSuccessfully_WithSeededAdminAccount` | smoke, auth | Page Object Model, Seed Data Verify | Đăng nhập tài khoản mẫu `admin` / `admin123` thành công, `currentUser` = "admin" | ✅ Pass |
| UI-AUTH-03 | `should_LoginSuccessfully_WithSeededAliceAccount` | smoke, auth | Page Object Model, Seed Data Verify | Đăng nhập tài khoản mẫu `alice` / `secret1` thành công, `currentUser` = "alice" | ✅ Pass |
| UI-AUTH-04 | `should_LogoutSuccessfully_When_ClickLogout` | smoke, auth | Page Object Model, Session Invalidation | Bấm Đăng xuất -> Chuyển về `/login`, hiện alert "Bạn đã đăng xuất", cố truy cập lại `/products` bị chặn chuyển về `/login` | ✅ Pass |
| UI-AUTH-05 | `should_ShowError_When_LoginWrongPassword` | smoke, auth | Page Object Model, Error Validation | Đăng nhập với mật khẩu sai -> Hiển thị thông báo "Đăng nhập không thành công" | ✅ Pass |
| UI-AUTH-06 | `should_ShowError_When_LoginNonExistentUser` | regression, auth | Page Object Model, Error Validation | Đăng nhập username không tồn tại -> Hiển thị "Đăng nhập không thành công" | ✅ Pass |
| UI-AUTH-07 | `should_RedirectToLogin_When_OpenProductsWithoutLogin` | regression, auth | Page Object Model, Auth Guard | Chưa đăng nhập mà truy cập `/products` -> Tự động chuyển hướng về `/login` | ✅ Pass |
| UI-AUTH-08 | `should_NavigateBetweenLoginAndRegisterPages` | regression, auth | Page Object Model, Navigation link | Click qua lại giữa link "Đăng ký" và "Đăng nhập" mượt mà | ✅ Pass |
| UI-AUTH-09 | `should_ShowError_When_RegisterInvalid` (DataProvider #1: "ab") | regression, auth | Boundary Value Analysis (BVA dưới min) | Báo lỗi: "Username phai tu 3 den 50 ky tu" | ✅ Pass |
| UI-AUTH-10 | `should_ShowError_When_RegisterInvalid` (DataProvider #2: 51 ký tự) | regression, auth | Boundary Value Analysis (BVA trên max) | Báo lỗi: "Username phai tu 3 den 50 ky tu" | ✅ Pass |
| UI-AUTH-11 | `should_ShowError_When_RegisterInvalid` (DataProvider #3: password 3 ký tự) | regression, auth | Boundary Value Analysis (Password < 6) | Báo lỗi: "Password phai co it nhat 6 ky tu" | ✅ Pass |
| UI-AUTH-12 | `should_ShowError_When_RegisterInvalid` (DataProvider #4: email "not-an-email") | regression, auth | Equivalence Partitioning (Email sai format) | Báo lỗi: "Email khong hop le" | ✅ Pass |
| UI-AUTH-13 | `should_ShowError_When_RegisterInvalid` (DataProvider #5: email "user@") | regression, auth | Equivalence Partitioning (Email thiếu domain) | Báo lỗi: "Email khong hop le" | ✅ Pass |

### 5.2. ProductCrudUiTest (Page Objects: ProductsPage, ProductFormPage, RegisterPage, LoginPage - 8 tests)

| ID | Test method | Group | Kỹ thuật áp dụng | Kết quả mong đợi | Trạng thái |
|:---|:---|:---:|:---|:---|:---:|
| UI-PRD-01 | `should_CreateProduct_When_FormValid` | smoke, product | Page Object Model, Create | Nhập form hợp lệ (Ao thun, 150000, 10) -> Lưu thành công, hiển thị trên bảng sản phẩm | ✅ Pass |
| UI-PRD-02 | `should_CreateProduct_WithBoundaryValues` | smoke, product | Boundary Value Analysis (Biên tối thiểu) | Nhập giá = 1, số lượng = 0 -> Tạo thành công với giá 1 và số lượng 0 | ✅ Pass |
| UI-PRD-03 | `should_ShowFieldErrors_When_CreateInvalid` | regression, product | Server Validation, Error Check | Nhập giá = 0, số lượng âm (-1) -> Hiển thị ít nhất 2 lỗi `.field-error` | ✅ Pass |
| UI-PRD-04 | `should_ShowFieldErrors_When_NameEmpty` | regression, product | Validation, Empty Field Check | Để trống tên sản phẩm và bấm Lưu -> Hiển thị lỗi validation | ✅ Pass |
| UI-PRD-05 | `should_CancelCreateProduct_When_CancelClicked` | regression, product | Navigation, Cancel Action | Nhập form dở dang rồi bấm nút Hủy -> Quay về `/products`, không thêm sản phẩm vào bảng | ✅ Pass |
| UI-PRD-06 | `should_DisplayPreFilledData_When_OpenEditForm` | regression, product | Data Binding, Edit State | Bấm nút "Sửa" -> Form hiển thị sẵn tên, giá, số lượng hiện tại của sản phẩm | ✅ Pass |
| UI-PRD-07 | `should_UpdateProduct_When_EditSaved` | regression, product | Page Object Model, Update | Sửa tên từ "Quan jean" thành "Quan jean moi", giá 350000 -> Bảng cập nhật thông tin mới | ✅ Pass |
| UI-PRD-08 | `should_DeleteProduct_When_DeleteClicked` | smoke, product | Page Object Model, Delete | Bấm nút "Xóa" tại dòng sản phẩm -> Sản phẩm biến mất khỏi bảng | ✅ Pass |

## 6. Tổng kết

| Loại test | Công cụ | Số lượng | Pass | Fail | Skip | Tỷ lệ Pass |
|-----------|---------|:--------:|:----:|:----:|:----:|:----------:|
| Unit Test (Service) | JUnit 5 + Mockito | 31 | 31 | 0 | 0 | **100%** |
| Controller Test | JUnit 5 + MockMvc | 16 | 16 | 0 | 0 | **100%** |
| UI Automation Test | Selenium WebDriver + TestNG (POM) | 21 | 21 | 0 | 0 | **100%** |
| **Tổng cộng** | | **68** | **68** | **0** | **0** | **100%** |

## 7. Coverage Matrix

| Chức năng nghiệp vụ | Unit Test | Controller Test | UI Automation Test (POM) |
|---|:---:|:---:|:---:|
| Đăng ký tài khoản (hợp lệ & không hợp lệ) | ✅ | ✅ | ✅ |
| Đăng nhập (tài khoản thường & tài khoản có sẵn) | ✅ | ✅ | ✅ |
| Đăng xuất (xóa session và bảo vệ route) | ❌ | ✅ | ✅ |
| Xem danh sách sản phẩm | ✅ | ✅ | ✅ |
| Thêm mới sản phẩm (hợp lệ & giá trị biên) | ✅ | ✅ | ✅ |
| Form validation khi thêm/sửa sản phẩm | ✅ | ✅ | ✅ |
| Chỉnh sửa sản phẩm | ✅ | ✅ | ✅ |
| Xóa sản phẩm | ✅ | ✅ | ✅ |
| Nút Hủy và điều hướng trang | ❌ | ❌ | ✅ |
| Phân quyền / Authorization (chặn truy cập trái phép) | ❌ | ✅ | ✅ |

