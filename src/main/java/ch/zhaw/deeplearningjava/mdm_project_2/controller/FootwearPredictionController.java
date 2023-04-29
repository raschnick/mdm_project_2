package ch.zhaw.deeplearningjava.mdm_project_2.controller;

import ai.djl.MalformedModelException;
import ch.zhaw.deeplearningjava.mdm_project_2.djl.Inference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FootwearPredictionController {

    private static final Logger logger = LoggerFactory.getLogger(FootwearPredictionController.class);

    private Inference inference;

    {
        try {
            inference = new Inference();
        } catch (MalformedModelException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws Exception {
        logger.info("Called /analyze endpoint with image: " + image.getOriginalFilename());
        return inference.predict(image.getBytes()).toJson();
    }

}
