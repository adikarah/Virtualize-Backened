package com.hu.Virtualize.services.admin;

import com.hu.Virtualize.commands.admin.DiscountCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.DiscountEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.DiscountRepository;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.services.admin.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * This function will insert the discount on product particular in particular shop
     * @param discountCommand discount details
     * @return all details
     */
    @Transactional
    @Override
    public AdminEntity insertDiscount(DiscountCommand discountCommand) {
       DiscountEntity discount = convert(discountCommand);

        Optional<ProductEntity> productEntity = productRepository.findById(discountCommand.getProductId());

        if(productEntity.isEmpty()) {
            log.error("Invalid product");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product when admin insert discount on product");
        }
        ProductEntity product = productEntity.get();

        product.getProductDiscounts().add(discount);
        product = productRepository.save(product);

        return adminRepository.findByAdminId(discountCommand.getAdminId());
    }

    /**
     * This function will delete the discount on particular product in particular shop.
     * @param discountCommand discount details.
     * @return remain details.
     */
    @Transactional
    @Override
    public AdminEntity deleteDiscount(DiscountCommand discountCommand) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(discountCommand.getProductId());

        if(productEntityOptional.isEmpty()) {
            log.error("Invalid product");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product when admin delete discount on product");
        }

        ProductEntity product = productEntityOptional.get();

        // add all remain discount in this list.
        Set<DiscountEntity> productDiscount = new HashSet<>();

        for(DiscountEntity discount: product.getProductDiscounts()) {
            if(!discount.getDiscountId().equals(discountCommand.getDiscountId())) {
                productDiscount.add(discount);
            }
        }

        product.setProductDiscounts(productDiscount);
        product = productRepository.save(product);

        // delete discount entity in database
        discountRepository.deleteById(discountCommand.getDiscountId());

        return adminRepository.findByAdminId(discountCommand.getAdminId());
    }

    /**
     * This function will convert the discount command into entity.
     * @param discountCommand discount details.
     * @return discount entity
     */
    public DiscountEntity convert(DiscountCommand discountCommand) {
        DiscountEntity discount = new DiscountEntity();

        if(discountCommand.getDiscountName() != null) {
            discount.setDiscountName(discountCommand.getDiscountName());
        }

        if(discountCommand.getDiscountPercentage() != null) {
            discount.setDiscountPercentage(discountCommand.getDiscountPercentage() );
        }

        if(discountCommand.getEndDate() != null) {
            discount.setEndDate(Date.valueOf(discountCommand.getEndDate()));
        }

        if(discountCommand.getDiscountDescription() != null) {
            discount.setDiscountDescription(discountCommand.getDiscountDescription());
        }
        return discount;
    }
}
