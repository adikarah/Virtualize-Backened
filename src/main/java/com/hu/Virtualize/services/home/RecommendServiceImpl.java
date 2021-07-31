package com.hu.Virtualize.services.home;

import com.hu.Virtualize.commands.home.RecommendCommand;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.repositories.RecommendRepository;
import com.hu.Virtualize.services.home.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;

    public RecommendServiceImpl(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    /**
     * This function will add the recommend in database.
     * @param recommendCommand recommend details
     * @return recommend entity object
     */
    public RecommendEntity insertRecommend(RecommendCommand recommendCommand) {
        RecommendEntity recommendEntity = new RecommendEntity();

        // convert command into entity
        if(recommendCommand.getCategoryType() != null) {
            recommendEntity.setCategoryType(recommendCommand.getCategoryType());
        }
        if(recommendCommand.getDescription() != null) {
            recommendEntity.setDescription(recommendCommand.getDescription());
        }
        if(recommendCommand.getEndDate() != null) {
            recommendEntity.setEndDate(Date.valueOf(recommendCommand.getEndDate()));
        }

        recommendEntity = recommendRepository.save(recommendEntity);
        return recommendEntity;
    }

    /**
     * This function will insert the recommend image in database.
     * @param recommendId recommend id.
     * @param multipartFile recommend image
     * @return status
     */
    public String insertRecommendImage(Long recommendId, MultipartFile multipartFile) {
        Optional<RecommendEntity> recommendEntityOptional = recommendRepository.findById(recommendId);

        if(recommendEntityOptional.isEmpty()) {
            log.error("Invalid recommend id");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid recommend id");
        }
        RecommendEntity recommendEntity = recommendEntityOptional.get();

        // convert MultipartFile into byte array and store in product entity
        Byte[] byteObjects;
        try{
            byteObjects = new Byte[multipartFile.getBytes().length];

            // copy the file data into byte array
            int i = 0;
            for (byte b : multipartFile.getBytes()){
                byteObjects[i++] = b;
            }

            //update recommend image
            recommendEntity.setRecommendImage(byteObjects);
            recommendEntity = recommendRepository.save(recommendEntity);

            log.info("Update recommend image successfully");
            return "Update recommend image successfully";
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, e.getMessage());
        }
    }

    /**
     * This function will delete the recommend entity.
     * @param recommendId recommend id.
     * @return status
     */
    public String deleteRecommend(Long recommendId) {
        recommendRepository.deleteById(recommendId);
        return "Delete recommend successfully";
    }

    /**
     * This will return all recommend object.
     * @param recommendId recommend id.
     * @return  recommend object
     */
    public RecommendEntity findById(Long recommendId) {
        Optional<RecommendEntity> recommendEntityOptional = recommendRepository.findById(recommendId);

        // if productId isn't valid
        if(recommendEntityOptional.isEmpty()) {
            log.error("Invalid recommend bar");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid recommend");
        }
        return recommendEntityOptional.get();
    }

    /**
     * This function will return all show recommend object id.
     * It also delete the expire recommend bar.
     * @return list of recommend id.
     */
    public List<RecommendEntity> findShowRecommends() {
        List<RecommendEntity> recommendEntities = recommendRepository.findAll();

        long millis = System.currentTimeMillis();
        java.sql.Date todayDate = new java.sql.Date(millis);

        // delete the expire recommend.
        for(RecommendEntity recommend : recommendEntities) {
            // if recommend image is expired, then that will be deleted.
            if(recommend.getEndDate().compareTo(todayDate) < 0) {
                recommendRepository.deleteById(recommend.getRecommendId());
            }
        }

        // again find all remain recommend items.
        recommendEntities = recommendRepository.findAll();

        // sorting according to the date
        recommendEntities.sort(Comparator.comparing(RecommendEntity::getEndDate));

        return recommendEntities;
    }
}
