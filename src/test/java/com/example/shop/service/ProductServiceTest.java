package com.example.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Test case UT-PRD-xx (CRUD san pham). */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(repository);
    }

    @Test
    @DisplayName("UT-PRD-01: findAll tra ve danh sach tu repository")
    void should_ReturnAll_When_FindAll() {
        when(repository.findAll()).thenReturn(List.of(new Product("A", 10, 1), new Product("B", 20, 2)));

        assertEquals(2, service.findAll().size());
    }

    @Test
    @DisplayName("UT-PRD-02: findById ton tai -> tra ve san pham")
    void should_ReturnProduct_When_IdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Product("A", 10, 1)));

        assertEquals("A", service.findById(1L).getName());
    }

    @Test
    @DisplayName("UT-PRD-03: findById khong ton tai -> NoSuchElementException")
    void should_Throw_When_IdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> service.findById(99L));
        assertEquals("Khong tim thay san pham id=99", ex.getMessage());
    }

    @Test
    @DisplayName("UT-PRD-04: create luon reset id ve null de tao ban ghi moi")
    void should_ResetId_When_Create() {
        Product input = new Product("A", 10, 1);
        input.setId(123L);
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product saved = service.create(input);

        assertNull(saved.getId());
    }

    @Test
    @DisplayName("UT-PRD-05: update cap nhat day du cac truong")
    void should_UpdateFields_When_Update() {
        Product existing = new Product("Old", 10, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = service.update(1L, new Product("New", 99, 5));

        assertEquals("New", result.getName());
        assertEquals(99, result.getPrice());
        assertEquals(5, result.getQuantity());
    }

    @Test
    @DisplayName("UT-PRD-06: update id khong ton tai -> NoSuchElementException, khong save")
    void should_Throw_When_UpdateMissing() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(7L, new Product("X", 1, 1)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("UT-PRD-07: delete ton tai -> goi deleteById")
    void should_Delete_When_IdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Product("A", 10, 1)));

        service.delete(1L);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    @Test
    @DisplayName("UT-PRD-08: delete id khong ton tai -> NoSuchElementException, khong xoa")
    void should_Throw_When_DeleteMissing() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.delete(5L));
        verify(repository, never()).deleteById(any());
    }
}
