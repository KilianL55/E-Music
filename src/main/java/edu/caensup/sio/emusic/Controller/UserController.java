package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.EmailService;
import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class UserController extends AbstractController {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired
    private IRepoCour repoCour;

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

    @GetMapping({"classes"})
    public String classesAction(ModelMap model){
        Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (responsable == "anonymousUser") {
            Iterable<Cours> cours = repoCour.findAll();
            model.put("cours", cours);
            vue.addData("isActive", "classes");
<<<<<<< HEAD
        } else if (responsable instanceof Responsable){
=======
            vue.addData("isConnected", false);
        } else {
>>>>>>> main
            Iterable<Cours> cours = repoCour.findAll();
            Responsable parent = (Responsable) responsable;
            Responsable realParent = repoResponsable.findById(parent.getId()).get();
            model.put("cours", cours);
            model.put("parent", realParent);
            vue.addData("isActive", "classes");
            vue.addData("isConnected", true);
            vue.addData("user","parent");
            for (Cours cour : cours) {
                vue.addData("isInscrit"+cour.getId(), cour.isInscrit(realParent));
            }
            System.out.println(realParent.getCours());
        } else if (responsable instanceof Enfant){
            Iterable<Cours> cours = repoCour.findAll();
            Enfant enfant = (Enfant) responsable;
            Enfant realEnfant = repoEnfant.findById(enfant.getId()).get();
            model.put("cours", cours);
            model.put("parent", realEnfant);
            vue.addData("isActive", "classes");
            vue.addData("isConnected", true);
            vue.addData("user","enfant");
            for (Cours cour : cours) {
                vue.addData("isInscrit"+cour.getId(), cour.isInscrit(realEnfant));
            }
        }
        return "classes";
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
    public RedirectView dashboardAction(){
        Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(responsable instanceof Enfant enfant){
            return new RedirectView("/enfant/dashboard");
        } else {
            return new RedirectView("/parent/dashboard");
        }

    }



}