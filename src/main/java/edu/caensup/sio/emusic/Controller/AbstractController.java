package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.pojo.ActiveUser;
import io.github.jeemv.springboot.vuejs.VueJS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractController {

    @Autowired
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }

    @ModelAttribute("activeUser")
    public ActiveUser getActiveUsername() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        return new ActiveUser(auth);
    }
}
