package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.EmailService;
import edu.caensup.sio.emusic.models.User;
import edu.caensup.sio.emusic.repositories.IRepoUser;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private IRepoUser repoResponsable;

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @GetMapping({"accueil", "/"})
    public String indexAction(){
        vue.addData("isActive", "accueil");
        return "index";
    }

    @GetMapping("signup")
    public String formAction(){
        vue.addData("state", true);
        vue.addData("isActive", "signup");
        System.out.println("signup page trouver");
        vue.addData("responsable", new User());
        return "signup";
    }

   @PostMapping("signup/register")
    public String registerAction(@ModelAttribute User resp, ModelMap model){
        model.put("resp",resp);
        System.out.println(resp.getUsername());
        return "signupRecap";
   }

    @RequestMapping("sendEmailVerif")
    public String sendEmailVerif(@ModelAttribute User resp, ModelMap model) throws MessagingException, UnsupportedEncodingException {
        int randomCode = (int) (Math.random()*100000);
        resp.setCode_verification(randomCode);
        resp.setEnabled(false);
        resp.setPassword(passwordEncoder.encode(resp.getPassword()));
        repoResponsable.save(resp);
        try{
            emailService.sendEmail(resp.getUsername(), "test", "Voici votre code de vérification : "+randomCode);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        model.put("resp",resp);
        return "signupVerif";
    }

    @PostMapping("codeVerif")
    public RedirectView codeVerifAction(@ModelAttribute User resp){
        Optional<User> responsable = repoResponsable.findByUsername(resp.getUsername());
        if (responsable.isPresent()){
            if (resp.getCode_verification()==responsable.get().getCode_verification()){
                responsable.get().setEnabled(true);
                responsable.get().setPassword(passwordEncoder.encode(responsable.get().getPassword()));
                responsable.get().setAuthorities("RESPONSABLE");
                repoResponsable.save(responsable.get());
                return new RedirectView("accueil");
            }
        }
        return new RedirectView("signup");

    }

}
