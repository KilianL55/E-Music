package edu.caensup.sio.emusic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GestionController {

  @RequestMapping("/403")
  public @ResponseBody String accessDeniedAction() {
    return "no access for user";
  }

  @GetMapping("/form")
  public String formAction() {
    return "/formView";
  }

  @PostMapping("/formSubmit")
  public @ResponseBody String formSubmitAction(@RequestParam("name") String name) {
    return name;
  }
}
