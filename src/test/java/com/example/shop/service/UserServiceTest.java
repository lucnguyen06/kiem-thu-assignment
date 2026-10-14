package com.example.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Test case UT-REG-xx va UT-LOGIN-xx (xem docs/TestCases.md). */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository);
    }

    // ---------- Register ----------

    @Test
    @DisplayName("UT-REG-01: dang ky hop le -> luu user, password duoc ma hoa")
    void should_SaveUserWithHashedPassword_When_RegisterWithValidData() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.register("alice", "secret1", "alice@mail.com");

        assertEquals("alice", saved.getUsername());
        assertNotEquals("secret1", saved.getPasswordHash());
        assertTrue(saved.getPasswordHash().startsWith("$2"));
    }

    @ParameterizedTest(name = "UT-REG-02: username [{0}] khong hop le")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "ab", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void should_Throw_When_UsernameInvalid(String username) {
        assertThrows(IllegalArgumentException.class,
                () -> service.register(username, "secret1", "a@b.com"));
        verify(userRepository, never()).save(any());
    }

    @ParameterizedTest(name = "UT-REG-03: password [{0}] khong hop le")
    @NullAndEmptySource
    @ValueSource(strings = {"a", "12345"})
    void should_Throw_When_PasswordTooShort(String password) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.register("alice", password, "a@b.com"));
        assertEquals("Password phai co it nhat 6 ky tu", ex.getMessage());
    }

    @ParameterizedTest(name = "UT-REG-04: email [{0}] khong hop le")
    @NullAndEmptySource
    @ValueSource(strings = {"abc", "a@b", "@b.com", "a b@c.com", "a@@b.com"})
    void should_Throw_When_EmailInvalid(String email) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.register("alice", "secret1", email));
        assertEquals("Email khong hop le", ex.getMessage());
    }

    @Test
    @DisplayName("UT-REG-05: username da ton tai -> bao loi")
    void should_Throw_When_UsernameAlreadyExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.register("alice", "secret1", "a@b.com"));

        assertEquals("Username da ton tai", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("UT-REG-06: bien - username dung 3 ky tu va password dung 6 ky tu")
    void should_Accept_When_BoundaryLengths() {
        when(userRepository.existsByUsername("abc")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = service.register("abc", "123456", "a@b.co");

        assertEquals("abc", saved.getUsername());
    }

    // ---------- Authenticate (Decision Table: 4 rule) ----------

    private User storedUser() {
        User u = new User("alice", new org.springframework.security.crypto.bcrypt
                .BCryptPasswordEncoder().encode("secret1"), "a@b.com");
        return u;
    }

    @Test
    @DisplayName("UT-LOGIN-01: dung username + dung password -> thanh cong")
    void should_ReturnUser_When_CredentialsCorrect() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(storedUser()));

        assertTrue(service.authenticate("alice", "secret1").isPresent());
    }

    @Test
    @DisplayName("UT-LOGIN-02: dung username + sai password -> that bai")
    void should_ReturnEmpty_When_PasswordWrong() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(storedUser()));

        assertFalse(service.authenticate("alice", "wrong").isPresent());
    }

    @Test
    @DisplayName("UT-LOGIN-03: sai username -> that bai")
    void should_ReturnEmpty_When_UsernameUnknown() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        assertFalse(service.authenticate("bob", "secret1").isPresent());
    }

    @Test
    @DisplayName("UT-LOGIN-04: null username hoac password -> that bai, khong nem exception")
    void should_ReturnEmpty_When_InputNull() {
        assertFalse(service.authenticate(null, "secret1").isPresent());
        assertFalse(service.authenticate("alice", null).isPresent());
        verify(userRepository, never()).findByUsername(any());
    }
}
