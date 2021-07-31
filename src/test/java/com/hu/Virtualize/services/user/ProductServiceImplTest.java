package com.hu.Virtualize.services.user;

import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.repositories.ProductRepository;
import com.hu.Virtualize.repositories.UserRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductEntity productEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        productEntity = new ProductEntity(1l, "Denim Jeans", 600, "Peter England", "Clothes", "Male", "Best jeans", null, null);

        byte[] image = "zhatab".getBytes();
        Byte[] productImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            productImage[i++] = x;
        }
        productEntity.setProductImage(productImage);
    }

    @Test
    void getProduct() {
        List<ProductEntity> data = new ArrayList<>();
        data.add(new ProductEntity(1l, "Denim Jeans", 600, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        data.add(new ProductEntity(2l, "Denim Jeans", 300, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        data.add(new ProductEntity(3l, "Denim Shirt", 700, "Peter England", "Clothes", "Male", "Best jeans", null, null));

        when(productRepository.findAll()).thenReturn(data);

        List<ProductEntity> productList = productService.getProduct();

        assertEquals(productList.get(0).getProductId(), 2l);
    }

    @Test
    void testGetProduct() {
        List<ProductEntity> data = new ArrayList<>();
        data.add(new ProductEntity(1l, "Levis Jeans", 600, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        data.add(new ProductEntity(2l, "Levis Jeans", 300, "Peter England", "Clothes", "Male", "Best jeans", null, null));
        data.add(new ProductEntity(3l, "Denim Shirt", 700, "Peter England", "Clothes", "Male", "Best jeans", null, null));

        when(productRepository.findAllByCategoryType(anyString())).thenReturn(data);

        List<ProductEntity> productList = productService.getProduct(anyString());
        assertEquals(productList.get(0).getProductId(), 3L);
    }

    @Test
    void insertProductImage() {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "praveen".getBytes());

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any())).thenReturn(productEntity);

        productService.insertProductImage(productEntity.getProductId(),multipartFile);
    }

    @Test
    void findProductById() {
        given(productRepository.findById(anyLong())).willReturn(Optional.of(productEntity));
        ProductEntity expected = productService.findProductById(anyLong());
        assertEquals(expected.getProductId(), productEntity.getProductId());
    }

    @Test
    void whenProductIdNotFound() {
        final Long id = 1L;
        given(productRepository.findById(id)).willReturn(null);
        assertThrows(ResponseStatusException.class, () -> productService.findProductById(anyLong()));
    }
}
