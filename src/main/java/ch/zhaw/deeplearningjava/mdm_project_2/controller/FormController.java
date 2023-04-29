package ch.zhaw.deeplearningjava.mdm_project_2.controller;

import ch.zhaw.deeplearningjava.mdm_project_2.model.ContactForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    @GetMapping("/contact")
    public String showForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "form";
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute ContactForm contactForm, Model model) {

        logger.info("Name: " + contactForm.getName());
        logger.info("Email: " + contactForm.getEmail());
        logger.info("Birthdate: " + contactForm.getBirthdate());

        model.addAttribute("contactForm", contactForm);

        return "result";
    }

}
