package edu.caensup.sio.emusic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestionController {
    @GetMapping("/admin")
    public String indexAction() {
        return "index";
    }
}
