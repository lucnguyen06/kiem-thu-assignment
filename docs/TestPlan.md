# Test Plan - Shop ASM

## 1. Pham vi
Ung dung Spring Boot (Thymeleaf, H2) gom: dang ky, dang nhap, dang xuat, CRUD san pham.

## 2. Business rules (nguon cho RAG)
- Username: khong rong, 3-50 ky tu, khong trung.
- Password: toi thieu 6 ky tu, luu dang BCrypt.
- Email: dang a@b.c.
- Dang nhap sai username hoac password: thong bao chung "Dang nhap khong thanh cong".
- San pham: ten khong rong (toi da 100), gia >= 1, so luong >= 0.
- Moi thao tac /products can dang nhap, neu khong chuyen ve /login.

## 3. Ky thuat thiet ke test
- Decision Table: dang nhap (username dung/sai x password dung/sai).
- Boundary Value Analysis: do dai username (2,3,50,51), password (5,6).
- Equivalence Partitioning: email hop le / khong hop le.
- Error guessing: null, rong, khoang trang.

## 4. Cong cu va cap do
| Cap do | Cong cu | Vi tri |
|---|---|---|
| Unit (service) | JUnit 5 + Mockito | src/test/java/.../service |
| Controller | JUnit 5 + MockMvc | src/test/java/.../controller |
| UI | Selenium + TestNG | src/test/java/.../ui |

## 5. Lenh chay
- JUnit: `mvn test`
- UI tat ca: `mvn test -Pui`
- UI smoke: `mvn test -Pui -Dgroups=smoke`
- Co giao dien: them `-Dheadless=false`

## 6. Tieu chi
- Vao: ung dung build duoc, Chrome co san cho UI test.
- Ra: 100% test smoke pass, regression khong co loi muc cao.

## 7. Rui ro
- UI test phu thuoc Chrome va driver. Selenium Manager tu tai driver, can mang.
- Du lieu H2 in-memory, moi lan chay suite bat dau tu trang thai rong.
