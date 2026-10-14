# Nhật ký sử dụng AI (Y6)

## Tổng quan
Dự án được tạo với sự hỗ trợ của AI (Cursor + Claude). File này ghi lại các prompt thật, 
quyết định thiết kế, và nhận xét cá nhân của sinh viên.

---

## 1. Thiết kế Test Case & Test Plan (Gen AI + RAG)

### Prompt thật sử dụng:
```
Tôi đang làm bài tập kiểm thử phần mềm. Cần tạo Test Plan cho ứng dụng 
Spring Boot với các chức năng:
- Đăng ký (username 3-50 ký tự, password tối thiểu 6 ký tự, email hợp lệ)
- Đăng nhập (BCrypt password)
- CRUD sản phẩm (tên không rỗng, giá >= 1, số lượng >= 0)

Yêu cầu:
1. Decision Table cho đăng nhập
2. Boundary Value Analysis cho username và password
3. Equivalence Partitioning cho email và giá sản phẩm
4. Error Guessing cho các trường hợp đặc biệt (null, empty, injection)
5. Mỗi test case cần có ID, mô tả, dữ liệu đầu vào, kết quả mong đợi

Nội dung này sẽ được dùng làm ngữ cảnh RAG cho việc tạo test tự động.
```

### Ban AI sinh ra:
```
# Test Plan - Shop ASM

## 1. Phạm vi
Ứng dụng Spring Boot (Thymeleaf, H2) gồm: đăng ký, đăng nhập, đăng xuất, CRUD sản phẩm.

## 2. Business rules
- Username: không rỗng, 3-50 ký tự, không trùng.
- Password: tối thiểu 6 ký tự, lưu dạng BCrypt.
- Email: đúng định dạng a@b.c.
- Đăng nhập sai: thông báo chung "Đăng nhập không thành công".
- Sản phẩm: tên không rỗng (tối đa 100), giá >= 1, số lượng >= 0.
```

### Chỉnh sửa của tôi:
1. **Bổ sung Test Cases chi tiết** - AI chỉ tạo Test Plan, tôi phải tự tạo Test Cases 
   với dữ liệu cụ thể (ví dụ: username 2 ký tự → fail, 3 ký tự → pass).
2. **Thêm phần lệnh chạy** - AI không cung cấp `mvn test`, `mvn test -Pui`, etc.
3. **Bổ sung tiêu chí nghiệm thu** - Tôi tự thêm "100% smoke tests pass".

### Prompt tiếp theo cho Test Cases:
```
Dựa trên Test Plan ở trên, hãy tạo Test Cases đầy đủ cho:
1. UserService (register, login) - sử dụng JUnit + Mockito
2. ProductService (CRUD) - sử dụng JUnit + Mockito
3. AuthController - sử dụng MockMvc
4. ProductController - sử dụng MockMvc

Mỗi test cần:
- ID duy nhất (UT-REG-01, CT-AUTH-01, etc.)
- Mô tả ngắn gọn
- Dữ liệu test cụ thể
- Kết quả mong đợi (assertion)
- Annotation phù hợp (@Test, @ParameterizedTest, etc.)
```

---

## 2. Page Object & Test Script (Cursor AI)

### Prompt cho Lab 4 - Page Object Model:
```
Tôi cần refactor UI tests sử dụng Page Object Model pattern. 

Ứng dụng có các trang:
1. /register - form đăng ký (username, email, password, nút đăng ký)
2. /login - form đăng nhập (username, password, nút đăng nhập)
3. /products - danh sách sản phẩm (bảng với tên, giá, số lượng, nút sửa/xóa)
4. /products/new và /products/edit/{id} - form sản phẩm (name, price, quantity)

Yêu cầu:
1. Tạo class RegisterPage với locators và actions
2. Tạo class LoginPage với locators và actions  
3. Tạo class ProductsPage với locators và actions
4. Tạo class ProductFormPage với locators và actions
5. Mỗi Page class cần có:
   - By locators cho các elements
   - Methods: open(), enter*(), click*(), get*()
   - Waits cho các trạng thái async

Sử dụng Selenium WebDriver và WebDriverWait.
```

### Code AI sinh vs code cuối cùng:

**AI sinh (phiên bản đầu):**
```java
public class RegisterPage {
    private By username = By.id("username");
    
    public void setUsername(String user) {
        driver.findElement(username).sendKeys(user);
    }
}
```

**Code cuối cùng (có cải thiện):**
```java
public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By usernameInput = By.id("username");
    
    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    public void enterUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }
    
    public void register(String username, String email, String password) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        clickRegister();
    }
    
    public boolean isSuccessDisplayed() {
        try {
            return driver.findElement(successMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
```

### Chỉnh sửa của tôi:
1. **Thêm WebDriverWait** - để có thể wait cho elements
2. **Thêm try-catch** cho `isSuccessDisplayed()` - tránh exception khi element không tồn tại
3. **Group methods** - gộp các action nhỏ thành `register()` method
4. **Thêm helper methods** - `getErrorMessage()`, `isOnRegisterPage()`, etc.

---

## 3. Xử lý lỗi & Debug

### Lỗi thật đã gặp:

**Lỗi 1: SessionNotCreatedException - Chrome not found**
```
Prompt cho AI:
```
Tôi đang chạy Selenium tests nhưng gặp lỗi:
```
SessionNotCreatedException: cannot find Chrome binary
```

Môi trường:
- Windows 10
- Chrome chưa cài đặt hoặc không tìm thấy đường dẫn
- Selenium Manager không hoạt động

Tôi đã thử:
- Cài Chrome nhưng vẫn lỗi
- Chạy headless nhưng không được

Giải pháp nào cho Windows? Có cách nào debug không?
```

**Cách AI hỗ trợ:**
```
1. Kiểm tra Chrome đã cài đặt chưa: `chrome://settings/help`
2. Đường dẫn mặc định Windows: `C:\Program Files\Google\Chrome\Application\chrome.exe`
3. Set Chrome binary path:
   ChromeOptions options = new ChromeOptions();
   options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
   
4. Alternative: Dùng Edge hoặc Firefox
5. CI: GitHub Actions Ubuntu có sẵn Chrome
```

**Cách tôi xử lý:**
1. Đã cài Chrome nhưng vẫn lỗi → Có thể là PATH environment variable
2. Tạm thời chạy CI trên GitHub Actions thay vì local
3. Thêm `--no-sandbox` và `--disable-dev-shm-usage` cho CI compatibility

**Lỗi 2: pom.xml thiếu dependency TestNG**
```
Prompt cho AI:
```
Maven build lỗi khi chạy TestNG tests:
```
Cannot find class org.testng.annotations.Test
```

pom.xml hiện tại:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

Làm sao để chạy được TestNG?
```

**Cách AI hỗ trợ:**
```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.10.2</version>
    <scope>test</scope>
</dependency>

<profile>
    <id>ui</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <dependencies>
                    <dependency>
                        <groupId>org.apache.maven.surefire</groupId>
                        <artifactId>surefire-testng</artifactId>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</profile>
```

**Cách tôi xử lý:**
- Đã thêm dependency TestNG theo hướng dẫn
- Tạo profile `ui` để tách biệt JUnit và TestNG
- Chạy: `mvn test -Pui` cho UI tests

---

## 4. GitHub Actions CI/CD

### Prompt cho workflow:
```
Tôi cần tạo GitHub Actions workflow để:
1. Chạy JUnit tests (mvn test) trên mọi push/PR
2. Chạy Selenium UI tests (mvn test -Pui) với Chrome headless
3. Chạy smoke tests mặc định, regression khi cần

Yêu cầu:
- Trigger: push, pull_request, manual (workflow_dispatch)
- Jobs: unit-tests (chạy trước), ui-tests (chạy sau, phụ thuộc unit-tests)
- Upload test reports (JUnit XML)
- Matrix strategy cho test groups (smoke, regression, all)
```

### Giải thích workflow:
```yaml
on:
  push:                      # Chạy khi push lên repo
  pull_request:              # Chạy khi có PR
  workflow_dispatch:         # Chạy thủ công
    inputs:
      test_group:
        type: choice
        options: [smoke, regression, all]
```

- **Job `unit-tests`**: Chạy JUnit với Maven, không cần Chrome
- **Job `ui-tests`**: Chạy Selenium với Chrome headless, phụ thuộc `unit-tests`
- **Matrix**: Cho phép chọn nhóm test khi chạy manual
- **Upload artifact**: Lưu test reports để xem chi tiết

### Trạng thái:
- ✅ Đã viết workflow
- ✅ Đã push lên GitHub
- ⏳ Đang chờ verify CI chạy thành công

---

## 5. Tối ưu & Review

### Đề xuất của AI:
1. **Sử dụng TestNG thay vì JUnit cho UI tests** - Vì TestNG có:
   - Groups (@Test(groups = {"smoke", "auth"}))
   - DataProvider
   - Dependency (dependsOnMethods)
   - Parallel execution

2. **Page Object Model** - Giảm duplication, dễ bảo trì

3. **CI/CD** - Tự động hóa, không cần Chrome local

### Quyết định của tôi:

| Đề xuất | Quyết định | Lý do |
|---------|------------|-------|
| TestNG thay JUnit cho UI | ✅ Đồng ý | Groups và DataProvider mạnh hơn |
| POM pattern | ✅ Đồng ý | Code sạch hơn, maintainable |
| CI/CD | ✅ Đồng ý | Không cần Chrome local |
| Parallel execution | ✅ Đồng ý | Lab 8 yêu cầu |
| ExtentReports | ⚠️ Tạm hoãn | Thêm complexity, chưa cần ngay |

### Bài học rút ra:
1. **AI không hoàn hảo** - Cần review kỹ code AI sinh ra
2. **Môi trường quan trọng** - CI có thể khác local environment
3. **Start simple** - Bắt đầu với JUnit đơn giản, sau đó mới thêm complexity
4. **Document everything** - Ghi lại prompt và quyết định cho Y6

---

## 6. Prompt cho Lab tiếp theo

### Lab 5 - Data-Driven:
```
Tạo CalculatorDataDrivenTest.java với các nguồn dữ liệu:
1. @CsvSource với test cases cho add, subtract, multiply, divide
2. @CsvFileSource đọc từ file /data/calculator.csv
3. @EnumSource cho Operation enum (ADD, SUBTRACT, etc.)
4. @MethodSource trả về Stream<Arguments>

Tạo file CSV: src/test/resources/data/calculator.csv
```

### Lab 6 - TestNG Annotations:
```
Tạo Lab6AnnotationTest.java với:
1. @BeforeSuite, @AfterSuite
2. @BeforeTest, @AfterTest  
3. @BeforeClass, @AfterClass
4. @BeforeMethod, @AfterMethod
5. @Test với groups, dependsOnMethods, enabled, invocationCount
6. @DataProvider trong class riêng
```

### Lab 7 - Reporting:
```
Tạo Lab7TestListener.java implements ITestListener:
- onStart, onFinish
- onTestStart, onTestSuccess, onTestFailure, onTestSkipped
- Ghi log ra file txt

Tạo Lab7Logger.java với:
- Custom logging methods (info, debug, warn, error)
- Ghi log ra file với timestamp
```

### Lab 8 - Advanced:
```
Tạo Lab8AdvancedTest.java với:
1. @Factory cho cross-browser testing (chrome, firefox, edge)
2. @Factory cho data-driven với nhiều data sets
3. RetryAnalyzer cho flaky tests
4. Parallel execution configuration trong testng.xml
```

---

## 7. Nhận xét cá nhân

### Ưu điểm khi dùng AI:
- **Nhanh** - Tạo code boilerplate trong vài giây
- **Đầy đủ** - Cung cấp cả test, documentation, CI/CD
- **Gợi ý** - Đề xuất best practices (POM, Data-Driven)

### Nhược điểm:
- **Không hiểu ngữ cảnh** - Đôi khi sinh code không phù hợp
- **Cần review** - Phải kiểm tra kỹ trước khi dùng
- **Lỗi không rõ ràng** - Debug code AI khó hơn

### Khuyến nghị cho sinh viên:
1. **Học lý thuyết trước** - Hiểu JUnit, TestNG, Selenium cơ bản
2. **Dùng AI như công cụ** - Không phải để thay thế hoàn toàn
3. **Review code** - Đọc và hiểu mọi dòng code AI sinh
4. **Tự viết lại** - Thử tự viết không có AI, so sánh

### Thời gian sử dụng AI:
- Thiết kế ban đầu: ~30% prompt engineering
- Code implementation: ~70% AI hỗ trợ
- Review & debug: ~50% tự làm
- Documentation: ~80% AI hỗ trợ
