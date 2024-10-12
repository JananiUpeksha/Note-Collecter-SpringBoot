package lk.ijse.notecollectorspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1/welcome")
public class WelcomeController {
    @GetMapping
    @ResponseBody
    public String viewWelcomeScreen() {
        return "Note Collector Springboot";
    }
}

