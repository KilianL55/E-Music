package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.models.Gestionnaire;
import edu.caensup.sio.emusic.repositories.IRepoGestionnaire;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController extends AbstractController {

    @Autowired
    private VueJS vue;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRepoGestionnaire repoGestionnaire;

    @GetMapping({"", "/"})
    public String adminAction() {
//        Création du compte admin

//        Gestionnaire gestionnaire = new Gestionnaire();
//        gestionnaire.setUsername("admin");
//        gestionnaire.setPassword(passwordEncoder.encode("1234"));
//        repoGestionnaire.save(gestionnaire);
        Object connected = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (connected instanceof Gestionnaire) {
            return "admin/index";
        } else {
            return "login";
        }
    }
}
