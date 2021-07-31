package com.hu.Virtualize.controllers.admin;

import com.hu.Virtualize.commands.admin.ProductCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.services.admin.service.ProductCreateService;
import com.hu.Virtualize.services.admin.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/product")
@RestController
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductCreateService productCreateService;

    @Autowired
    private ShopService shopService;

    /**
     * This api will help you to add new shop under admin.
     * @param productCommand shop and admin details.
     * @return admin details
     */
    @PostMapping("/create")
    public ResponseEntity<?> insertShop(@RequestBody ProductCommand productCommand) {
        log.info("admin insert the new product in admin shop");
        AdminEntity admin = productCreateService.insertProduct(productCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will update the product details.
     * @param productCommand product details
     * @return updated details.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductCommand productCommand) {
        log.info("admin try to update the product details in his shop");
        AdminEntity admin = productCreateService.updateProduct(productCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will delete the product in admin shop.
     * @param productCommand product detail
     * @return updated details
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductCommand productCommand) {
        log.info("delete product in product");
        AdminEntity admin = productCreateService.deleteProduct(productCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This  function will return all available type of products.
     * @return list of products.
     */
    @GetMapping("/types")
    public ResponseEntity<?> getAllProductType() {
        List<String> types = productCreateService.getAllProductType();
        return new ResponseEntity<>(types,HttpStatus.OK);
    }
}
