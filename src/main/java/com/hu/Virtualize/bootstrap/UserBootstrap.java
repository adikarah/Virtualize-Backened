package com.hu.Virtualize.bootstrap;

import com.hu.Virtualize.entities.AdminEntity;
import com.hu.Virtualize.entities.DiscountEntity;
import com.hu.Virtualize.entities.ProductEntity;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.entities.ShopEntity;
import com.hu.Virtualize.entities.UserEntity;
import com.hu.Virtualize.enums.ProductEnum;
import com.hu.Virtualize.repositories.AdminRepository;
import com.hu.Virtualize.repositories.RecommendRepository;
import com.hu.Virtualize.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Component
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecommendRepository recommendRepository;

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public UserBootstrap(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Start user bootstrap class ");
        testDataBase();
    }

    public void testDataBase() {
        // add user1 in database
        UserEntity user1 = new UserEntity("Zhatab","zhatabsaifi1@gmail.com",passwordEncoder.encode("123"),null);
        UserEntity user2 = new UserEntity("Praveen", "p@gmail.com",passwordEncoder.encode("123"),null);
        UserEntity user3 = new UserEntity("Bikash", "b@gmail.com",passwordEncoder.encode("123"),null);
        UserEntity user4 = new UserEntity("Shahansk", "s@gmail.com",passwordEncoder.encode("123"),null);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        user4 = userRepository.save(user4);

//        ------------------------------------------------------------------------------------------------------------

        ShopEntity clothShop1 = new ShopEntity("Peter England", "Delhi","This is peter england shop","07AAAAA0000A1ZA");
        clothShop1.setShopImage(getImage("src/main/resources/static/images/PeterEngland.png"));

        ShopEntity clothShop2 = new ShopEntity("Raymond", "UTTAR PRADESH","This is Raymond shop","09AAAAA0001A1ZC");
        clothShop2.setShopImage(getImage("src/main/resources/static/images/RaymondShop.png"));

        ShopEntity clothShop3 = new ShopEntity("Allen Solly", "UTTAR PRADESH","This is Allen Solly shop","09AAAAA1001A1ZD");
        clothShop3.setShopImage(getImage("src/main/resources/static/images/LMShop.png"));

        // create cloth products
        ProductEntity cloth1 = new ProductEntity("Shirt", 1200,null, ProductEnum.CLOTHES.toString(),"Male","This is an cotton shirt.");
        cloth1.setProductImage(getImage("src/main/resources/static/images/shirt1.jfif"));
        cloth1.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Diwali",5,"2021-07-21"," Get exciting cash back offers and extra discounts on cotton shirt"),new DiscountEntity("Holi",15,"2021-07-22"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands"))));

        ProductEntity cloth2 = new ProductEntity("Jeans", 1500,null, ProductEnum.CLOTHES.toString(),"Female","This is an denim jeans");
        cloth2.setProductImage(getImage("src/main/resources/static/images/jeans1.jfif"));
        cloth2.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Eid",50,"2021-07-23"," Get exciting cash back offers on this product only on this eid festival"),new DiscountEntity("Special",15,"2021-07-24"," Get exciting cash back offers on this jeans only for you"))));

        ProductEntity cloth3 = new ProductEntity("Tie", 1600,null, ProductEnum.CLOTHES.toString(),"Unisex","This is an woolen tie");
        cloth3.setProductImage(getImage("src/main/resources/static/images/tie1.jfif"));
        cloth3.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Diwali",25,"2021-07-26"," Get exciting cash back offers  on this product only on this diwali festival"),new DiscountEntity("Holi",15,"2021-07-25"," Get exciting cash back offers  on this product only on this holi festival"))));

        ProductEntity cloth4 = new ProductEntity("T-shirt", 2000,null, ProductEnum.CLOTHES.toString(),"Unisex","This is an amazing product");
        cloth4.setProductImage(getImage("src/main/resources/static/images/tshirt.jfif"));
        cloth4.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Diwali",30,"2021-07-25"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands"),new DiscountEntity("Holi",15,"2021-07-25"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands"))));

        ProductEntity cloth5 = new ProductEntity("Shirt", 500,null, ProductEnum.CLOTHES.toString(),"Female","This is an amazing product");
        cloth5.setProductImage(getImage("src/main/resources/static/images/shirt2.jfif"));

        ProductEntity cloth6 = new ProductEntity("Jeans", 900,null, ProductEnum.CLOTHES.toString(),"Female","This is an woolen jeans.");
        cloth6.setProductImage(getImage("src/main/resources/static/images/jeans2.jfif"));
        cloth6.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Diwali",70,"2021-07-25"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands"),new DiscountEntity("Holi",15,"2021-07-25"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands"))));

        ProductEntity cloth7 = new ProductEntity("Tie", 10000,null, ProductEnum.CLOTHES.toString(),"Unisex","This is an cotton tie.");
        cloth7.setProductImage(getImage("src/main/resources/static/images/tie3.jfif"));

        ProductEntity cloth8 = new ProductEntity("T-shirt", 3000,null, ProductEnum.CLOTHES.toString(),"Male","This is an amazing t-shirt");
        cloth8.setProductImage(getImage("src/main/resources/static/images/tshirt2.jfif"));

        cloth1.setBrandName(clothShop1.getShopName());
        cloth2.setBrandName(clothShop1.getShopName());
        cloth3.setBrandName(clothShop1.getShopName());
        clothShop1.setShopProducts(new HashSet<>(Arrays.asList(cloth1,cloth2,cloth3)));

        cloth4.setBrandName(clothShop2.getShopName());
        cloth5.setBrandName(clothShop2.getShopName());
        cloth6.setBrandName(clothShop2.getShopName());
        clothShop2.setShopProducts(new HashSet<>(Arrays.asList(cloth4,cloth5,cloth6)));

        cloth7.setBrandName(clothShop3.getShopName());
        cloth8.setBrandName(clothShop3.getShopName());
        clothShop3.setShopProducts(new HashSet<>(Arrays.asList(cloth7,cloth8)));

//       Complete cloth shops -------------------------------------------------------------------------------

        // MEDICINES shop -------------------------------------------------------------------------------------------------

        ShopEntity medicineShop1 = new ShopEntity("Aurobindo Pharma", "BIHAR","This is Aurobindo Pharma shop","10IAAAA1001A1ZB");
        medicineShop1.setShopImage(getImage("src/main/resources/static/images/medicine.jpg"));

        ShopEntity medicineShop2 = new ShopEntity("Pharmaceutical Pharma", "GUJARAT","This is Pharmaceutical Pharma shop","24AAACC1206D1ZM");
        medicineShop2.setShopImage(getImage("src/main/resources/static/images/MedicalShop.png"));

        ShopEntity medicineShop3 = new ShopEntity("ManKind Pharma", "KARNATAKA","This is ManKind Pharma shop","29AAACC1206D2ZB");
        medicineShop3.setShopImage(getImage("src/main/resources/static/images/MedicalShop.png"));

        ProductEntity medicine1 = new ProductEntity("Paracetamol",100,medicineShop1.getShopName(),ProductEnum.MEDICINES.toString(),null,null);
        medicine1.setProductImage(getImage("src/main/resources/static/images/medicine1.jfif"));
        medicine1.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Covid",70,"2021-07-25","In this covid situation we are giving you a 20% discount on this product."),new DiscountEntity("White fungus",15,"2021-07-25"," Those who have facing white fungus problem. we will provide them 70% discount on this product."))));

        ProductEntity medicine2 = new ProductEntity("Penicillin",1000,medicineShop1.getShopName(),ProductEnum.MEDICINES.toString(),null,null);
        medicine2.setProductImage(getImage("src/main/resources/static/images/medicine2.jfif"));
        medicine2.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Covid",30,"2021-07-25"," In this covid situation we are giving you a 20% discount on this product."),new DiscountEntity("Black fungus",60,"2021-07-25","  Those who have facing black fungus problem. we will provide them 70% discount on this product."))));

        ProductEntity medicine3 = new ProductEntity("Insulin",500,medicineShop2.getShopName(),ProductEnum.MEDICINES.toString(),null,null);
        medicine3.setProductImage(getImage("src/main/resources/static/images/medicine3.jfif"));
        medicine3.setProductDiscounts(new HashSet<>(Arrays.asList(new DiscountEntity("Covid",20,"2021-07-25","In this covid situation we are giving you a 20% discount on this product."),new DiscountEntity("Yellow fungus",70,"2021-07-25"," Those who have facing yellow fungus problem. we will provide them 70% discount on this product."))));

        ProductEntity medicine4 = new ProductEntity("Smallpox",50,medicineShop3.getShopName(),ProductEnum.MEDICINES.toString(),null,null);
        medicine4.setProductImage(getImage("src/main/resources/static/images/medicine4.jfif"));


        medicineShop1.setShopProducts(new HashSet<>(Arrays.asList(medicine1,medicine2)));
        medicineShop2.setShopProducts(new HashSet<>(Collections.singletonList(medicine3)));
        medicineShop3.setShopProducts(new HashSet<>(Collections.singletonList(medicine4)));

//        complete MEDICINES shops ---------------------------------------------------------------

        // RESTAURANTS shop ------------------------------------------------------------------
        ShopEntity restaurant1 = new ShopEntity("PizzaHut", "DELHI","This is PizzaHut shop","07AAACC1206D1ZI");
        restaurant1.setShopImage(getImage("src/main/resources/static/images/restaurant.jpg"));

        ProductEntity food1 = new ProductEntity("Pizza",500,restaurant1.getShopName(),ProductEnum.RESTAURANTS.toString(),null,"This is a pizza hut pizza with extra chess.");
        food1.setProductImage(getImage("src/main/resources/static/images/pizza1.jfif"));
        food1.setProductDiscounts(new HashSet<>(Collections.singletonList(new DiscountEntity("First time user", 10, "2021-07-25"," This is a first time order offer for you. Order your favorite food and get up to 10% extra discount."))));

        ProductEntity food2 = new ProductEntity("Casey",1500,restaurant1.getShopName(),ProductEnum.RESTAURANTS.toString(),null,"This is a casey bread with 88% energy and 0% fat.");
        food2.setProductImage(getImage("src/main/resources/static/images/food1.jfif"));

        ProductEntity food3 = new ProductEntity("Bread",800,restaurant1.getShopName(),ProductEnum.RESTAURANTS.toString(),null,"This is a fresh chess bread with 100% fat free and 65% energy.");
        food3.setProductImage(getImage("src/main/resources/static/images/food2.jfif"));

        restaurant1.setShopProducts(new HashSet<>(Arrays.asList(food1,food2,food3)));

        // complete RESTAURANTS items -----------------------------------------------------------------

//        Admin work-----------------------------------------------------------------
        AdminEntity admin1 = new AdminEntity("zsaifi","zsaifi@deloitte.com",passwordEncoder.encode("123"));
        AdminEntity admin2 = new AdminEntity("praveen","pravtripathi@deloitte.com",passwordEncoder.encode("123"));
        AdminEntity admin3 = new AdminEntity("Shashank","ssrivastavakumar@deloitte.com",passwordEncoder.encode("123"));
        AdminEntity admin4 = new AdminEntity("Bikash","bkushwaha@deloitte.com",passwordEncoder.encode("123"));

        admin1.setAdminShops(new HashSet<>(Arrays.asList(clothShop1,medicineShop1,restaurant1)));
        admin2.setAdminShops(new HashSet<>(Arrays.asList(clothShop2,medicineShop2)));
        admin3.setAdminShops(new HashSet<>(Arrays.asList(clothShop3,medicineShop3)));


        admin1 = adminRepository.save(admin1);
        admin2 = adminRepository.save(admin2);
        admin3 = adminRepository.save(admin3);

        addRecommendation();
        log.info("Data load successfully");
    }

    void addRecommendation() {
        RecommendEntity recommend1 = new RecommendEntity(ProductEnum.CLOTHES.toString(), "2021-07-08", " Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands");
        recommend1.setRecommendImage(getImage("src/main/resources/static/images/clothes_offer1.jpg"));
        recommendRepository.save(recommend1);

        RecommendEntity recommend2 = new RecommendEntity(ProductEnum.MEDICINES.toString(), "2021-07-09"," Get exciting cash back offers and extra discounts on medicine products.");
        recommend2.setRecommendImage(getImage("src/main/resources/static/images/medicine_offer1.png"));
        recommendRepository.save(recommend2);

        RecommendEntity recommend3 = new RecommendEntity(ProductEnum.RESTAURANTS.toString(), "2021-07-10"," Get exciting cash back offers and extra discounts on tasty meals.");
        recommend3.setRecommendImage(getImage("src/main/resources/static/images/RESTAURANT_offer1.jpg"));
        recommendRepository.save(recommend3);

        RecommendEntity recommend4 = new RecommendEntity(ProductEnum.CLOTHES.toString(), "2021-07-11", " Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands");
        recommend4.setRecommendImage(getImage("src/main/resources/static/images/c1.jfif"));
        recommendRepository.save(recommend4);

        RecommendEntity recommend5 = new RecommendEntity(ProductEnum.CLOTHES.toString(), "2021-07-12"," Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands");
        recommend5.setRecommendImage(getImage("src/main/resources/static/images/recomm1.jfif"));
        recommendRepository.save(recommend5);

        RecommendEntity recommend6 = new RecommendEntity(ProductEnum.RESTAURANTS.toString(), "2021-07-13", " Get exciting cash back offers and extra discounts on tasty meal.");
        recommend6.setRecommendImage(getImage("src/main/resources/static/images/RESTAURANT1.jpg"));
        recommendRepository.save(recommend6);

        RecommendEntity recommend7 = new RecommendEntity(ProductEnum.RESTAURANTS.toString(), "2021-07-14", " Get exciting cash back offers and extra discounts on tasty meal.");
        recommend7.setRecommendImage(getImage("src/main/resources/static/images/RESTAURANT2.jpg"));
        recommendRepository.save(recommend7);

        RecommendEntity recommend8 = new RecommendEntity(ProductEnum.MEDICINES.toString(), "2021-07-15", " Get exciting cash back offers and extra discounts in this covid period.");
        recommend8.setRecommendImage(getImage("src/main/resources/static/images/medicine1.jpg"));
        recommendRepository.save(recommend8);

        RecommendEntity recommend9 = new RecommendEntity(ProductEnum.MEDICINES.toString(), "2021-07-16", " Get exciting cash back offers and extra discounts in this covid period.");
        recommend9.setRecommendImage(getImage("src/main/resources/static/images/medicine2.jpg"));
        recommendRepository.save(recommend9);

        RecommendEntity recommend10 = new RecommendEntity(ProductEnum.CLOTHES.toString(), "2021-07-17", " Get exciting cash back offers and extra discounts on Men and Women Fashion clothing brands");
        recommend10.setRecommendImage(getImage("src/main/resources/static/images/recomm2.jfif"));
        recommendRepository.save(recommend10);

        RecommendEntity recommend11 = new RecommendEntity(ProductEnum.RESTAURANTS.toString(), "2021-07-18", " Get exciting cash back offers and extra discounts on tasty meal.");
        recommend11.setRecommendImage(getImage("src/main/resources/static/images/RESTAURANT4.jpg"));
        recommendRepository.save(recommend11);

        RecommendEntity recommend12 = new RecommendEntity(ProductEnum.RESTAURANTS.toString(), "2021-07-19", " Get exciting cash back offers and extra discounts on tasty meal.");
        recommend12.setRecommendImage(getImage("src/main/resources/static/images/RESTAURANT3.jpg"));
        recommendRepository.save(recommend12);
    }

    /**
     * This function will convert the image into byte format.
     * @param imageUrl image url
     * @return byte array
     */
    Byte[] getImage(String imageUrl) {
        try{
            // get the image in resources folder
            BufferedImage bImage = ImageIO.read(new File(imageUrl));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);

            // convert into byte array
            byte[] byteData = bos.toByteArray();

            // convert  byte[] into Byte[]
            Byte[] bytesImage = new Byte[byteData.length];

            int i = 0;
            for (byte data : byteData) {
                bytesImage[i++] = data; //Autoboxing
            }
            return bytesImage;
        } catch (Exception e) {

        }
        return null;
    }

}
