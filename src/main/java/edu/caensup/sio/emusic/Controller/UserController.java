package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.EmailService;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import io.github.jeemv.springboot.vuejs.VueJS;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.Random;
import java.util.random.RandomGenerator;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private IRepoEnfant repoEnfant;

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
        vue.addData("responsable", new Responsable());
        return "signup";
    }

   @PostMapping("signup/register")
    public String registerAction(@ModelAttribute Responsable resp, ModelMap model){
        model.put("resp",resp);
        System.out.println(resp.getUsername());
        return "signupRecap";
   }

    @RequestMapping("sendEmailVerif")
    public String sendEmailVerif(@ModelAttribute Responsable resp, ModelMap model) throws MessagingException, UnsupportedEncodingException {
        int randomCode = (int) (Math.random()*100000);
        resp.setCode_verification(randomCode);
        resp.setEnabled(false);
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
    public RedirectView codeVerifAction(@ModelAttribute Responsable resp){
        Optional<Responsable> responsable = (Optional<Responsable>) Optional.ofNullable(repoResponsable.findByUsername(resp.getUsername()));
        if (responsable.isPresent()){
            if (resp.getCode_verification()==responsable.get().getCode_verification()){
                responsable.get().setEnabled(true);
                responsable.get().setPassword(passwordEncoder.encode(responsable.get().getPassword()));
                repoResponsable.save(responsable.get());
                return new RedirectView("accueil");
            }
        }
        return new RedirectView("signup");

    }

    @RequestMapping("dashboard")
    public String dashboardAction(ModelMap model){
        Responsable responsable = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(responsable.getNom());
        model.put("responsable", responsable);
        vue.addData("isActive", "account");
        return "dashboard/index";
    }

    @GetMapping("addChildren")
    public String addChildren(){
        return "dashboard/signupChildren";
    }

    @PostMapping("saveChildren")
    public RedirectView saveChildren(@ModelAttribute Enfant enfant){
        Responsable resp = repoResponsable.findByUsername(enfant.getEmail_parent());
        enfant.setUsername(enfant.getPrenom()+"."+enfant.getNom());
        enfant.setPassword(passwordEncoder.encode(enfant.getPassword()));
        enfant.setResponsable(resp);
        enfant.setEnabled(true);
        repoEnfant.save(enfant);
        return new RedirectView("dashboard");
    }




}
