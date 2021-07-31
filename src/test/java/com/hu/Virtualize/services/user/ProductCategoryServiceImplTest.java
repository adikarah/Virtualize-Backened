package com.hu.Virtualize.services.user;

import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.repositories.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ProductCategoryServiceImplTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStoreName() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity(1l, "Denim Jeans", 600, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        products.add(new ProductEntity(2l, "Denim Jeans", 300, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        products.add(new ProductEntity(3l, "Denim Shirt", 700, "Peter England", "Clothes", "Male", "Best jeans", null, null));

        given(productRepository.findAllByCategoryType(anyString())).willReturn(products);
        Set<String> shopNames = new HashSet<>();
        List<String> shops = new ArrayList<>(shopNames);
        shops = productCategoryService.getStoreName(anyString());

    }

    @Test
    void getProductNames() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity(1l, "Denim Jeans", 600, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        products.add(new ProductEntity(2l, "Denim Jeans", 300, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        products.add(new ProductEntity(3l, "Denim Shirt", 700, "Peter England", "Clothes", "Male", "Best jeans", null, null));

        given(productRepository.findAllByCategoryType(anyString())).willReturn(products);
        Set<String> productNames = new HashSet<>();
        List<String> productsList = new ArrayList<>(productNames);
        productsList = productCategoryService.getProductNames(anyString());
    }
}