package com.hu.Virtualize.services.admin.service;

import com.hu.Virtualize.commands.admin.ShopCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.entities.ShopEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ShopService {
    AdminEntity insertShop(ShopCommand shopCommand);
    AdminEntity updateShop(ShopCommand shopCommand);
    AdminEntity deleteShop(ShopCommand shopCommand);
    Set<ShopEntity> getAllShopsByAdminId(Long id);
    ShopEntity findShopById(Long shopId);
    String insertShopImage(Long shopId, MultipartFile multipartFile);
}
