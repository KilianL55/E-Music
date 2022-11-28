package edu.caensup.sio.emusic.Controller;

import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController extends AbstractController {

    @Autowired
    private VueJS vue;

    @GetMapping({"", "/"})
    public String adminAction() {
        return "admin/index";
    }
}
