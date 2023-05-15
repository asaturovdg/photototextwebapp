package ru.les.jettywebapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import ru.les.jettywebapp.models.Prediction;
import ru.les.jettywebapp.services.PhotoService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/photo")
public class PhotoController {

    PhotoService photoService;

    @GetMapping()
    public String index(HttpServletRequest request, Model model) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null && !inputFlashMap.isEmpty()) {
            model.addAttribute(inputFlashMap.get("prediction"));
        } else {
            model.addAttribute("prediction", "Тут будет результат обработки текста");
        }
        return "photo/index";
    }

    @GetMapping("/redirect")
    public String redirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("prediction", "224422");
        return "redirect:/photo";
    }

    @PostMapping("/load")
    public String savePhoto(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        Prediction prediction = photoService.handle(file);
        redirectAttributes.addFlashAttribute("prediction", prediction.getText());
        return "redirect:/photo";
    }

    @PostMapping()
    public String showButton(RedirectAttributes redirectAttributes, @ModelAttribute("file") MultipartFile file) {
        Prediction prediction = photoService.handle(file);
        redirectAttributes.addFlashAttribute("prediction", prediction.getText());
        return "redirect:/photo#download_place";
    }
}
