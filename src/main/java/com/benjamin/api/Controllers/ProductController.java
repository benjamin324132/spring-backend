package com.benjamin.api.Controllers;

import com.benjamin.api.Models.Product;
import com.benjamin.api.Models.ProductResponse;
import com.benjamin.api.Services.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product product){

        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ProductResponse> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page
    ){
        Pageable pageable =  PageRequest.of(page != null ? page - 1 : 0, 15);
        Page<Product> data = productService.getAllProducts(keyword, pageable);
        List<Product> products = data.getContent();
        Integer currentPage = data.getNumber() + 1;
        Integer totalPages = data.getTotalPages();
        ProductResponse productResponse = new ProductResponse(products, currentPage, totalPages);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductbyId(@PathVariable ObjectId id){

        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable ObjectId id,
            @RequestBody Map<String, String> payload){

        return new ResponseEntity<>(productService.updateProduct(id, payload), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductbyCategory(
            @PathVariable String category){

        return new ResponseEntity<>(productService.getProductByCategory(category), HttpStatus.OK);
    }
}
