package com.hu.Virtualize.services.admin;

import com.hu.Virtualize.commands.admin.DiscountCommand;
import com.hu.Virtualize.entities.DiscountEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.DiscountRepository;
import com.hu.Virtualize.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DiscountServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DiscountServiceImpl discountService;

    private DiscountCommand discountCommand;

    private ProductEntity product;

    private DiscountEntity discountEntity;

    @BeforeEach
    void setup() throws ParseException {
        MockitoAnnotations.openMocks(this);
        discountCommand = new DiscountCommand(1L,1L,1L,1L,"Holi Offers",20,"2021-06-23","Get off on each fashion products");
        product = new ProductEntity(1L,"Rasgulla",500,"Ganesh Sweets","Restaurant",null,"100% Pure Ingredients",null,null);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-mm-dd");
        discountEntity = new DiscountEntity(1L,"Holi Offer",15, Date.valueOf("2021-06-23"),"Offers on all products");
    }

    @Test
    void insertDiscount() {
        DiscountEntity discount = discountService.convert(discountCommand);
        given(productRepository.findById(discountCommand.getProductId())).willReturn(Optional.of(product));

        Set<DiscountEntity> productDiscount = new HashSet<>();
        productDiscount.add(discountEntity);
        product.setProductDiscounts(productDiscount);
        product.getProductDiscounts().add(discount);
        productRepository.save(product);

        discountService.insertDiscount(discountCommand);
        verify(productRepository,times(2)).save(any(ProductEntity.class));
    }
}