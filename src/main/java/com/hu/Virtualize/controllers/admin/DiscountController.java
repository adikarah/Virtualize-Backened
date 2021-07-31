package com.hu.Virtualize.controllers.admin;

import com.hu.Virtualize.commands.admin.DiscountCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.services.admin.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/admin/discount")
@RestController
@CrossOrigin("*")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    /**
     * This function will insert the discount on product
     * @param discountCommand discount details.
     * @return all details object
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertDiscount(@RequestBody DiscountCommand discountCommand) {
        log.info("Admin insert the discount on product");
        AdminEntity admin = discountService.insertDiscount(discountCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will delete the discount on product.
     * @param discountCommand product details.
     * @return all remains details.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDiscount(@RequestBody DiscountCommand discountCommand) {
        log.info("admin try to delete the discount on product");
        AdminEntity admin = discountService.deleteDiscount(discountCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }
}
