package com.hu.Virtualize.controllers.admin;

import com.hu.Virtualize.commands.admin.ShopCommand;
import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.services.admin.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Slf4j
@RequestMapping("/admin/shop")
@RestController
@CrossOrigin("*")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * This api will help you to add new shop under admin.
     * @param shopCommand shop and admin details.
     * @return admin details
     */
    @PostMapping("/create")
    public ResponseEntity<?> insertShop(@RequestBody ShopCommand shopCommand) {
        log.info("Admin add new shop in his list");
        AdminEntity admin = shopService.insertShop(shopCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will update the shop details.
     * @param shopCommand shop details.
     * @return updated details
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateShop(@RequestBody ShopCommand shopCommand) {
        log.info("Admin update the shop details");
        AdminEntity admin = shopService.updateShop(shopCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will delete the shop in admin shops.
     * @param shopCommand shop details
     * @return updated detail
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteShop(@RequestBody ShopCommand shopCommand) {
        log.info("Admin delete the shop in his list");
        AdminEntity admin = shopService.deleteShop(shopCommand);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * This function will return the shop by shopId.
     * @param id shop id
     * @return shop object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopsById(@PathVariable Long id){
        log.info("Admin find the shop by shop id");
        Set<ShopEntity> shops = shopService.getAllShopsByAdminId(id);
        return new ResponseEntity<>(shops,HttpStatus.OK);
    }

    /**
     * This api will insert image for specific shop
     * @param shopId shop Id
     * @param multipartFile image for shop
     * @return 200 OK status
     */
    @PostMapping("/insertImage/{shopId}")
    public ResponseEntity<String> insertShopImage(@PathVariable String shopId, @RequestParam("image") MultipartFile multipartFile) {
        log.info("Admin try to change the shop image");
        String status = shopService.insertShopImage(Long.valueOf(shopId), multipartFile);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * This function will render the shop image
     * @param shopId shop id
     * @param response http servlet response stream
     */
    @GetMapping("/image/{shopId}")
    public void renderImageFromDB(@PathVariable String shopId, HttpServletResponse response) {
        try {
            ShopEntity shopEntity = shopService.findShopById(Long.valueOf(shopId));

            byte[] byteArray = new byte[shopEntity.getShopImage().length];

            int i = 0;
            for (Byte wrappedByte : shopEntity.getShopImage()) {
                byteArray[i++] = wrappedByte; //auto unboxing
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            log.error("Image fetch error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
