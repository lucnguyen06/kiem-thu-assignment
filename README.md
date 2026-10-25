# Assignment - Kiểm thử nâng cao cùng AI (Java)

Dự án kiểm thử tự động toàn diện cho ứng dụng Web Shop (Spring Boot) đáp ứng đầy đủ các yêu cầu **Y1 - Y6** của đề bài Assignment (FPT Polytechnic):
- **JUnit 5 + Mockito**: Kiểm thử tầng Service và tầng Controller (MockMvc).
- **Selenium WebDriver + TestNG**: Kiểm thử tự động giao diện (UI) áp dụng mô hình **Page Object Model (POM)**.
- **GitHub Actions (CI/CD)**: Pipeline tự động chạy test khi push/PR, phân nhóm test smoke/regression, upload báo cáo kiểm thử.
- **Runtime**: Đã nâng cấp tương thích hoàn toàn với **Java 25 LTS** và Spring Boot **3.5.16**.

## Cấu trúc dự án ASM

```
assignment/
├── src/main/java/com/example/shop/            # Y1: Ứng dụng Web Spring Boot
│   ├── ShopApplication.java
│   ├── controller/
│   │   ├── AuthController.java                # Đăng ký, đăng nhập, đăng xuất
│   │   └── ProductController.java             # Quản lý CRUD sản phẩm
│   ├── model/
│   │   ├── User.java                          # Thực thể User
│   │   └── Product.java                       # Thực thể Product
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── ProductRepository.java
│   └── service/
│       ├── UserService.java                   # Xử lý logic đăng ký (BCrypt) & đăng nhập
│       └── ProductService.java                # Xử lý logic CRUD sản phẩm
├── src/main/resources/
│   ├── application.properties                 # Cấu hình H2 database, JPA, Server
│   └── templates/                             # Thymeleaf UI templates
│       ├── register.html
│       ├── login.html
│       ├── products.html
│       └── product-form.html
├── src/test/java/com/example/shop/
│   ├── service/                               # Y3: Unit Test với JUnit 5 & Mockito (31 tests)
│   │   ├── UserServiceTest.java               # 23 tests đăng ký (BVA, Equivalence) + 6 tests đăng nhập
│   │   └── ProductServiceTest.java            # 8 tests CRUD logic
│   ├── controller/                            # Y3: Controller Test với MockMvc (16 tests)
│   │   ├── AuthControllerTest.java            # 6 tests endpoints /register, /login
│   │   └── ProductControllerTest.java         # 10 tests endpoints /products CRUD
│   └── ui/                                    # Y4: Selenium WebDriver + TestNG
│       ├── pages/                             # Page Object Model (POM)
│       │   ├── RegisterPage.java              # Page Object trang /register
│       │   ├── LoginPage.java                 # Page Object trang /login
│       │   ├── ProductsPage.java              # Page Object trang /products
│       │   └── ProductFormPage.java           # Page Object trang /products/new & edit
│       ├── BaseTest.java                      # Khởi động Spring Boot ngẫu nhiên + Chrome WebDriver
│       ├── AuthUiTest.java                    # 4 tests UI đăng ký & đăng nhập (POM)
│       └── ProductCrudUiTest.java             # 4 tests UI CRUD sản phẩm (POM)
├── src/test/resources/
│   ├── testng.xml                             # Y4, Y5: TestNG suite cho UI tests
│   └── data/
│       └── registration-data.csv              # Dữ liệu kiểm thử
├── .github/workflows/
│   └── selenium-ci.yml                        # Y5: CI/CD Pipeline trên GitHub Actions
├── docs/
│   ├── TestPlan.md                            # Y2: Kế hoạch kiểm thử & Business rules cho RAG
│   ├── TestCases.md                           # Y2: Bảng Test Case chi tiết
│   └── AI_Log.md                              # Y6: Nhật ký sử dụng AI (Prompt, Debug, Review)
├── pom.xml
└── README.md
```

## Lệnh chạy kiểm thử

### 1. Chạy Unit & Controller Tests (JUnit 5 + Mockito + MockMvc)
```bash
mvn clean test
```
*Kết quả:* **47/47 tests PASS (100%)**

### 2. Chạy UI Automation Tests (Selenium + TestNG + Page Object Model)
```bash
# Chạy toàn bộ kiểm thử giao diện
mvn test -Pui

# Chạy riêng nhóm kiểm thử smoke (nhanh, trọng tâm)
mvn test -Pui -Dgroups=smoke

# Chạy có giao diện trình duyệt (không headless)
mvn test -Pui -Dgroups=smoke -Dheadless=false
```
*Kết quả:* **10/10 tests PASS (100%)**

### 3. Chạy ứng dụng trực tiếp
```bash
mvn spring-boot:run
```
Truy cập: `http://localhost:8080/login` hoặc `http://localhost:8080/register`.

## Bảng đối chiếu yêu cầu Assignment (Y1 - Y6)

| Yêu cầu | Mô tả | Vị trí trong mã nguồn | Trạng thái |
|:-------:|-------|-----------------------|:----------:|
| **Y1** | Ứng dụng web chạy hoàn chỉnh các chức năng | `src/main/java/com/example/shop/` | ✅ Hoàn thành |
| **Y2** | Thiết kế Test Case & Test Plan (RAG + Prompt) | `docs/TestPlan.md`, `docs/TestCases.md` | ✅ Hoàn thành |
| **Y3** | JUnit 5 + Mockito cho Service & MockMvc cho Controller | `src/test/java/com/example/shop/{service,controller}` | ✅ 47 tests PASS |
| **Y4** | Selenium + TestNG kiểm thử Đăng ký, Đăng nhập, CRUD (POM) | `src/test/java/com/example/shop/ui/` | ✅ 10 tests PASS |
| **Y5** | GitHub Actions CI/CD (push/PR, group smoke, upload reports) | `.github/workflows/selenium-ci.yml` | ✅ Hoàn thành |
| **Y6** | Nhật ký sử dụng AI (Prompt + Before/After + Debug + Review) | `docs/AI_Log.md` | ✅ Hoàn thành |

## Tổng kết số lượng Test Cases

| Phân loại | Công cụ | Số lượng test | Kết quả |
|-----------|---------|:-------------:|:-------:|
| Service Test (Logic nghiệp vụ) | JUnit 5 + Mockito | 31 | ✅ 31 Pass (0 Fail) |
| Controller Test (REST / Web MVC) | JUnit 5 + MockMvc | 16 | ✅ 16 Pass (0 Fail) |
| UI Automation Test (Giao diện POM) | Selenium + TestNG | 21 | ✅ 21 Pass (0 Fail) |
| **Tổng cộng** | | **68** | **✅ 68/68 Pass (100%)** |

## CI/CD Pipeline (GitHub Actions)

File workflow `.github/workflows/selenium-ci.yml`:
- **Triggers**: Tự động kích hoạt khi `push` hoặc tạo `pull_request` vào nhánh `main`/`master`, hoặc kích hoạt thủ công qua `workflow_dispatch`.
- **Job `unit-tests`**: Chạy JUnit 5 với Maven trên môi trường Ubuntu và Java 25, lưu và upload surefire report lên Artifacts.
- **Job `ui-tests`**: Phụ thuộc sau khi `unit-tests` thành công, tự động khởi tạo Chrome headless và chạy suite Selenium TestNG (hỗ trợ chọn nhóm `smoke`/`regression`/`all`), sau đó upload báo cáo lên Artifacts.

## Kỹ thuật thiết kế kiểm thử (Y2)

- **Decision Table**: Bảng quyết định cho ma trận đăng nhập (Username đúng/sai × Mật khẩu đúng/sai).
- **Boundary Value Analysis (BVA)**: Kiểm thử biên cho độ dài Username (2, 3, 50, 51) và Mật khẩu (5, 6).
- **Equivalence Partitioning**: Phân vùng tương đương cho định dạng Email và đơn giá sản phẩm.
- **Error Guessing**: Dự đoán lỗi với chuỗi null, rỗng, khoảng trắng, ký tự đặc biệt, injection.

## Ghi chú

1. **Cơ sở dữ liệu**: Hỗ trợ MySQL (XAMPP) cho môi trường thực tế và H2 in-memory cho môi trường kiểm thử tự động.
2. **Security**: Mật khẩu được mã hóa an toàn bằng BCrypt, bảo vệ phiên làm việc (Session-based Auth Guard).
3. **TestNG Groups**: Phân loại test theo `smoke`, `regression`, `auth`, `product`.
4. **Selenium Manager**: Tự động quản lý và tải driver trình duyệt tương thích.
5. **AI_Log.md**: Nhật ký minh chứng sử dụng AI ghi chép đầy đủ prompt và quyết định thiết kế (Y6).
