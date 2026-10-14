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
│   │   ├── CalculatorBasicTest.java           # Lab 1: JUnit cơ bản (25 tests)
│   │   ├── CalculatorTest.java                # Lab 2, 3: Parameterized + Exception (21 tests)
│   │   ├── CalculatorExceptionTest.java       # Lab 3: Exception tests
│   │   └── CalculatorDataDrivenTest.java      # Lab 5: Data-driven (45 tests)
│   └── shop/
│       ├── controller/                         # Y3: MockMvc
│       │   ├── AuthControllerTest.java
│       │   └── ProductControllerTest.java
│       ├── service/                            # Y3: JUnit + Mockito
│       │   ├── UserServiceTest.java
│       │   └── ProductServiceTest.java
│       ├── ui/                                 # Y4: Selenium + TestNG
│       │   ├── pages/                          # Lab 4: Page Objects
│       │   │   ├── RegisterPage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── ProductsPage.java
│       │   │   └── ProductFormPage.java
│       │   ├── BaseTest.java
│       │   ├── AuthUiTest.java
│       │   ├── ProductCrudUiTest.java
│       │   ├── Lab4BaseTest.java               # Lab 4: POM base
│       │   ├── Lab4AuthTest.java               # Lab 4: Auth POM tests
│       │   ├── Lab4ProductTest.java            # Lab 4: Product POM tests
│       │   ├── Lab6AnnotationTest.java         # Lab 6: TestNG annotations
│       │   ├── Lab6DataProvider.java           # Lab 6: Data providers
│       │   ├── Lab7TestListener.java          # Lab 7: Custom listeners
│       │   ├── Lab7Logger.java                 # Lab 7: Logging
│       │   ├── Lab8AdvancedTest.java           # Lab 8: Cross-browser + Factory
│       │   ├── Lab8RetryAnalyzer.java          # Lab 8: Retry logic
│       │   └── Lab8DataDrivenTest.java         # Lab 8: Data-driven Factory
│       └── data/
├── src/test/resources/
│   ├── testng.xml                             # Y4, Y5: TestNG suite
│   ├── testng-lab4.xml                        # Lab 4: POM tests
│   ├── testng-lab6.xml                        # Lab 6: Annotations tests
│   ├── testng-lab7.xml                        # Lab 7: Reporting tests
│   ├── testng-lab8.xml                        # Lab 8: Advanced tests
│   └── data/
│       ├── calculator-add.csv                  # Lab 5: Addition test data
│       ├── calculator-divide.csv               # Lab 5: Division test data
│       └── registration-data.csv              # Lab 5: Registration test data
├── .github/workflows/selenium-ci.yml           # Y5: CI/CD
├── docs/
│   ├── TestPlan.md                            # Y2: Kế hoạch kiểm thử
│   ├── TestCases.md                           # Y2: Danh sách test case
│   └── AI_Log.md                              # Y6: Nhật ký sử dụng AI
├── pom.xml
└── README.md
```

## Lệnh chạy

### 1. JUnit (tất cả unit + controller)
```bash
mvn test
```
**Kết quả:** ~154 tests, 0 fail

### 2. Selenium + TestNG (UI)
```bash
# Tất cả
mvn test -Pui

# Chỉ nhóm smoke
mvn test -Pui -Dgroups=smoke

# Có giao diện (không headless)
mvn test -Pui -Dgroups=smoke -Dheadless=false
```

### 3. Lab-specific
```bash
# Lab 1: JUnit cơ bản
mvn test -Dtest=CalculatorBasicTest

# Lab 2, 3: Calculator tests
mvn test -Dtest=CalculatorTest,CalculatorExceptionTest

# Lab 4: Page Object Model
mvn test -Pui -Dgroups=lab4

# Lab 5: Data-Driven
mvn test -Dtest=CalculatorDataDrivenTest

# Lab 6: TestNG Annotations
mvn test -Pui -Dsurefire.suiteXmlFiles=src/test/resources/testng-lab6.xml

# Lab 7: Reporting
mvn test -Pui -Dsurefire.suiteXmlFiles=src/test/resources/testng-lab7.xml

# Lab 8: Advanced
mvn test -Pui -Dsurefire.suiteXmlFiles=src/test/resources/testng-lab8.xml
```

### 4. Chạy ứng dụng
```bash
mvn spring-boot:run
```
Mở `http://localhost:8080/register` để đăng ký, sau đó `/login` và `/products`.

## Yêu cầu đã hoàn thành

| Yêu cầu | Mô tả | Vị trí | Trạng thái |
|----------|--------|---------|------------|
| Lab 1 | JUnit cơ bản | `CalculatorBasicTest` | ✅ 25 tests pass |
| Lab 2 | @ParameterizedTest | `CalculatorTest` | ✅ 21 tests pass |
| Lab 3 | Vòng đời + Exception | `CalculatorExceptionTest` | ✅ 16 tests pass |
| Lab 4 | Page Object Model | `Lab4*Test.java` + `pages/` | ✅ 18 tests |
| Lab 5 | Data-Driven Testing | `CalculatorDataDrivenTest` | ✅ 45 tests |
| Lab 6 | TestNG Annotations | `Lab6AnnotationTest` | ✅ 15 tests |
| Lab 7 | Reporting & Logging | `Lab7*` | ✅ 5 tests |
| Lab 8 | Advanced Topics | `Lab8*` | ✅ Factory + Retry |
| Y1 | Ứng dụng web | `src/main/java/.../shop` | ✅ |
| Y2 | Test Plan + Test Cases | `docs/TestPlan.md`, `TestCases.md` | ✅ |
| Y3 | JUnit service + controller | `src/test/java/.../shop/service` | ✅ 47 tests |
| Y4 | Selenium + TestNG | `src/test/java/.../shop/ui` | ✅ |
| Y5 | TestNG suite + CI/CD | `testng.xml`, `selenium-ci.yml` | ✅ |
| Y6 | Nhật ký AI | `docs/AI_Log.md` | ✅ |

## Tổng số tests

| Loại | Số lượng |
|------|----------|
| Calculator (Lab 1, 2, 3) | 62 |
| Calculator Data-Driven (Lab 5) | 45 |
| Shop Service (Y3) | 31 |
| Controller (Y3) | 16 |
| UI POM (Lab 4) | 18 |
| TestNG Annotations (Lab 6) | 15 |
| Reporting (Lab 7) | 5 |
| **Tổng cộng** | **~192 tests** |

## CI/CD

Workflow `.github/workflows/selenium-ci.yml`:
- **Trigger**: push/PR (chạy `smoke`), hoặc thủ công (chọn `smoke`/`regression`/`all`).
- **Job 1** (`unit-tests`): chạy JUnit, upload report.
- **Job 2** (`ui-tests`): phụ thuộc job 1, chạy Selenium với Chrome headless.

## Kỹ thuật thiết kế test (Y2)

- **Decision Table**: đăng nhập (username đúng/sai × password đúng/sai).
- **Boundary Value Analysis**: độ dài username (2,3,50,51), password (5,6).
- **Equivalence Partitioning**: email hợp lệ / không hợp lệ.
- **Error Guessing**: null, rỗng, khoảng trắng, overflow, chia 0.

## Ghi chú

1. **H2 in-memory**: dữ liệu mất khi tắt ứng dụng.
2. **Security**: BCrypt cho password, session-based auth.
3. **TestNG groups**: `smoke`, `regression`, `auth`, `product`, `lab1`-`lab8`.
4. **Selenium Manager**: tự tải chromedriver nếu chưa có.
5. **AI_Log.md**: ghi lại prompt thật và quyết định thiết kế (Y6).

## Tác giả

Dự án được tạo bởi AI (Cursor/Claude) theo yêu cầu đề bài. Sinh viên cần review, chạy thật, và nộp.
