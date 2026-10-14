# Test Cases - Shop ASM

Trang thai lay tu lan chay `mvn test` (JUnit): 47 test, 0 fail.
UI test (Selenium/TestNG): da viet, chua chay duoc tren may nay vi thieu Chrome.

## 1. Decision Table - Dang nhap

| Dieu kien / Rule | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| Username ton tai | Y | Y | N | N |
| Password dung | Y | N | - | - |
| **Ket qua** | Vao /products | Loi chung | Loi chung | Loi chung |

Anh xa: R1 = UT-LOGIN-01, R2 = UT-LOGIN-02, R3 = UT-LOGIN-03, null = UT-LOGIN-04.

## 2. Test case dang ky (UserServiceTest)

| ID | Mo ta | Du lieu | Ket qua mong doi | Trang thai |
|---|---|---|---|---|
| UT-REG-01 | Dang ky hop le | alice / secret1 / alice@mail.com | Luu, password BCrypt | Pass |
| UT-REG-02 | Username khong hop le | null, rong, "   ", "ab", 51 ky tu | IllegalArgumentException | Pass |
| UT-REG-03 | Password ngan | null, rong, "a", "12345" | "Password phai co it nhat 6 ky tu" | Pass |
| UT-REG-04 | Email sai dinh dang | null, rong, abc, a@b, @b.com, "a b@c.com", a@@b.com | "Email khong hop le" | Pass |
| UT-REG-05 | Username trung | alice da ton tai | "Username da ton tai", khong save | Pass |
| UT-REG-06 | Bien | username 3 ky tu, password 6 ky tu | Chap nhan | Pass |

## 3. Test case dang nhap (UserServiceTest)

| ID | Mo ta | Ket qua mong doi | Trang thai |
|---|---|---|---|
| UT-LOGIN-01 | Dung username + password | Optional co gia tri | Pass |
| UT-LOGIN-02 | Dung username, sai password | Optional rong | Pass |
| UT-LOGIN-03 | Sai username | Optional rong | Pass |
| UT-LOGIN-04 | username hoac password null | Rong, khong goi repository | Pass |

## 4. Test case san pham (ProductServiceTest)

UT-PRD-01 den UT-PRD-08: findAll, findById ton tai / khong ton tai, create reset id, update, update id khong ton tai, delete, delete id khong ton tai. Tat ca Pass (8 test).

## 5. Test case controller (MockMvc)

- AuthControllerTest: CT-AUTH-01 den CT-AUTH-06 (6 test, Pass).
- ProductControllerTest: CT-PRD-01 den CT-PRD-10 (10 test, Pass), gom truong hop chua dang nhap, du lieu khong hop le, id khong ton tai.

## 6. Test case UI (Selenium + TestNG)

| ID | Test method | Group | Trang thai |
|---|---|---|---|
| UI-AUTH-01 | should_RegisterThenLogin_When_DataValid | smoke, auth | Chua chay |
| UI-AUTH-02 | should_ShowError_When_RegisterInvalid (3 bo du lieu) | regression, auth | Chua chay |
| UI-AUTH-03 | should_ShowError_When_LoginWrongPassword | smoke, auth | Chua chay |
| UI-AUTH-04 | should_RedirectToLogin_When_OpenProductsWithoutLogin | regression, auth | Chua chay |
| UI-PRD-01 | should_CreateProduct_When_FormValid | smoke, product | Chua chay |
| UI-PRD-02 | should_ShowFieldErrors_When_CreateInvalid | regression, product | Chua chay |
| UI-PRD-03 | should_UpdateProduct_When_EditSaved | regression, product | Chua chay |
| UI-PRD-04 | should_DeleteProduct_When_DeleteClicked | smoke, product | Chua chay |

Ly do "Chua chay": lan chay `mvn test -Pui -Dgroups=smoke` that bai o buoc mo trinh duyet (`SessionNotCreatedException: cannot find Chrome binary`), chua toi buoc kiem tra giao dien.
