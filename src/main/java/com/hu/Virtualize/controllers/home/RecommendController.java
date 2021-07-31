package com.hu.Virtualize.controllers.home;

import com.hu.Virtualize.commands.home.RecommendCommand;
import com.hu.Virtualize.entities.RecommendEntity;
import com.hu.Virtualize.services.home.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RequestMapping("/recommend")
@RestController
@CrossOrigin("*")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    /**
     * This function will insert new recommend in list.
     * @param recommendCommand recommend details.
     * @return recommend object
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insertRecommend(@RequestBody RecommendCommand recommendCommand) {
        log.info("Insert offer to show on home recommend bar");
        RecommendEntity recommendEntity = recommendService.insertRecommend(recommendCommand);
        return new ResponseEntity<>(recommendEntity, HttpStatus.OK);
    }

    /**
     * This api will insert image for specific recommend
     * @param recommendId recommend id
     * @param multipartFile image for recommend
     * @return 200 OK status
     */
    @PostMapping("/insertImage/{recommendId}")
    public ResponseEntity<String> insertRecommendImage(@PathVariable String recommendId, @RequestParam("image") MultipartFile multipartFile) {
        log.info("Admin try to change the shop image");
        String status = recommendService.insertRecommendImage(Long.valueOf(recommendId), multipartFile);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{recommendId}")
    public ResponseEntity<?> deleteRecommend(@PathVariable String recommendId) {
        String status = recommendService.deleteRecommend(Long.valueOf(recommendId));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * This function will return all the show recommend IDs.
     * @return list of recommends
     */
    @GetMapping("/showRecommend/{recommendId}")
    public ResponseEntity<?> findRecommendById(@PathVariable String recommendId) {
        log.info("Find recommendation by id");
        RecommendEntity showRecommend = recommendService.findById(Long.parseLong(recommendId));
        return new ResponseEntity<>(showRecommend, HttpStatus.OK);
    }

    /**
     * This function will return all the show recommend IDs.
     * @return list of recommends
     */
    @GetMapping("/showRecommend")
    public ResponseEntity<?> findShowRecommendId() {
        log.info("Find all recommend bar");
        List<RecommendEntity> showRecommends = recommendService.findShowRecommends();
        return new ResponseEntity<>(showRecommends, HttpStatus.OK);
    }

    /**
     * This function will show the image for recommend.
     * @param recommendId recommend id
     * @param response http servlet response.
     */
    @GetMapping("/bar/{recommendId}")
    public void renderImageFromDB(@PathVariable String recommendId, HttpServletResponse response) {
        log.info("show all recommend info on home page");
        try {
            RecommendEntity recommendEntity = recommendService.findById(Long.valueOf(recommendId));

            byte[] byteArray = new byte[recommendEntity.getRecommendImage().length];

            int i = 0;
            for (Byte wrappedByte : recommendEntity.getRecommendImage()) {
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
