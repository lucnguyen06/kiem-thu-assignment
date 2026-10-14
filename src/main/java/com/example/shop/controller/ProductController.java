package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CRUD san pham. Moi thao tac yeu cau da dang nhap (session attribute currentUser).
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private boolean notLoggedIn(HttpSession session) {
        return session.getAttribute(AuthController.SESSION_USER) == null;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("products", productService.findAll());
        model.addAttribute("currentUser", session.getAttribute(AuthController.SESSION_USER));
        return "products";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         HttpSession session) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "product-form";
        }
        productService.create(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("product", productService.findById(id));
            return "product-form";
        } catch (NoSuchElementException ex) {
            return "redirect:/products";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         HttpSession session) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            product.setId(id);
            return "product-form";
        }
        try {
            productService.update(id, product);
        } catch (NoSuchElementException ex) {
            return "redirect:/products";
        }
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (notLoggedIn(session)) {
            return "redirect:/login";
        }
        try {
            productService.delete(id);
        } catch (NoSuchElementException ex) {
            // san pham da bi xoa truoc do: bo qua
        }
        return "redirect:/products";
    }
}
