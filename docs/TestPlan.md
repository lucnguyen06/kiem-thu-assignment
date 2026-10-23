# Test Plan - Shop ASM

## 1. Phạm vi
Ứng dụng Spring Boot (Thymeleaf, H2) gồm: đăng ký, đăng nhập, đăng xuất, CRUD sản phẩm.

## 2. Business rules (nguồn cho RAG)
- Username: không rỗng, 3-50 ký tự, không trùng.
- Password: tối thiểu 6 ký tự, lưu dạng BCrypt.
- Email: đúng định dạng a@b.c.
- Đăng nhập sai username hoặc password: thông báo chung "Đăng nhập không thành công".
- Sản phẩm: tên không rỗng (tối đa 100), giá >= 1, số lượng >= 0.
- Mọi thao tác /products cần đăng nhập, nếu không chuyển về /login.

## 3. Kỹ thuật thiết kế test

### 3.1. Decision Table - Đăng nhập

| Điều kiện / Rule | R1 | R2 | R3 | R4 |
|---|---|---|---|---|
| Username tồn tại | Y | Y | N | N |
| Password đúng | Y | N | - | - |
| **Kết quả** | Vào /products | Lỗi chung | Lỗi chung | Lỗi chung |

### 3.2. Boundary Value Analysis (BVA)

**Username:**
| Giá trị | Kết quả |
|---------|---------|
| 2 ký tự | Invalid (dưới min) |
| 3 ký tự | Valid (min) |
| 50 ký tự | Valid (max) |
| 51 ký tự | Invalid (trên max) |

**Password:**
| Giá trị | Kết quả |
|---------|---------|
| 5 ký tự | Invalid (dưới min) |
| 6 ký tự | Valid (min) |

### 3.3. Equivalence Partitioning

**Email:**
- Partition 1: Email hợp lệ (alice@mail.com, bob@test.org)
- Partition 2: Email không hợp lệ (abc, a@b, @b.com, a b@c.com)

**Giá sản phẩm:**
- Partition 1: Giá >= 1 (hợp lệ)
- Partition 2: Giá < 1 (không hợp lệ: 0, -1)

### 3.4. Error Guessing
- null, rỗng "", khoảng trắng "   "
- SQL injection: "' OR '1'='1"
- XSS: "<script>alert(1)</script>"
- Overflow: số cực lớn
- Chia 0

## 4. Công cụ và cấp độ

| Cấp độ | Công cụ | Vị trí |
|--------|---------|---------|
| Unit (Service) | JUnit 5 + Mockito | src/test/java/com/example/shop/service |
| Controller (MockMvc) | JUnit 5 + MockMvc | src/test/java/com/example/shop/controller |
| UI (Page Object Model) | Selenium + TestNG | src/test/java/com/example/shop/ui |

## 5. Test Groups

| Nhóm | Mô tả | Ví dụ test |
|------|-------|------------|
| smoke | Tests quan trọng, chạy nhanh | login hợp lệ, tạo sản phẩm |
| regression | Toàn bộ UI tests | tất cả UI test cases (boundary, invalid error, CRUD) |
| auth | Tests liên quan xác thực | login, register, redirect |
| product | Tests liên quan sản phẩm | Create, Read, Update, Delete sản phẩm |

## 6. Lệnh chạy

### JUnit 5 (Unit & Controller tests)
```bash
# Chạy tất cả test Service và Controller
mvn test
```

### TestNG (Selenium UI tests với Page Object Model)
```bash
# Chạy toàn bộ UI tests
mvn test -Pui

# Chỉ chạy nhóm smoke
mvn test -Pui -Dgroups=smoke

# Chạy có giao diện trình duyệt (không headless)
mvn test -Pui -Dgroups=smoke -Dheadless=false
```

## 7. Tiêu chí nghiệm thu

### Đầu vào
- Ứng dụng build được (`mvn compile`)
- Chrome/Chromium có sẵn cho UI test

### Đầu ra
- 100% test smoke pass
- 0 critical/high failures trong regression

## 8. Rủi ro và mitigation

| Rủi ro | Mức | Mitigation |
|--------|-----|------------|
| UI test phụ thuộc Chrome | Cao | Selenium Manager tự tải driver; CI có Ubuntu + Chrome |
| Flaky UI tests | Trung | Retry analyzer cho tests không ổn định |
| Dữ liệu H2 in-memory | Thấp | Mỗi test suite bắt đầu từ trạng thái rỗng |
| Parallel execution race | Trung | Thread-safe drivers, @BeforeMethod tạo driver riêng |

## 9. Test Environments

| Môi trường | Mô tả |
|------------|-------|
| Local Dev | Windows, Chrome/Edge, chạy thủ công |
| CI/CD | GitHub Actions Ubuntu, Chrome headless |
| Staging | (Tùy chọn) Server cloud |

## 10. Test Deliverables

- [x] Test Plan (file này)
- [x] Test Cases (docs/TestCases.md)
- [x] AI Log (docs/AI_Log.md)
- [x] CI/CD Workflow (.github/workflows/selenium-ci.yml)
- [x] Test Suite XML (src/test/resources/testng.xml)
