package com.hu.Virtualize.services.user;

import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.ShopRepository;
import com.hu.Virtualize.repositories.ProductRepository;

import com.hu.Virtualize.services.user.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;

    public ProductCategoryServiceImpl(ShopRepository shopRepository, ProductRepository productRepository) {
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
    }

    /**
     * This function will return all the shops name according to the product type
     * @param productType product type
     * @return list of product
     */
    @Transactional
    public List<String> getStoreName(String productType) {
        List<ProductEntity> productList = productRepository.findAllByCategoryType(productType);

        Set<String> shopNames = new HashSet<>();

        for(ProductEntity product : productList) {
            shopNames.add(product.getBrandName());
        }

        List<String> shops = new ArrayList<>(shopNames);
        Collections.sort(shops);

        log.info("Return all store according to the shop type");
        return shops;
    }

    /**
     * This function will return the product in product type.
     * @param productType product type
     * @return list of product.
     */
    @Transactional
    public List<String> getProductNames(String productType) {
        List<ProductEntity> productList = productRepository.findAllByCategoryType(productType);

        Set<String> productNames = new HashSet<>();

        for(ProductEntity product: productList) {
            productNames.add(product.getProductName());
        }

        List<String> products = new ArrayList<>(productNames);
        Collections.sort(products);

        log.info("Return all product items");
        return products;
    }
}
