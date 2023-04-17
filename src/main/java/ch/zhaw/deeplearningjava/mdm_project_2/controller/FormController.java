package ch.zhaw.deeplearningjava.mdm_project_2.controller;

import ch.zhaw.deeplearningjava.mdm_project_2.model.ContactForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;

@RestController
public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    @RequestMapping("/")
    public ModelAndView showForm() {
        ModelAndView modelAndView = new ModelAndView("form");
        modelAndView.addObject("demoForm", new ContactForm());
        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute("demoForm") ContactForm contactForm, Model model) {
        logger.info("Name: " + contactForm.getName());
        logger.info("Email: " + contactForm.getEmail());
        logger.info("Birthdate: " + contactForm.getBirthdate());

        model.addAttribute("name", contactForm.getName());
        model.addAttribute("email", contactForm.getEmail());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(contactForm.getBirthdate());
        model.addAttribute("birthdate", formattedDate);

        return "result";
    }

}
