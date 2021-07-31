package com.hu.Virtualize.services.admin;

import com.hu.Virtualize.commands.admin.ProductCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.enums.ProductEnum;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.repositories.ShopRepository;
import com.hu.Virtualize.services.admin.service.ProductCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ProductCreateServiceImpl implements ProductCreateService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ShopRepository shopRepository;

    /**
     * This function will insert the product in shop.
     * @param productCommand product details.
     * @return admin details after create the product.
     */
    @Transactional
    @Override
    public AdminEntity insertProduct(ProductCommand productCommand) {
        Optional<ShopEntity> shopEntityOptional = shopRepository.findById(productCommand.getShopId());

        if(shopEntityOptional.isEmpty()) {
            log.error("Invalid Shop when user add product");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid shop when user add product");
        }

        ShopEntity shop = shopEntityOptional.get();

        ProductEntity product = new ProductEntity();
        product = convert(product, productCommand);

        shop.getShopProducts().add(product);

        shop = shopRepository.save(shop);
        return adminRepository.findByAdminId(productCommand.getAdminId());
    }

    /**
     * This function will update the product details.
     * @param productCommand product command
     * @return admin details after update the product.
     */
    @Transactional
    @Override
    public AdminEntity updateProduct(ProductCommand productCommand) {
        Optional<ShopEntity> shopEntityOptional = shopRepository.findById(productCommand.getShopId());

        if(shopEntityOptional.isEmpty()) {
            log.error("Invalid shop when user try to update product");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid shop when user try to update product");
        }
        ShopEntity shop = shopEntityOptional.get();

        ProductEntity shopProduct = null;

        for(ProductEntity product: shop.getShopProducts()) {
            if(product.getProductId().equals(productCommand.getProductId())) {
                shopProduct = product;
                shopProduct = convert(shopProduct, productCommand);
                break;
            }
        }

        // if product is not present in the shop.
        if(shopProduct == null) {
            log.error("Invalid Product in shop list");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Product in shop list");
        }

        // update product value
        shop.getShopProducts().add(shopProduct);
        shop = shopRepository.save(shop);
        return adminRepository.findByAdminId(productCommand.getAdminId());
    }

    /**
     * This function will delete the product in admin shop.
     * @param productCommand product command.
     * @return admin details after delete the product.
     */
    @Transactional
    @Override
    public AdminEntity deleteProduct(ProductCommand productCommand) {
        Optional<ShopEntity> shopEntityOptional = shopRepository.findById(productCommand.getShopId());

        if(shopEntityOptional.isEmpty()) {
            log.error("Invalid Shop in which product will delete");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Shop in which product will delete");
        }

        ShopEntity shop = shopEntityOptional.get();

        Set<ProductEntity> shopProduct = new HashSet<>();
        boolean presentProductInShop = false;

        for(ProductEntity product : shop.getShopProducts()) {
            if(product.getProductId().equals(productCommand.getProductId())) {
                presentProductInShop = true;
            } else {
                shopProduct.add(product);
            }
        }

        if(!presentProductInShop) {
            log.error("Product isn't present in given shop");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product isn't present in given shop");
        }

        shop.setShopProducts(shopProduct);
        shop = shopRepository.save(shop);

        productRepository.deleteById(productCommand.getProductId());

        return adminRepository.findByAdminId(productCommand.getAdminId());
    }

    /**
     * This function just update the values.
     * @param productEntity product entity
     * @param productCommand  product command
     * @return product entity
     */
    ProductEntity convert(ProductEntity productEntity, ProductCommand productCommand) {
        // update all details
        if(productCommand.getProductName() != null) {
            productEntity.setProductName(productCommand.getProductName());
        }

        if(productCommand.getProductPrice() != null) {
            productEntity.setProductPrice(productCommand.getProductPrice());
        }
        if(productCommand.getBrandName() != null) {
            productEntity.setBrandName(productCommand.getBrandName());
        }
        if(productCommand.getCategoryType() != null) {
            productEntity.setCategoryType(productCommand.getCategoryType());
        }
        if(productCommand.getProductType() != null) {
            productEntity.setProductType(productCommand.getProductType());
        }
        if(productCommand.getProductDescription() != null) {
            productEntity.setProductDescription(productCommand.getProductDescription());
        }
        if(productCommand.getProductImage() != null) {
            productEntity.setProductImage(productCommand.getProductImage());
        }
        return productEntity;
    }

    /**
     * It will send all type of product enum values.
     * @return list of product type.
     */
    @Transactional
    public List<String> getAllProductType() {
        List<String> types = new ArrayList<>();

        ProductEnum[] products = ProductEnum.values();

        for(ProductEnum product: products) {
            types.add(product.toString());
        }

        return types;
    }
}
