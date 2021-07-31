package com.hu.Virtualize.controllers.user;

import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.services.user.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RequestMapping("/product")
@RestController
@CrossOrigin("*")
public class UserDashboardController {
    private final ProductService productService;

    public UserDashboardController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * This function will return all the product present in every shop.
     * @return list of product.
     */
    @GetMapping({"","/"})
    public ResponseEntity<?> getProduct() {
        List<ProductEntity> products = productService.getProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * This function will return the specific type of product according to the category.
     * @param category category
     * @return list of product.
     */
    @GetMapping("/{category}")
    public ResponseEntity<?> getProduct(@PathVariable String category) {
        List<ProductEntity> products = productService.getProduct(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * This function will insert the product image.
     * @param productId product id
     * @param multipartFile product image
     * @return status message
     */
    @PostMapping("/insertImage/{productId}")
    public ResponseEntity<String> insertProductImage(@PathVariable String productId, @RequestParam("image") MultipartFile multipartFile) {
        log.info("User try to change the product image");
        String status = productService.insertProductImage(Long.valueOf(productId), multipartFile);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * This function will render the product image.
     * @param productId product id
     * @param response http servlet stream
     */
    @GetMapping("/image/{productId}")
    public void renderImageFromDB(@PathVariable String productId, HttpServletResponse response) {
        try {
            ProductEntity productEntity = productService.findProductById(Long.valueOf(productId));

            byte[] byteArray = new byte[productEntity.getProductImage().length];

            int i = 0;
            for (Byte wrappedByte : productEntity.getProductImage()) {
                byteArray[i++] = wrappedByte; //auto unboxing
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            log.error("Image fetch error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}