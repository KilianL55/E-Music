package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private VueJS vue;

    @Autowired
    private IRepoResponsable repoResponsable;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @GetMapping("accueil")
    public String indexAction(){
        return "index";
    }

    @GetMapping("/signup")
    public String formAction(){
        vue.addData("responsable", new Responsable());
        return "signup";
    }

   @PostMapping("/signup/register")
    public String registerAction(@ModelAttribute Responsable resp, ModelMap model){
        model.put("resp",resp);
        return "signupRecap";
   }
}
