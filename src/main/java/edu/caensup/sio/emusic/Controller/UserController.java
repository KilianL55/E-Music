package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.EmailService;
import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Facture;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoFacture;
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

    @Autowired
    private IRepoFacture repoFacture;

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
            vue.addData("isConnected", false);
        } else {
            Iterable<Cours> cours = repoCour.findAll();
            Responsable parent = (Responsable) responsable;
            Responsable realParent = repoResponsable.findById(parent.getId()).get();
            model.put("cours", cours);
            model.put("parent", realParent);
            vue.addData("isActive", "classes");
            vue.addData("isConnected", true);
            for (Cours cour : cours) {
                vue.addData("isInscrit"+cour.getId(), cour.isInscrit(realParent));
            }
            System.out.println(realParent.getCours());
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
    public String dashboardAction(ModelMap model){
        Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(responsable instanceof Enfant enfant){
            model.put("enfant",enfant);
            return "enfant/index";
        } else {
            Responsable parent = (Responsable) responsable;
            List<Enfant> enfants = repoEnfant.findByResponsable(parent);
            Responsable realParent = repoResponsable.findById(parent.getId()).get();
            model.put("cours", realParent.getCours());
            parent.setAdresse2("");
            model.put("responsable", realParent);
            if(enfants.size() >= 1){
                model.put("enfants",enfants);
            }
            vue.addData("isActive", "planning");
            vue.addData("activeBilling", "disable");
            vue.addData("haveCours", realParent.getCours().size());
            vue.addData("active", "disable");
            return "/parent/index";
        }

    }

    @RequestMapping("addCours/{id}")
    public RedirectView addCoursAction(ModelMap model, @PathVariable int id){
        Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Responsable parent = (Responsable) responsable;
        Optional<Cours> cours = repoCour.findById(id);
        Responsable realParent = repoResponsable.findById(parent.getId()).get();
        cours.ifPresent(c -> {
            realParent.getCours().add(c);
            repoResponsable.save(realParent);
        });
        return new RedirectView("/dashboard/cours");
    }

    @RequestMapping("removeCours/{id}")
    public RedirectView removeCoursAction(ModelMap model, @PathVariable int id){
        Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Responsable parent = (Responsable) responsable;
        Optional<Cours> cours = repoCour.findById(id);
        Responsable realParent = repoResponsable.findById(parent.getId()).get();
        cours.ifPresent(c -> {
            realParent.getCours().remove(c);
            repoResponsable.save(realParent);
        });
        return new RedirectView("/dashboard/cours");
    }

    @RequestMapping("dashboard/cours")
    public String dashboardClassesAction(ModelMap model){
        String result=dashboardAction(model);
        vue.addData("isActive", "cours");
        return result;
    }

    @PostMapping("/updateResponsable")
    public RedirectView updateResponsable( @ModelAttribute Responsable responsable){
        Responsable resp = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resp.setNom(responsable.getNom());
        resp.setPrenom(responsable.getPrenom());
        resp.setUsername(responsable.getUsername());
        resp.setAdresse(responsable.getAdresse());
        resp.setAdresse2(responsable.getAdresse2());
        resp.setVille(responsable.getVille());
        resp.setCode_postal(responsable.getCode_postal());
        resp.setQuotient_familial(responsable.getQuotient_familial());
        resp.setTel1(responsable.getTel1());
        resp.setTel2(responsable.getTel2());
        resp.setTel3(responsable.getTel3());
        repoResponsable.save(resp);
        return new RedirectView("dashboard");

    }

    @PostMapping("saveChildren")
    public RedirectView saveChildren(@ModelAttribute Enfant enfant){
        Responsable resp = repoResponsable.findByUsername(enfant.getEmail_parent());
        enfant.setUsername(enfant.getPrenom()+"."+enfant.getNom());
        enfant.setPassword(passwordEncoder.encode(enfant.getPassword()));
        enfant.setResponsable(resp);
        enfant.setEnabled(true);
        vue.addData("isActive","children");
        vue.addData("active","disable");
        repoEnfant.save(enfant);
        return new RedirectView("dashboard");

    }

    @RequestMapping("removeChildren/{id}")
    public RedirectView removeChildren(@PathVariable int id){
        repoEnfant.deleteById(id);
        return new RedirectView("dashboard");
    }

    @GetMapping("removeAccount")
    public RedirectView removeAction(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for(int i = 0; i< cookies.length ; ++i){
            if(cookies[i].getName().equals("JSESSIONID")){
                cookies[i].setMaxAge(0);
                response.addCookie(cookies[i]);
                break;
            }
        }
        Responsable resp = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repoResponsable.deleteById(resp.getId());
        return new RedirectView("/");
    }

    @RequestMapping("manageChildren/{id}")
    public String manageAction(@PathVariable int id, ModelMap modelMap){
        Optional<Enfant> enfant=repoEnfant.findById(id);
        modelMap.put("enfant",enfant.get());
        vue.addData("manage","edit");
        return "/parent/manageChildren";
    }

    @PostMapping("editChildren")
    public RedirectView editAction(@ModelAttribute Enfant enfant){
        Responsable resp = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Enfant enfantSave = repoEnfant.findByUsernameAndResponsable(enfant.getUsername(),resp);
        enfantSave.setDate_naissance(enfant.getDate_naissance());
        enfantSave.setNom(enfant.getNom());
        enfantSave.setPrenom(enfant.getPrenom());
        repoEnfant.save(enfantSave);
        return new RedirectView("dashboard");
    }

    @RequestMapping("viewFacture/{id}")
    public String viewFacture(@PathVariable int id, ModelMap modelMap){
        Optional<Facture> facture=repoFacture.findById(id);
        String[] desciption = facture.get().getDescription().split(",");
        String[] quantite = facture.get().getQuantite().split(",");
        modelMap.put("facture",facture.get());
        modelMap.put("desc",desciption);
        modelMap.put("quant",quantite);
        System.out.println();
        System.out.println();
        return "/parent/facture";
    }

}