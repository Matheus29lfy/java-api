package com.exemplo.crud.controller;

import com.exemplo.crud.dto.ApiResponse;
import com.exemplo.crud.model.Product;
import com.exemplo.crud.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

@GetMapping
public ResponseEntity<ApiResponse<List<Product>>> all() {
    List<Product> products = repo.findAll();

    if (products.isEmpty()) {
        return ResponseEntity
                .status(404)
                .body(new ApiResponse<>("Nenhum produto cadastrado ainda.", null));
    }

    return ResponseEntity.ok(new ApiResponse<>("Lista de produtos", products));
}


    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(@Validated @RequestBody Product p) {
        Product saved = repo.save(p);
        return ResponseEntity.ok(new ApiResponse<>("Produto criado com sucesso!", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Validated @RequestBody Product p) {
        if (!repo.existsById(id)) {
             return ResponseEntity
                .status(404)
                .body(new ApiResponse<>("Nenhum produto cadastrado ainda.", null));
            // return ResponseEntity.notFound().build();
        }
        p.setId(id);
        Product updated = repo.save(p);
        return ResponseEntity.ok(new ApiResponse<>("Produto atualizado com sucesso!", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Produto deletado com sucesso!", null));
    }
}
