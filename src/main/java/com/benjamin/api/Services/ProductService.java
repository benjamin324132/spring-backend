package com.benjamin.api.Services;

import com.benjamin.api.Models.Product;
import com.benjamin.api.Repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<Product> getAllProducts(String keyword, Pageable pageable){
        Query query = new Query().with(pageable);

        List<Criteria> criteria = new ArrayList<>();

        if(keyword != null && !keyword.isEmpty()){
            criteria.add(Criteria.where("title").regex(keyword, "i"));
        }

        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(new Criteria[0])));
        }

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Product.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), Product.class)
        );
    }

    public Product createProduct(Product product){
        product.setCreatedAt(Instant.now().toString());
        product.setUpdatedAt(Instant.now().toString());
        return productRepository.insert(product);
    }

    public Optional<Product> getProductById(ObjectId id){
        return productRepository.findById(id);
    }

    public List<Product> getProductByCategory(String category){
        return  productRepository.findByCategory(category);
    }

    public Product updateProduct(
            @PathVariable ObjectId id,
            @RequestBody Map<String, String> payload){
        Optional<Product> productExist = productRepository.findById(id);

        if(!productExist.isPresent())
            return null;

        Product product = productExist.get();


        if(payload.get("title") != null)
            product.setTitle(payload.get("title"));

        if(payload.get("image") != null)
            product.setImage(payload.get("image"));

        if(payload.get("description") != null)
            product.setDescription(payload.get("description"));

        if(payload.get("category") != null)
            product.setCategory(payload.get("category"));

        if(payload.get("price") != null)
            product.setPrice(Double.parseDouble(payload.get("price")));

        if(payload.get("quantity") != null)
            product.setQuantity(Integer.valueOf(payload.get("quantity")));

        return productRepository.save(product);
    }
}
