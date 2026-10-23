# BÁO CÁO KẾT QUẢ THỰC HIỆN ASSIGNMENT
## Môn: Kiểm thử nâng cao cùng AI (Java) - Mã môn: SOF304
**Học viên thực hiện:** SinhVien  
**Dự án:** Ứng dụng Quản lý Shop (Spring Boot Web Application)  
**Môi trường:** Java 25 LTS, Spring Boot 3.5.16, Selenium 4.25.0, TestNG 7.10.2, JUnit 5  

---

## MỤC LỤC
1. [Tổng quan dự án & Yêu cầu đề bài](#1-tổng-quan-dự-án--yêu-cầu-đề-bài)
2. [Chi tiết thực hiện Y1: Ứng dụng nguồn Web Shop](#2-chi-tiết-thực-hiện-y1-ứng-dụng-nguồn-web-shop)
3. [Chi tiết thực hiện Y2: Thiết kế Test Plan & Test Cases](#3-chi-tiết-thực-hiện-y2-thiết-kế-test-plan--test-cases)
4. [Chi tiết thực hiện Y3: Kiểm thử JUnit 5 & Mockito](#4-chi-tiết-thực-hiện-y3-kiểm-thử-junit-5--mockito)
5. [Chi tiết thực hiện Y4: Tự động hóa kiểm thử Selenium & TestNG (POM)](#5-chi-tiết-thực-hiện-y4-tự-động-hóa-kiểm-thử-selenium--testng-pom)
6. [Chi tiết thực hiện Y5: CI/CD Pipeline với GitHub Actions](#6-chi-tiết-thực-hiện-y5-cicd-pipeline-với-github-actions)
7. [Chi tiết thực hiện Y6: Minh chứng ứng dụng AI qua 5 giai đoạn](#7-chi-tiết-thực-hiện-y6-minh-chứng-ứng-dụng-ai-qua-5-giai-đoạn)
8. [Bảng số liệu tổng hợp & Ma trận độ bao phủ](#8-bảng-số-liệu-tổng-hợp--ma-trận-độ-bao-phủ)
9. [Kịch bản & Câu hỏi phản biện khi bảo vệ Assignment](#9-kịch-bản--câu-hỏi-phản-biện-khi-bảo-vệ-assignment)

---

## 1. TỔNG QUAN DỰ ÁN & YÊU CẦU ĐỀ BÀI

Báo cáo này tổng hợp đầy đủ kết quả thực hiện đồ án môn **Kiểm thử nâng cao cùng AI (Java)** theo đúng 6 tiêu chí đánh giá trọng tâm trong `Assignment.pdf`:

| Tiêu chí | Nội dung yêu cầu | Trạng thái | Minh chứng |
|:---:|---|:---:|---|
| **Y1** | Ứng dụng Web Spring Boot chạy hoàn chỉnh các chức năng | ✅ Hoàn thành | `src/main/java/com/example/shop/` |
| **Y2** | Thiết kế Test Plan & Bộ Test Case chi tiết (kết hợp AI + RAG) | ✅ Hoàn thành | `docs/TestPlan.md`, `docs/TestCases.md` |
| **Y3** | Tích hợp JUnit 5, Mockito (Service) và MockMvc (Controller) | ✅ 47/47 PASS | `src/test/java/com/example/shop/{service,controller}` |
| **Y4** | Selenium + TestNG kiểm thử tự động UI theo mô hình POM | ✅ 10/10 PASS | `src/test/java/com/example/shop/ui/` |
| **Y5** | Đưa mã nguồn lên GitHub, tạo GitHub Actions CI/CD tự động | ✅ Hoàn thành | `.github/workflows/selenium-ci.yml` |
| **Y6** | Nhật ký sử dụng AI đầy đủ qua 5 giai đoạn bắt buộc | ✅ Hoàn thành | `docs/AI_Log.md` |

---

## 2. CHI TIẾT THỰC HIỆN Y1: ỨNG DỤNG NGUỒN WEB SHOP

- **Công nghệ nền tảng:**
  - Spring Boot 3.5.16 (nâng cấp tối ưu hóa cho Java 25 LTS).
  - Spring Data JPA + H2 In-Memory Database (tự động cấu hình, độc lập môi trường).
  - Spring Validation (`@Valid`, Hibernate Validator).
  - Spring Security Crypto (Mã hóa mật khẩu chuẩn BCrypt).
  - Thymeleaf Template Engine.
- **Các module chức năng chính:**
  1. *Đăng ký tài khoản (`/register`):* Ràng buộc độ dài username (3-50 ký tự), password tối thiểu 6 ký tự, định dạng email chuẩn, kiểm tra trùng lặp username trong database.
  2. *Đăng nhập (`/login`):* Xác thực thông tin, mã hóa BCrypt so khớp mật khẩu, thiết lập phiên làm việc (Session).
  3. *Bảo vệ quyền truy cập (Auth Guard):* Mọi truy cập vào `/products` khi chưa đăng nhập đều bị chặn và chuyển hướng về `/login`.
  4. *Quản lý sản phẩm CRUD (`/products`):* Xem danh sách, tạo mới sản phẩm, cập nhật thông tin sản phẩm và xóa sản phẩm.

---

## 3. CHI TIẾT THỰC HIỆN Y2: THIẾT KẾ TEST PLAN & TEST CASES

Đã áp dụng các kỹ thuật thiết kế ca kiểm thử chuẩn quốc tế (ISTQB) kết hợp kỹ thuật **Gen AI + RAG** (đưa business rules vào làm ngữ cảnh prompt):

### 3.1. Kỹ thuật áp dụng
1. **Decision Table (Bảng quyết định):**
   - Áp dụng cho module đăng nhập với 4 quy tắc: Username đúng/sai $\times$ Password đúng/sai $\rightarrow$ Đạt kết quả chuyển trang `/products` hoặc thông báo lỗi chung `"Đăng nhập không thành công"` (tránh lộ thông tin tài khoản).
2. **Boundary Value Analysis (Phân tích giá trị biên - BVA):**
   - Username: Kiểm thử tại các biên 2 ký tự (dưới min), 3 ký tự (min), 50 ký tự (max), 51 ký tự (trên max).
   - Password: Kiểm thử tại biên 5 ký tự (dưới min), 6 ký tự (min).
   - Giá sản phẩm: Giá 0 (dưới min), giá 1 (min).
3. **Equivalence Partitioning (Phân vùng tương đương):**
   - Email: Vùng hợp lệ (`alice@mail.com`, `bob@test.org`) vs Vùng không hợp lệ (`abc`, `a@b`, `@b.com`, `a b@c.com`).
   - Số lượng sản phẩm: Vùng hợp lệ ($\ge 0$) vs Vùng không hợp lệ ($< 0$).
4. **Error Guessing (Dự đoán lỗi):**
   - Kiểm thử với chuỗi `null`, chuỗi rỗng `""`, chuỗi chỉ chứa khoảng trắng `"   "`, SQL Injection (`' OR '1'='1`), Cross-Site Scripting (`<script>alert(1)</script>`).

Tài liệu chi tiết được lưu trữ tại:
- [docs/TestPlan.md](./TestPlan.md)
- [docs/TestCases.md](./TestCases.md)

---

## 4. CHI TIẾT THỰC HIỆN Y3: KIỂM THỬ JUNIT 5 & MOCKITO

Toàn bộ 47 test case đã được viết và chạy thành công trên JDK 25 LTS:

### 4.1. Tầng Service (`UserServiceTest`, `ProductServiceTest`)
- Sử dụng **JUnit 5 + Mockito 5.14.0 + Byte Buddy 1.18.16**:
  - `@Mock`: Mocking các Repository (`UserRepository`, `ProductRepository`).
  - `@InjectMocks`: Khởi tạo Service với các dependency giả lập.
  - `@ParameterizedTest` kết hợp `@ValueSource`, `@CsvSource`: Kiểm tra nhanh nhiều bộ dữ liệu biên và email không hợp lệ.
  - `verify(..., never())`: Đảm bảo không ghi dữ liệu vào database khi dữ liệu đầu vào không hợp lệ.

### 4.2. Tầng Controller (`AuthControllerTest`, `ProductControllerTest`)
- Sử dụng **Spring MockMvc**:
  - Giả lập các yêu cầu HTTP GET / POST mà không cần khởi động Tomcat thật.
  - Kiểm tra mã trạng thái HTTP (200 OK, 302 Found redirect).
  - Kiểm tra Model attributes, BindingResult errors và chuyển hướng Session.

**Kết quả thực thi:**
```
[INFO] Running com.example.shop.controller.AuthControllerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.shop.controller.ProductControllerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.shop.service.ProductServiceTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.shop.service.UserServiceTest
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 47, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 5. CHI TIẾT THỰC HIỆN Y4: TỰ ĐỘNG HÓA KIỂM THỬ SELENIUM & TESTNG (POM)

### 5.1. Kiến trúc Page Object Model (POM)
Hệ thống kiểm thử UI được thiết kế tách biệt 100% giữa **Giao diện (Locators & Actions)** và **Kịch bản kiểm thử (Test Scripts)**:
1. `LoginPage.java`: Quản lý các element `username`, `password`, nút đăng nhập, thông báo lỗi `#error`.
2. `RegisterPage.java`: Quản lý các ô nhập liệu, nút đăng ký, thông báo thành công `#msg-registered`.
3. `ProductsPage.java`: Quản lý danh sách bảng sản phẩm, nút thêm, nút sửa, nút xóa, hiển thị user hiện tại.
4. `ProductFormPage.java`: Quản lý form nhập thông tin sản phẩm (name, price, quantity), các thông báo lỗi `.field-error`.

### 5.2. Các kịch bản kiểm thử tự động
- **AuthUiTest (4 test methods / 6 test runs):**
  - Đăng ký tài khoản mới $\rightarrow$ Chuyển sang đăng nhập $\rightarrow$ Kiểm tra redirect vào `/products` và hiển thị đúng tên User.
  - Đăng ký dữ liệu không hợp lệ với TestNG `@DataProvider` (username ngắn, password ngắn, email sai định dạng).
  - Đăng nhập sai mật khẩu $\rightarrow$ Kiểm tra hiển thị thông báo lỗi.
  - Truy cập trực tiếp `/products` khi chưa đăng nhập $\rightarrow$ Kiểm tra tự động chuyển hướng về `/login`.
- **ProductCrudUiTest (4 test methods):**
  - **Create:** Tạo sản phẩm hợp lệ, kiểm tra hiển thị dòng mới trong bảng.
  - **Validation:** Nhập giá trị sai (tên rỗng, giá $\le 0$, số lượng $< 0$), kiểm tra hiển thị lỗi validation.
  - **Update:** Chỉnh sửa thông tin sản phẩm có sẵn, kiểm tra dữ liệu cập nhật thành công.
  - **Delete:** Nhấn xóa sản phẩm, kiểm tra dòng sản phẩm biến mất khỏi bảng.

**Kết quả thực thi:**
```
[INFO] Running TestSuite
[UI] App started at http://localhost:52902
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 21.50 s
[INFO] BUILD SUCCESS
```

---

## 6. CHI TIẾT THỰC HIỆN Y5: CI/CD PIPELINE VỚI GITHUB ACTIONS

Pipeline CI/CD được thiết lập trong file `.github/workflows/selenium-ci.yml`:

### 6.1. Cấu trúc Pipeline
1. **Trigger:**
   - Tự động kích hoạt khi `push` hoặc tạo `pull_request` trên các nhánh `main`, `master`.
   - Hỗ trợ kích hoạt thủ công qua `workflow_dispatch` với tùy chọn nhóm test (`smoke`, `regression`, `all`).
2. **Job `unit-tests`:**
   - Môi trường: `ubuntu-latest`, Temurin JDK 25, cache Maven.
   - Lệnh: `mvn -B test`.
   - Upload báo cáo: Lưu trữ `target/surefire-reports/` thành Artifact `junit-report`.
3. **Job `ui-tests`:**
   - Chạy tuần tự sau khi `unit-tests` đã PASS (`needs: unit-tests`).
   - Khởi động Selenium headless trên Ubuntu.
   - Lệnh: `mvn -B test -Pui -Dgroups=${{ matrix.group }}`.
   - Upload báo cáo: Lưu trữ báo cáo TestNG thành Artifact `testng-report-${{ matrix.group }}`.

---

## 7. CHI TIẾT THỰC HIỆN Y6: MINH CHỨNG ỨNG DỤNG AI QUA 5 GIAI ĐOẠN

Nhật ký đầy đủ được ghi nhận chi tiết tại [docs/AI_Log.md](./AI_Log.md):

1. **Giai đoạn 1 - Thiết kế Test Case & Test Plan (Gen AI + RAG):**
   - Đưa business rules về User, Password, Product làm ngữ cảnh RAG.
   - AI sinh Test Plan & khung ca kiểm thử; Sinh viên bổ sung dữ liệu kiểm thử biên cụ thể và tiêu chí nghiệm thu.
2. **Giai đoạn 2 - Viết Page Object & Test Script (Cursor / Copilot):**
   - AI sinh bản nháp các class Page Object.
   - Sinh viên refactor thêm `WebDriverWait`, cơ chế `isSuccessDisplayed()`, đóng gói các thao tác thành hàm nghiệp vụ cấp cao (`login()`, `register()`).
3. **Giai đoạn 3 - Xử lý lỗi & Debug thực tế:**
   - *Lỗi Mockito/Byte Buddy trên Java 25:* Gặp `Failed to resolve the class file version of the current VM: Unknown Java version: 0` $\rightarrow$ AI hướng dẫn nâng cấp Spring Boot 3.5.16, bổ sung `byte-buddy:1.18.16` và cờ JVM `-Dnet.bytebuddy.experimental=true -XX:+EnableDynamicAgentLoading`.
   - *Lỗi Chrome Binary trên máy Windows:* Khắc phục bằng `selenium-manager --force-browser-download` tải Chrome độc lập vào cache người dùng mà không cần quyền Admin.
4. **Giai đoạn 4 - Viết GitHub Actions CI/CD:**
   - AI sinh cấu hình YAML với 2 job độc lập, ma trận test groups và bước upload artifact.
5. **Giai đoạn 5 - Tối ưu & Review:**
   - Đưa ra quyết định tách riêng các bài thực hành Lab sang thư mục `labs/` độc lập để dự án `assignment/` chuẩn chỉ 100% cho đề tài Shop.
   - Review và nâng cấp hệ thống lên Java 25 LTS hiện đại.

---

## 8. BẢNG SỐ LIỆU TỔNG HỢP & MA TRẬN ĐỘ BAO PHỦ

### 8.1. Bảng số liệu kiểm thử

| Nhóm kiểm thử | Công cụ | Số lượng | Đạt (Pass) | Hỏng (Fail) | Bỏ qua (Skip) | Tỷ lệ đạt |
|---|---|:---:|:---:|:---:|:---:|:---:|
| Unit Test (Service) | JUnit 5 + Mockito | 31 | 31 | 0 | 0 | **100%** |
| Controller Test | JUnit 5 + MockMvc | 16 | 16 | 0 | 0 | **100%** |
| UI Automation Test | Selenium + TestNG (POM) | 10 | 10 | 0 | 0 | **100%** |
| **Tổng cộng** | | **57** | **57** | **0** | **0** | **100%** |

### 8.2. Ma trận độ bao phủ chức năng (Coverage Matrix)

| Chức năng nghiệp vụ | Unit Test | Controller Test | UI Automation Test (POM) |
|---|:---:|:---:|:---:|
| Đăng ký tài khoản mới | ✅ Đầy đủ BVA | ✅ MockMvc POST | ✅ Selenium End-to-end |
| Xác thực đăng nhập (BCrypt) | ✅ Decision Table | ✅ MockMvc Session | ✅ Selenium Form + Redirect |
| Kiểm tra phân quyền truy cập | N/A | ✅ Chặn truy cập | ✅ Chuyển hướng về Login |
| Xem danh sách sản phẩm | ✅ findAll | ✅ MockMvc GET | ✅ Đọc dữ liệu bảng |
| Thêm mới sản phẩm | ✅ create | ✅ MockMvc POST | ✅ Nhập form & xác nhận |
| Chỉnh sửa sản phẩm | ✅ update | ✅ MockMvc POST | ✅ Click Edit & lưu mới |
| Xóa sản phẩm | ✅ delete | ✅ MockMvc POST | ✅ Click Delete & xác nhận |
| Bắt lỗi Validation | ✅ BVA, Regex | ✅ BindingResult | ✅ Hiển thị `.field-error` |

---

## 9. KỊCH BẢN & CÂU HỎI PHẢN BIỆN KHI BẢO VỆ ASSIGNMENT

Dưới đây là một số câu hỏi giảng viên thường đặt ra và gợi ý câu trả lời chuẩn:

1. **Hỏi:** *Tại sao lại chọn mô hình Page Object Model (POM) cho Selenium?*  
   **Trả lời:** POM giúp tách biệt hoàn toàn giữa cấu trúc giao diện HTML (locators) và logic của các ca kiểm thử. Khi giao diện thay đổi ID hoặc class, ta chỉ cần sửa tại 1 vị trí trong Page class tương ứng mà không phải sửa hàng loạt test script, giúp mã nguồn dễ bảo trì và tái sử dụng cao.

2. **Hỏi:** *Sự khác nhau giữa kiểm thử Unit bằng Mockito và kiểm thử Controller bằng MockMvc là gì?*  
   **Trả lời:** Test Service với Mockito là unit test thuần túy tập trung vào logic nghiệp vụ và dữ liệu, mock hoàn toàn database. Còn MockMvc là kiểm thử tầng web (Web Layer test), kiểm tra việc ánh xạ URL, xử lý HTTP method, kiểm tra Validation của Spring và tính hợp lệ của Model/View mà không cần khởi động server nhúng thật.

3. **Hỏi:** *Em đã áp dụng kỹ thuật RAG (Retrieval-Augmented Generation) cùng AI như thế nào?*  
   **Trả lời:** Em không yêu cầu AI viết test chung chung, mà em đóng vai trò cung cấp tài liệu đặc tả nghiệp vụ (Business rules: độ dài username, password BCrypt, giá $\ge 1$) vào trong prompt làm nguồn tri thức (context). Nhờ đó, AI tạo ra bộ Test Plan và Test Case bám sát 100% nghiệp vụ thực tế của đồ án.

4. **Hỏi:** *Tại sao lại tách các bài Lab ra khỏi thư mục Assignment?*  
   **Trả lời:** Việc tách biệt giúp tuân thủ nguyên lý Single Responsibility. Dự án Assignment chỉ tập trung giải quyết bài toán Web Shop thực tế từ Y1-Y6, tránh xung đột cấu hình `@Factory` của TestNG và tối ưu thời gian chạy kiểm thử trên pipeline CI/CD.
