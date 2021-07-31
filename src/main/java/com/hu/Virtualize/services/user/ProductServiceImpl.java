package com.hu.Virtualize.services.user;

import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.services.user.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This function will return all the product in the all shops
     * @return list of product
     */
    @Override
    public List<ProductEntity> getProduct() {
        List<ProductEntity> productEntities = productRepository.findAll();

        productEntities.sort((product1, product2) -> {
            if(product1.getProductName().equals(product2.getProductName())) {
                return product1.getProductPrice() - product2.getProductPrice();
            } else {
                return product1.getProductName().compareTo(product2.getProductName());
            }
        });
        return productEntities;
    }

    /**
     * This function will return product according to the filter.
     * @return list of product
     */
    @Transactional
    @Override
    public List<ProductEntity> getProduct(String category) {
        log.info("category: " + category);
        List<ProductEntity> productEntities = productRepository.findAllByCategoryType(category);

        productEntities.sort((product1, product2) -> {
            if(product1.getProductName().equals(product2.getProductName())) {
                return product1.getProductPrice() - product2.getProductPrice();
            } else {
                return product1.getProductName().compareTo(product2.getProductName());
            }
        });
        return productEntities;
    }

    /**
     * This function will insert the image in product entity
     * @param productId product id
     * @param multipartFile product image
     * @return status message
     */
    public String insertProductImage(Long productId, MultipartFile multipartFile) {
        try{
            ProductEntity productEntity = findProductById(productId);

            // convert MultipartFile into byte array and store in product entity
            Byte[] byteObjects = new Byte[multipartFile.getBytes().length];

            // copy the file data into byte array
            int i = 0;
            for (byte b : multipartFile.getBytes()){
                byteObjects[i++] = b;
            }

            // set the profile image
            productEntity.setProductImage(byteObjects);
            productEntity = productRepository.save(productEntity);
            log.info("Insert image for product is successfully done");
            return "Image update successfully for product: " + productEntity.getProductId();
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, e.getMessage());
        }
    }

    /**
     * This function will return the product entity by productId
     * @param productId product id
     * @return product entity
     */
    public ProductEntity findProductById(Long productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);

        // if productId isn't valid
        if(productEntity.isEmpty()) {
            log.error("Invalid product");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This productId isn't valid. Please enter valid productId");
        }
        return productEntity.get();
    }
}
