package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import edu.caensup.sio.emusic.service.AESEncryptionDecryption;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/parent/")
public class ResponsableController {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired
    private IRepoCour repoCour;

    @Autowired
    private IRepoEnfant repoEnfant;

    @Autowired
    private VueJS vue;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @RequestMapping("dashboard")
    public String dashboardAction(ModelMap model){
        String secretKey = "zadqsqgfgqsdqzdq";
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        Responsable parent = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Enfant> enfants = repoEnfant.findByResponsable(parent);
        Responsable realParent = repoResponsable.findById(parent.getId()).get();
        Set<Cours> cours = new HashSet<>();
        for ( Enfant enfant : enfants){
             cours.addAll(enfant.getCours());
        }
        model.put("cours", realParent.getCours());;
        model.put("coursC", cours);
        parent.setAdresse2("");
        if( realParent.getPayMethod() != null && realParent.getPayData() != null){
            realParent.setPayMethod(aesEncryptionDecryption.decrypt(realParent.getPayMethod(), secretKey));
            realParent.setPayData(aesEncryptionDecryption.decrypt(realParent.getPayData(), secretKey));
        }
        model.put("responsable", realParent);
        if(enfants.size() >= 1){
            model.put("enfants",enfants);
        }
        vue.addData("isActive", "planning");
        vue.addData("haveCours", realParent.getCours().size());
        vue.addData("active", "disable");
        return "/parent/index";

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
        return new RedirectView("./parent/dashboard/cours");
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
        return new RedirectView("./parent/dashboard/cours");
    }

    @RequestMapping("dashboard/cours")
    public String dashboardClassesAction(ModelMap model){
        String result=dashboardAction(model);
        vue.addData("isActive", "cours");
        return result;
    }

    @PostMapping("/updateResponsable")
    public RedirectView updateResponsable( @ModelAttribute Responsable responsable){
        String secretKey = "zadqsqgfgqsdqzdq";
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
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
        resp.setPayMethod(aesEncryptionDecryption.encrypt(responsable.getPayMethod(), secretKey));
        resp.setPayData(aesEncryptionDecryption.encrypt(responsable.getPayData(), secretKey));
        repoResponsable.save(resp);
        return new RedirectView("./parent/dashboard");

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
        return new RedirectView("./parent/dashboard");

    }

    @RequestMapping("removeChildren/{id}")
    public RedirectView removeChildren(@PathVariable int id){
        repoEnfant.deleteById(id);
        return new RedirectView("./dashboard");
    }

    @RequestMapping("removeAccount/{id}")
    public RedirectView removeAction(HttpServletRequest request, HttpServletResponse response, @PathVariable int id){
        HttpSession session = request.getSession();
        session.invalidate();
        session =request.getSession(false);
        repoResponsable.deleteById(id);
        return new RedirectView("/");
    }

    @RequestMapping("manageChildren/{id}")
    public String manageAction(@PathVariable int id, ModelMap modelMap){
        Optional<Enfant> enfant=repoEnfant.findById(id);
        modelMap.put("enfant",enfant.get());
        vue.addData("manage","edit");
        return "./parent/manageChildren";
    }

    @PostMapping("editChildren")
    public RedirectView editAction(@ModelAttribute Enfant enfant){
        Responsable resp = (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Enfant enfantSave = repoEnfant.findByUsernameAndResponsable(enfant.getUsername(),resp);
        enfantSave.setDate_naissance(enfant.getDate_naissance());
        enfantSave.setNom(enfant.getNom());
        enfantSave.setPrenom(enfant.getPrenom());
        repoEnfant.save(enfantSave);
        return new RedirectView("./parent/dashboard");
    }

}
