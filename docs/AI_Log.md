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

**Lỗi 3: Nâng cấp Java 25 LTS - Lỗi Mockito / Byte Buddy không nhận diện bytecode version 69**
```
Prompt cho AI:
Tôi vừa nâng cấp dự án từ Java 17 lên Java 25 LTS (bản phát hành dài hạn mới nhất của Oracle).
Khi biên dịch mã nguồn (mvn test-compile) thì thành công, nhưng khi chạy test (mvn test) thì toàn bộ các test có Mockito đều ném ngoại lệ:

Caused by: java.lang.IllegalStateException: Failed to resolve the class file version of the current VM: Unknown Java version: 0
    at net.bytebuddy.ClassFileVersion$VersionLocator$Unresolved.resolve(ClassFileVersion.java:676)
    at net.bytebuddy.ClassFileVersion.ofThisVm(ClassFileVersion.java:358)
    at org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker.<clinit>(InlineDelegateByteBuddyMockMaker.java:142)

Hãy giải thích nguyên nhân và đưa ra giải pháp cấu hình Maven chuẩn xác nhất.
```

**Cách AI hỗ trợ:**
```
1. Nguyên nhân:
   - Java 25 sử dụng định dạng file lớp (class file version) là 69.0.
   - Thư viện Byte Buddy cũ (1.14.x đi kèm Spring Boot 3.3.x) chưa có hằng số nhận diện bytecode 69.0, dẫn đến ClassFileVersion.ofThisVm() trả về 0 và gây lỗi khởi tạo MockMaker của Mockito.
2. Giải pháp khắc phục đồng bộ:
   a. Nâng cấp Spring Boot lên 3.5.16 (hỗ trợ Spring Framework 6.2+ và ASM mới cho Java 25).
   b. Bổ sung trực tiếp net.bytebuddy:byte-buddy và net.bytebuddy:byte-buddy-agent phiên bản 1.18.16 vào test scope của pom.xml.
   c. Cấu hình cờ JVM trong maven-surefire-plugin:
      <argLine>-Dnet.bytebuddy.experimental=true -XX:+EnableDynamicAgentLoading -Xmx1024m -XX:MaxMetaspaceSize=512m</argLine>
   d. Cập nhật file workflow .github/workflows/selenium-ci.yml: thay java-version '17' thành '25'.
```

**Cách tôi xử lý:**
- Cập nhật `assignment/pom.xml` với Spring Boot 3.5.16, Java 25, Byte Buddy 1.18.16 và cờ JVM tương ứng.
- Cập nhật `.github/workflows/selenium-ci.yml` sử dụng JDK Temurin 25.
- Thực thi kiểm tra: `mvn clean test` -> 47/47 tests JUnit (UserServiceTest, ProductServiceTest, AuthControllerTest, ProductControllerTest) đều PASS 100%.

**Lỗi 4: Xử lý môi trường Selenium UI Test cục bộ (Khắc phục triệt để cannot find Chrome binary)**
```
Prompt cho AI:
Khi chạy mvn test -Pui trên máy tính Windows cá nhân, Selenium WebDriver báo lỗi:
SessionNotCreatedException: cannot find Chrome binary
Do máy tính không cài đặt Google Chrome trên hệ thống và không có quyền Administrator để cài đặt phần mềm qua installer truyền thống.
Làm thế nào để chạy được Selenium test cục bộ mà không cần quyền Admin?
```

**Cách AI hỗ trợ:**
```
1. Trong Selenium 4.25+, công cụ selenium-manager tích hợp sẵn hỗ trợ tải trình duyệt Chromium/Chrome độc lập (standalone) trực tiếp vào thư mục người dùng (~/.cache/selenium).
2. Lệnh kích hoạt:
   ~/.cache/selenium/manager/0.4.25/selenium-manager.exe --browser chrome --force-browser-download
3. Sau khi tải, Selenium WebDriver sẽ tự động nhận diện và sử dụng Chrome trong cache người dùng mà không cần quyền Administrator.
```

**Cách tôi xử lý:**
- Chạy công cụ `selenium-manager` để tải trình duyệt Chrome tương thích vào cache người dùng.
- Khởi chạy lại: `mvn test -Pui -Dgroups=smoke` và `mvn test -Pui`.
- Kết quả: 10/10 test UI Selenium đều chạy thành công ở chế độ headless.

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

2. **Page Object Model (POM)** - Giảm duplication, tách biệt locators và business logic, dễ bảo trì

3. **Nâng cấp Runtime lên Java 25 LTS & Spring Boot 3.5.16** - Tối ưu hiệu năng JVM hiện đại, chu kỳ hỗ trợ dài hạn

4. **Tách biệt bài Lab ra khỏi dự án Assignment** - Tránh xung đột cấu hình suite TestNG, giữ mã nguồn Assignment tinh gọn đúng trọng tâm Y1-Y6

5. **CI/CD tự động hóa trên GitHub Actions** - Đảm bảo kiểm thử tự động trên môi trường chuẩn

### Quyết định của tôi:

| Đề xuất | Quyết định | Lý do |
|---------|------------|-------|
| TestNG thay JUnit cho UI | ✅ Đồng ý | Hỗ trợ Groups (`smoke`, `regression`) và DataProvider mạnh mẽ hơn cho UI |
| Áp dụng triệt để POM cho ASM | ✅ Đồng ý | `AuthUiTest` và `ProductCrudUiTest` dùng các Page Objects (`LoginPage`, `RegisterPage`, `ProductsPage`, `ProductFormPage`), code sạch và đáp ứng Y4 |
| Nâng cấp Java 25 LTS | ✅ Đồng ý | Đảm bảo tính hiện đại, kiểm tra khả năng tương thích của hệ thống kiểm thử trên LTS mới nhất |
| Ghim Byte Buddy 1.18.16 | ✅ Đồng ý | Khắc phục triệt để lỗi phân giải class file version 69.0 của Mockito MockMaker |
| Tách biệt Labs ra thư mục `labs/` | ✅ Đồng ý | Giữ thư mục `assignment/` chuẩn chỉ 100% cho đề tài Shop ASM (Y1-Y6), tránh lẫn lộn test Calculator và các bài thực hành trên lớp |
| CI/CD GitHub Actions | ✅ Đồng ý | Chạy tự động cả unit-tests và ui-tests khi push/PR, tự động tải report |

### Bài học rút ra:
1. **AI không hoàn hảo** - Cần review kỹ code AI sinh ra, đặc biệt là khi cấu hình tương thích giữa các phiên bản thư viện (Java 25 vs Byte Buddy vs Mockito).
2. **Môi trường quan trọng** - Cần cấu hình test có khả năng tự thích ứng trên cả máy phát triển cục bộ và runner CI/CD.
3. **Phân tách trách nhiệm (Separation of Concerns)** - Việc tách các bài Lab ra khỏi Assignment giúp bộ test của Assignment chạy nhanh, rõ ràng, đạt tỉ lệ pass 100%.
4. **Document everything** - Ghi lại prompt và quyết định thiết kế đầy đủ làm minh chứng cho tiêu chí Y6.

---

## 6. Tinh gọn dự án & Tách biệt bài Lab

### Đặt vấn đề:
Ban đầu dự án gộp chung các bài tập Lab (Calculator Basic, Calculator Parameterized, Exception, TestNG Annotations Lab 6, Listener Lab 7, Factory Lab 8) vào cùng dự án Shop ASM. Điều này dẫn đến:
- Số lượng test bị pha trộn giữa bài tập nhỏ (Lab) và ứng dụng thực tế (Shop ASM).
- Cấu hình suite TestNG (`testng-lab8.xml`) gặp xung đột `@Factory` giữa các class.
- Bộ test chạy lâu và không tập trung vào đánh giá nghiệp vụ Shop theo yêu cầu đề bài Y1-Y6.

### Giải pháp thực hiện cùng AI:
1. Di chuyển toàn bộ mã nguồn và test của các bài Lab sang thư mục riêng `labs/`:
   - `Calculator.java` và các test `Calculator*Test.java`.
   - Các test `Lab4*`, `Lab6*`, `Lab7*`, `Lab8*`.
   - Các cấu hình suite `testng-lab*.xml` và file dữ liệu `calculator-*.csv`.
2. Dự án `assignment/` được chuẩn hóa 100% cho ứng dụng Shop:
   - Tầng Service: `UserServiceTest`, `ProductServiceTest` (JUnit 5 + Mockito).
   - Tầng Controller: `AuthControllerTest`, `ProductControllerTest` (JUnit 5 + MockMvc).
   - Tầng UI: `AuthUiTest`, `ProductCrudUiTest` kết hợp bộ Page Objects đầy đủ.
3. Kết quả: Toàn bộ 57/57 tests của Assignment đều đạt trạng thái **PASS (100%)**.

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
