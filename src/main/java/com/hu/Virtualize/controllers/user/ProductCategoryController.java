package com.hu.Virtualize.controllers.user;

import com.hu.Virtualize.services.user.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/productCategory")
@RestController
@CrossOrigin("*")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * This function will fetch the branch name according to the type.
     * @param productType product type.
     * @return list of store name
     */
    @GetMapping("/stores/{productType}")
    public ResponseEntity<?> getStoreName(@PathVariable String productType) {
        List<String> shopsName = productCategoryService.getStoreName(productType);
        return new ResponseEntity<>(shopsName, HttpStatus.OK);
    }

    /**
     * This function will return all available products according to product type.
     * @return list of products.
     */
    @GetMapping("/products/{productType}")
    public ResponseEntity<?> getProductNames(@PathVariable String productType) {
        List<String> productName = productCategoryService.getProductNames(productType);
        return new ResponseEntity<>(productName, HttpStatus.OK);
    }
}
