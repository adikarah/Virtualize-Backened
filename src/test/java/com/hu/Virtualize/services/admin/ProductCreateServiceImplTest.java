package com.hu.Virtualize.services.admin;

import com.hu.Virtualize.commands.admin.ProductCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.repositories.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.search.SearchTerm;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class ProductCreateServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private ProductCreateServiceImpl productCreateService;

    private ProductEntity product;

    private ShopEntity shopEntity;

    private AdminEntity admin;

    private ProductCommand productCommand;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        product = new ProductEntity(1L,"Rasgulla",500,"Ganesh Sweets","Restaurant",null,"100% Pure Ingredients",null,null);
        shopEntity = new ShopEntity(1L,"Ganesh Misthaan","Greater Noida","Sweet Shop","29AAACC1206D2ZB",null,null);
        admin = new AdminEntity("Praveen","parveen@deloitte.com","123");
        productCommand = new ProductCommand(1L,1L,1L,"Jeans",500,"Peter England","Clothes","Fashion","Awesome",null);
    }

    @Test
    void insertProduct() {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shopEntity));
        Set<ProductEntity> shopProducts = new HashSet<>();
        shopProducts.add(product);
        product = productCreateService.convert(product,productCommand);

        shopEntity.setShopProducts(shopProducts);

        shopEntity.getShopProducts().add(product);
        shopRepository.save(shopEntity);
        productCreateService.insertProduct(productCommand);

    }

    @Test
    void insertProductForInvalidShop(){
        when(shopRepository.findById(anyLong())).thenReturn(null);
        ProductCommand productCommand1 = new ProductCommand();
        assertThrows(ResponseStatusException.class, () -> productCreateService.insertProduct(productCommand1));
    }

    @Test
    void updateProduct() {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shopEntity));
        Set<ProductEntity> shopProducts = new HashSet<>();
        shopProducts.add(product);
        product = productCreateService.convert(product,productCommand);

        shopEntity.setShopProducts(shopProducts);
        productCommand.setProductId(product.getProductId());
        productCreateService.updateProduct(productCommand);

    }

    @Test
    void updateProductForInvalidShop(){

        when(shopRepository.findById(anyLong())).thenReturn(null);
        ProductCommand productCommand1 = new ProductCommand();
        assertThrows(ResponseStatusException.class, () -> productCreateService.updateProduct(productCommand1));

    }

    @Test
    void updateProductForInvalidProduct(){
        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shopEntity));

        Set<ProductEntity> shopProducts = new HashSet<>();
        shopProducts.add(product);

        shopEntity.setShopProducts(shopProducts);
        ProductCommand productCommand1 = new ProductCommand(1L,1L,2L,"Jeans",500,"Peter England","Clothes","Fashion","Awesome",null);

        assertThrows(ResponseStatusException.class,()-> productCreateService.updateProduct(productCommand1));
    }

    @Test
    void deleteProduct() {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shopEntity));

        Set<ProductEntity> shopProducts = new HashSet<>();

        shopProducts.add(product);

        product = productCreateService.convert(product,productCommand);
        shopEntity.setShopProducts(shopProducts);
        shopEntity.getShopProducts().add(product);
        shopRepository.save(shopEntity);
        productCreateService.deleteProduct(productCommand);
    }

    @Test
    void convert() {
    }

    @Test
    void getAllProductType() {
        List<String> types = new ArrayList<>();
        types = productCreateService.getAllProductType();
    }
}