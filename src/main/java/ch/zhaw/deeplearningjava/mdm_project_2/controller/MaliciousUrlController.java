package ch.zhaw.deeplearningjava.mdm_project_2.controller;

import ai.djl.MalformedModelException;
import ch.zhaw.deeplearningjava.mdm_project_2.djl.MaliciousUrlModel;
import ch.zhaw.deeplearningjava.mdm_project_2.model.MaliciousUrlForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
public class MaliciousUrlController {

    private static final Logger logger = LoggerFactory.getLogger(MaliciousUrlController.class);

    private final MaliciousUrlModel urlModel;

    @Autowired
    public MaliciousUrlController() throws MalformedModelException, IOException {
        urlModel = MaliciousUrlModel.getInstance();
        urlModel.defineModel();
        urlModel.loadModel();
    }

    @GetMapping("/")
    public String showUrlForm(Model model) {
        model.addAttribute("maliciousUrlForm", new MaliciousUrlForm());
        return "form";
    }

    @PostMapping(path = "/api/predict")
    public String predictUrl(@ModelAttribute MaliciousUrlForm maliciousUrlForm, Model tlModel) {
        String url = maliciousUrlForm.getUrl();
        var classification = urlModel.inference(url);
        System.out.printf("Classification: %s\n", classification);
        maliciousUrlForm.setMaliciousProbability(classification.getProbabilities().get(1));

        tlModel.addAttribute("contactForm", maliciousUrlForm);

        logger.info("Prediction for URL: " + url + ", Classification: " + classification);
        return "result";
    }

}
