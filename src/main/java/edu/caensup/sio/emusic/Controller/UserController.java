package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.mail.EmailServiceImpl;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired EmailServiceImpl email;

    @GetMapping("accueil")
    public String indexAction(){
        return "index";
    }

    @GetMapping("/signup")
    public String formAction(){
        return "signup";
    }

   @PostMapping("/signup/register")
    public String registerAction(@ModelAttribute Responsable resp, ModelMap model){
        model.put("resp",resp);
        email.sendSimpleMessage("kilian.levasseur@sts-sio-caen.info","Test","Test");
        return "signupRecap";
   }

}
