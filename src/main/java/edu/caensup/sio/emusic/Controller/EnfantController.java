package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("/enfant/")
public class EnfantController {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired
    private IRepoCour repoCour;

    @Autowired
    private IRepoEnfant repoEnfant;

    @Autowired
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @RequestMapping("dashboard")
    public String dashboardAction(ModelMap model){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Enfant enfant = (Enfant) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vue.addData("isActiveChildren", "planning");
        Optional<Enfant> realEnfant = repoEnfant.findById(enfant.getId());
        model.put("cours", realEnfant.get().getCours());
        model.put("enfant", realEnfant);
        System.out.println(enfant.getCours());
        return "/enfant/index";
    }

    @RequestMapping("addCours/{id}")
    public RedirectView addCoursAction(ModelMap model, @PathVariable int id){
        Enfant enfant = (Enfant) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Cours> cours = repoCour.findById(id);
        Enfant realEnfant = repoEnfant.findById(enfant.getId()).get();
        cours.ifPresent(c -> {
            realEnfant.getCours().add(c);
            repoEnfant.save(realEnfant);
        });
        return new RedirectView("/enfant/dashboard/cours");
    }

    @RequestMapping("removeCours/{id}")
    public RedirectView removeCoursAction(ModelMap model, @PathVariable int id){
        Enfant enfant = (Enfant) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Cours> cours = repoCour.findById(id);
        Enfant realEnfant = repoEnfant.findById(enfant.getId()).get();
        cours.ifPresent(c -> {
            realEnfant.getCours().remove(c);
            repoEnfant.save(realEnfant);
        });
        return new RedirectView("/enfant/dashboard/cours");
    }

    @RequestMapping("dashboard/cours")
    public String dashboardClassesAction(ModelMap model){
        String result=dashboardAction(model);
        vue.addData("isActive", "cours");
        return result;
    }

}