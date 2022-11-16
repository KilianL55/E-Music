package edu.caensup.sio.emusic.Controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import edu.caensup.sio.emusic.EmailService;
import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import io.github.jeemv.springboot.vuejs.VueJS;

@Controller
@RequestMapping("/")
public class UserController {


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
  public String indexAction() {
    vue.addData("isActive", "accueil");
    return "index";
  }

  @GetMapping({"classes"})
  public String classesAction(ModelMap model) {
    Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (responsable == "anonymousUser") {
      Iterable<Cours> cours = repoCour.findAll();
      model.put("cours", cours);
      vue.addData("isActive", "classes");
    } else {
      Iterable<Cours> cours = repoCour.findAll();
      model.put("cours", cours);
      vue.addData("isActive", "classes");
      vue.addData("isConnected", true);
      System.out.println("isConnected");
    }
    return "classes";
  }

  @GetMapping("signup")
  public String formAction() {
    vue.addData("state", true);
    vue.addData("isActive", "signup");
    System.out.println("signup page trouver");
    vue.addData("responsable", new Responsable());
    return "signup";
  }

  @PostMapping("signup/register")
  public String registerAction(@ModelAttribute Responsable resp, ModelMap model) {
    model.put("resp", resp);
    System.out.println(resp.getUsername());
    return "signupRecap";
  }

  @RequestMapping("sendEmailVerif")
  public String sendEmailVerif(@ModelAttribute Responsable resp, ModelMap model)
      throws MessagingException, UnsupportedEncodingException {
    int randomCode = (int) (Math.random() * 100000);
    resp.setCode_verification(randomCode);
    resp.setEnabled(false);
    repoResponsable.save(resp);
    try {
      emailService.sendEmail(resp.getUsername(), "test",
          "Voici votre code de vérification : " + randomCode);
    } catch (UnsupportedEncodingException | MessagingException e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
    model.put("resp", resp);
    return "signupVerif";
  }

  @PostMapping("codeVerif")
  public RedirectView codeVerifAction(@ModelAttribute Responsable resp) {
    Optional<Responsable> responsable = (Optional<Responsable>) Optional
        .ofNullable(repoResponsable.findByUsername(resp.getUsername()));
    if (responsable.isPresent()) {
      if (resp.getCode_verification() == responsable.get().getCode_verification()) {
        responsable.get().setEnabled(true);
        responsable.get().setPassword(passwordEncoder.encode(responsable.get().getPassword()));
        repoResponsable.save(responsable.get());
        return new RedirectView("accueil");
      }
    }
    return new RedirectView("signup");

  }

  @RequestMapping("dashboard")
  public String dashboardAction(ModelMap model) {
    Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (responsable instanceof Enfant enfant) {
      model.put("enfant", enfant);
      return "enfant/index";
    } else {

      Responsable parent = (Responsable) responsable;
      parent.setAdresse2("");
      model.put("responsable", parent);
      vue.addData("isActive", "account");
      vue.addData("active", "disable");
      return "parent/index";
    }
  }

  @RequestMapping("addCours/{id}")
  @Transactional
  public RedirectView addCoursAction(ModelMap model, @PathVariable int id) {
    System.out.println(id);
    Object responsable = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Responsable parent = (Responsable) responsable;
    Optional<Cours> cours = repoCour.findById(id);
    Responsable realParent = repoResponsable.refreshDetached(parent, parent.getId());
    cours.ifPresent(c -> {
      realParent.getCours().add(c);
      repoResponsable.save(realParent);
    });

    return new RedirectView("/dashboard/cours");
  }

  @RequestMapping("dashboard/cours")
  public String dashboardClassesAction(ModelMap model) {
    String result = dashboardAction(model);

    vue.addData("isActive", "cours");
    return result;
  }

  @PostMapping("/updateResponsable")
  public RedirectView updateResponsable(@ModelAttribute Responsable responsable) {
    Responsable resp =
        (Responsable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
  public RedirectView saveChildren(@ModelAttribute Enfant enfant) {
    Responsable resp = repoResponsable.findByUsername(enfant.getEmail_parent());
    enfant.setUsername(enfant.getPrenom() + "." + enfant.getNom());
    enfant.setPassword(passwordEncoder.encode(enfant.getPassword()));
    enfant.setResponsable(resp);
    enfant.setEnabled(true);
    vue.addData("isActive", "children");
    vue.addData("active", "disable");
    repoEnfant.save(enfant);
    return new RedirectView("dashboard");
  }

}
