# Assignment - Kiểm thử phần mềm

Dự án Spring Boot minh họa kiểm thử đa tầng: JUnit 5 (unit + controller) và Selenium + TestNG (UI).

## Cấu trúc

```
assignment/
├── src/main/java/com/example/
│   ├── lab/Calculator.java                    # Lab 2, Lab 3
│   └── shop/                                   # Y1: ứng dụng web
│       ├── ShopApplication.java
│       ├── controller/{Auth,Product}Controller.java
│       ├── model/{User,Product}.java
│       ├── repository/{User,Product}Repository.java
│       └── service/{User,Product}Service.java
├── src/main/resources/
│   ├── application.properties
│   └── templates/{register,login,products,product-form}.html
├── src/test/java/com/example/
│   ├── lab/
│   │   ├── CalculatorTest.java                # Lab 2 bài 3 + Lab 3 bài 1,3
│   │   └── CalculatorExceptionTest.java       # Lab 3 bài 2
│   └── shop/
│       ├── controller/                         # Y3: MockMvc
│       │   ├── AuthControllerTest.java
│       │   └── ProductControllerTest.java
│       ├── service/                            # Y3: JUnit + Mockito
│       │   ├── UserServiceTest.java
│       │   └── ProductServiceTest.java
│       └── ui/                                 # Y4: Selenium + TestNG
│           ├── BaseTest.java
│           ├── AuthUiTest.java
│           └── ProductCrudUiTest.java
├── src/test/resources/testng.xml              # Y5: TestNG suite
├── .github/workflows/selenium-ci.yml          # Y5: CI/CD (chưa chạy)
├── docs/
│   ├── TestPlan.md                             # Y2: kế hoạch kiểm thử
│   ├── TestCases.md                            # Y2: danh sách test case
│   └── AI_Log.md                               # Y6: nhật ký sử dụng AI (khung)
├── pom.xml
└── README.md
```

## Lệnh chạy

### 1. JUnit (unit + controller)
```bash
mvn test
```
Kết quả: **84 test, 0 fail**  
- 37 test Calculator (Lab 2, Lab 3): `@BeforeAll/@AfterAll`, `@ParameterizedTest`, `assertThrows`, overflow, sqrt âm.  
- 47 test Shop (Y3): service (Mockito), controller (MockMvc), boundary value, decision table.

### 2. Selenium + TestNG (UI)
```bash
# Tất cả
mvn test -Pui

# Chỉ nhóm smoke
mvn test -Pui -Dgroups=smoke

# Hiển thị trình duyệt (không headless)
mvn test -Pui -Dgroups=smoke -Dheadless=false
```

**Lưu ý**: UI test cần Chrome/Chromium. Selenium Manager tự tải driver, nhưng binary của trình duyệt phải có sẵn.  
Lỗi `cannot find Chrome binary` → cài Chrome hoặc chạy trên CI (Ubuntu GitHub Actions có sẵn).

### 3. Chạy ứng dụng
```bash
mvn spring-boot:run
```
Mở `http://localhost:8080/register` để đăng ký, sau đó `/login` và `/products`.

## Yêu cầu đã hoàn thành

| Yêu cầu | Mô tả | Vị trí | Trạng thái |
|---|---|---|---|
| Lab 2 bài 3 | JUnit cơ bản (Calculator) | `CalculatorTest` | ✅ 29 test pass |
| Lab 3 bài 1 | Vòng đời `@Before/@After` | `CalculatorTest` | ✅ test pass (chưa chụp log thứ tự chạy) |
| Lab 3 bài 2 | Ngoại lệ `assertThrows` | `CalculatorExceptionTest` | ✅ 8 test pass |
| Lab 3 bài 3 | `@ParameterizedTest` | `CalculatorTest` | ✅ CsvSource + ValueSource |
| Y1 | Ứng dụng web | `src/main/java/.../shop` | ✅ biên dịch OK, các test MockMvc pass (chưa chạy thủ công trên trình duyệt) |
| Y2 | Test plan, test case | `docs/TestPlan.md`, `TestCases.md` | ✅ decision table, BVA |
| Y3 | JUnit service + controller | `src/test/java/.../shop` | ✅ 47 test pass |
| Y4 | Selenium + TestNG | `src/test/java/.../ui` | ✅ code sẵn sàng, chưa chạy được (thiếu Chrome) |
| Y5 | TestNG suite + CI/CD | `testng.xml`, `selenium-ci.yml` | ⚠️ đã viết, chưa push GitHub nên chưa chạy thử |
| Y6 | Nhật ký AI | `docs/AI_Log.md` | ✅ khung (sinh viên điền thêm) |

## Test case tổng quan

### JUnit (84 test, 0 fail)
- **Calculator** (37): add/subtract/multiply/divide với boundary, overflow, chia 0, sqrt âm, even/odd.
- **UserService** (23): đăng ký (null, rỗng, độ dài username 2/3/50/51, password 5/6, email sai, trùng), đăng nhập (đúng/sai).
- **ProductService** (8): CRUD (findAll, findById, create reset id, update, delete, id không tồn tại).
- **AuthController** (6): register/login GET/POST, validation, redirect.
- **ProductController** (10): list, add GET/POST, edit GET/POST, delete, yêu cầu đăng nhập, validation.

### Selenium + TestNG (8 test, chưa chạy được)
- **AuthUiTest** (4): đăng ký → đăng nhập, sai mật khẩu, validation, redirect khi chưa đăng nhập.
- **ProductCrudUiTest** (4): tạo, sửa, xóa, validation (nhóm `smoke` và `regression`).

## CI/CD

Workflow `.github/workflows/selenium-ci.yml`:
- **Trigger**: push/PR (chạy `smoke`), hoặc thủ công (chọn `smoke`/`regression`/`all`).
- **Job 1** (`unit-tests`): chạy JUnit, upload report.
- **Job 2** (`ui-tests`): phụ thuộc job 1, chạy Selenium với Chrome headless, matrix theo nhóm.

Chưa push lên GitHub nên workflow chưa được chạy thật. UI test cũng chưa từng chạy thành công ở đâu, nên chưa thể khẳng định sẽ pass trên CI (runner Ubuntu thường có sẵn Chrome, nhưng cần xác nhận khi chạy).

## Kỹ thuật thiết kế test (Y2)

- **Decision Table**: đăng nhập (username đúng/sai × password đúng/sai).
- **Boundary Value Analysis**: độ dài username (2,3,50,51), password (5,6).
- **Equivalence Partitioning**: email hợp lệ / không hợp lệ.
- **Error Guessing**: null, rỗng, khoảng trắng, overflow, chia 0.

## Ghi chú

1. **H2 in-memory**: dữ liệu mất khi tắt ứng dụng. Mỗi lần chạy test suite bắt đầu từ DB rỗng.
2. **Security**: BCrypt cho password, session-based auth, `/products` yêu cầu đăng nhập.
3. **TestNG groups**: `smoke` (quan trọng, chạy nhanh), `regression` (toàn bộ).
4. **Selenium Manager**: Selenium 4.6+ tự tải chromedriver nếu chưa có, nhưng vẫn cần cài sẵn trình duyệt Chrome.
5. **AI_Log.md**: khung sẵn, sinh viên cần điền prompt thật, code AI sinh, chỉnh sửa, nhận xét (Y6).

## Tác giả

Dự án được tạo bởi AI (Cursor/Claude) theo yêu cầu đề bài. Sinh viên cần review, chạy thật, điền nhật ký AI, và nộp.
