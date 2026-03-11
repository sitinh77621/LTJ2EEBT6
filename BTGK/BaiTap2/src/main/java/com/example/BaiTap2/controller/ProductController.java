package com.example.BaiTap2.controller;

import com.example.BaiTap2.model.Product;
import com.example.BaiTap2.model.Category;
import com.example.BaiTap2.repository.CategoryRepository;
import com.example.BaiTap2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    // Danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/products";
    }

    // Form thêm
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/create";
    }

    // Lưu
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam(value = "categoryId", required = false) Integer categoryId) {

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            product.setCategory(category);
        }

        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                String fileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();

                Files.copy(imageFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        return "redirect:/products";
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/edit";
    }

    // Cập nhật
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id,
                              @ModelAttribute Product form,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              @RequestParam(value = "categoryId", required = false) Integer categoryId) {

        Product product = productRepository.findById(id).orElseThrow();
        product.setName(form.getName());
        product.setPrice(form.getPrice());

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            product.setCategory(category);
        }

        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                String fileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();

                Files.copy(imageFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        return "redirect:/products";
    }

    // Xóa
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}