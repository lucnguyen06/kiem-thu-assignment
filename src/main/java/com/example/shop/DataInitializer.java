package com.example.shop;

import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Tu dong khoi tao tai khoan mau va danh sach san pham mau khi khoi dong.
 * Hoat dong tuong thich 100% voi ca MySQL (runtime) va H2 Database (test/CI).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Khoi tao tai khoan mau neu chua co
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", encoder.encode("admin123"), "admin@shop.com"));
        }
        if (!userRepository.existsByUsername("alice")) {
            userRepository.save(new User("alice", encoder.encode("secret1"), "alice@shop.com"));
        }

        // 2. Khoi tao danh sach san pham mau neu bang trong
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product("Laptop Dell XPS 15", 45000000L, 15),
                    new Product("Áo thun đen", 130000L, 45),
                    new Product("Áo sơ mi trắng", 250000L, 30),
                    new Product("Áo sơ mi xanh kẻ caro", 280000L, 25),
                    new Product("Quần jean nam xanh", 450000L, 40),
                    new Product("Quần jean nữ đen", 420000L, 38),
                    new Product("Quần kaki nam be", 350000L, 35),
                    new Product("Đầm maxi hoa nhí", 380000L, 20),
                    new Product("Đầm công sở ngắn tay", 450000L, 18),
                    new Product("Váy xòe hoa tím", 320000L, 22),
                    new Product("Áo khoác jean nam", 550000L, 15),
                    new Product("Áo khoác dạ nữ", 480000L, 12),
                    new Product("Áo hoodie unisex ghi", 390000L, 60),
                    new Product("Áo hoodie unisex đen", 390000L, 55),
                    new Product("Quần short thể thao", 180000L, 70),
                    new Product("Quần dài thể thao", 220000L, 65),
                    new Product("Bộ thể thao nam", 560000L, 25),
                    new Product("Bộ đồ ngủ nữ", 290000L, 30),
                    new Product("Mũ bucket trắng", 95000L, 80),
                    new Product("Mũ lưỡi trai đen", 85000L, 90),
                    new Product("Túi tote canvas", 150000L, 60),
                    new Product("Balo laptop 15 inch", 650000L, 20),
                    new Product("Thắt lưng da nam đen", 180000L, 35),
                    new Product("Ví da nam nâu", 320000L, 28),
                    new Product("Tất cổ ngắn (3 đôi)", 55000L, 120)
            ));
        }
    }
}
