package edu.caensup.sio.emusic.Controller;

import edu.caensup.sio.emusic.pojo.ActiveUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractController {

    @ModelAttribute("activeUser")
    public ActiveUser getActiveUsername() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        return new ActiveUser(auth);
    }
}
