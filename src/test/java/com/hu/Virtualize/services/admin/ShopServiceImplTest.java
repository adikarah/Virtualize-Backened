package com.hu.Virtualize.services.admin;

import com.hu.Virtualize.commands.admin.ShopCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ShopServiceImplTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private ShopServiceImpl shopService;

    private ShopCommand shopCommand;

    private ShopEntity shopEntity;

    private AdminEntity admin;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
         shopCommand = new ShopCommand(1L,2L,"Peter England","Delhi","Complete Fashion Shop", "29AAACC1206D2ZB");

        byte[] image = "parveen".getBytes();
        Byte[] shopImage = new Byte[image.length];
        int i=0;
        for(byte x : image) {
            shopImage[i++] = x;
        }

         shopEntity = new ShopEntity(1L,"Ganesh Misthaan","Greater Noida","Sweet Shop","29AAACC1206D2ZB" ,shopImage,null);
         admin = new AdminEntity("Praveen","parveen@deloitte.com","123");
         shopEntity.setShopImage(shopImage);
    }

//    @Test
//    void insertShopForValidAdmin() {
//
//        ShopEntity shopEntity = new ShopEntity(shopCommand.getShopName(), shopCommand.getGST());
//
//        final String location = "Gorakhpur";
//
//        if(shopCommand.getShopLocation()==null){
//            shopCommand.setShopLocation(location);
//        }
//
//        when(adminRepository.findByAdminId(1L)).thenReturn(admin);
//        admin.getAdminShops().add(shopEntity);
//        shopService.insertShop(shopCommand);
//        adminRepository.save(admin);
//        verify(adminRepository,times(2)).save(any(AdminEntity.class));
//    }

    @Test
    void insertShopForInvalidAdmin(){

        ShopServiceImpl shopService1 = mock(ShopServiceImpl.class);
        ShopEntity shopEntity = new ShopEntity(shopCommand.getShopName(),shopCommand.getGST());

        final String location = "Noida";

        if(shopCommand.getShopLocation()==null){
            shopCommand.setShopLocation(location);
        }

        when(adminRepository.findByAdminId(anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class,()-> shopService.insertShop(shopCommand));
        verify(adminRepository,never()).save(any(AdminEntity.class));

    }

    @Test
    void updateShop() {
        Set<ShopEntity> shops = new HashSet<>();
        shops.add(shopEntity);
//        admin.setAdminShops(shops);

        admin.getAdminShops().add(shopEntity);
        given(adminRepository.findByAdminId(anyLong())).willReturn(admin);
        shopCommand.setShopId(shopEntity.getShopId());
        AdminEntity updatedShop = shopService.updateShop(shopCommand);

    }

    @Test
    void updateShopForInvalidAdmin(){
        when(adminRepository.findByAdminId(anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> shopService.updateShop(shopCommand));
    }

    @Test
    void updateShopWhenShopNotPresent(){
        given(adminRepository.findByAdminId(anyLong())).willReturn(admin);
        boolean present = false;
        for(ShopEntity shop: admin.getAdminShops()){
            if(shop.getShopId().equals(shopCommand.getShopId())){
                present = true;
            }
        }
        assertEquals(false,present);
        assertThrows(ResponseStatusException.class, ()-> shopService.updateShop(shopCommand));
    }

    @Test
    void deleteShop() {
        Set<ShopEntity> shops = new HashSet<>();
        shops.add(shopEntity);

        admin.getAdminShops().add(shopEntity);
        given(adminRepository.findByAdminId(anyLong())).willReturn(admin);
        shopCommand.setShopId(shopEntity.getShopId());
        shopService.deleteShop(shopCommand);

    }

    @Test
    void deleteShopForInvalidAdmin(){
        given(adminRepository.findByAdminId(anyLong())).willReturn(null);
        ShopCommand shopCommand1 = new ShopCommand();
        assertThrows(ResponseStatusException.class, () -> shopService.deleteShop(shopCommand1));
    }

    @Test
    void deleteInvalidShop(){
        given(adminRepository.findByAdminId(anyLong())).willReturn(admin);
        assertThrows(ResponseStatusException.class, () ->shopService.deleteShop(shopCommand));
    }

    @Test
    void findShopById() {
        given(shopRepository.findById(anyLong())).willReturn(Optional.of(shopEntity));
        ShopEntity expected = shopService.findShopById(anyLong());
        assertEquals(expected.getShopId(),shopEntity.getShopId());

    }

    @Test
    void shopByIdNotFound(){
        ShopServiceImpl shopService1 = mock(ShopServiceImpl.class);
        final Long id = 1l;
        given(shopRepository.findById(id)).willReturn(Optional.of(shopEntity));
        assertThrows(ResponseStatusException.class,()-> shopService.findShopById(anyLong()));
    }
}