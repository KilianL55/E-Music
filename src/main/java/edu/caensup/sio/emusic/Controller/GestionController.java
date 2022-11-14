package edu.caensup.sio.emusic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GestionController {
  @GetMapping("/admin")
  public String indexAction() {
    return "index";
  }

  @RequestMapping("/403")
  public @ResponseBody String accessDeniedAction() {
    return "no access for user";
  }
}
