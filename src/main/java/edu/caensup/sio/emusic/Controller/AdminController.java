package edu.caensup.sio.emusic.Controller;

import java.time.LocalDateTime;

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

import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Gestionnaire;
import edu.caensup.sio.emusic.models.Instrument;
import edu.caensup.sio.emusic.repositories.IRepoCour;
import edu.caensup.sio.emusic.repositories.IRepoGestionnaire;
import edu.caensup.sio.emusic.repositories.IRepoInstrument;
import io.github.jeemv.springboot.vuejs.VueJS;

@Controller
@RequestMapping("admin")
public class AdminController extends AbstractController {

	@Autowired
	private VueJS vue;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IRepoGestionnaire repoGestionnaire;

	@Autowired
	private IRepoInstrument repoInstrument;

	@Autowired
	private IRepoCour repoCours;

	@GetMapping({ "", "/" })
	public String adminAction(ModelMap model) {
//        Création du compte admin

//		Gestionnaire gestionnaire = new Gestionnaire();
//		gestionnaire.setUsername("admin");
//		gestionnaire.setPassword(passwordEncoder.encode("1234"));
//		repoGestionnaire.save(gestionnaire);
		Object connected = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (connected instanceof Gestionnaire) {
			model.put("instruments", repoInstrument.findAll());
			vue.addData("isActiveAdmin", "cours");
			vue.addData("active", false);
			model.put("cours", repoCours.findAll());
			return "admin/index";
		} else {
			return "login";
		}
	}

	@PostMapping("addCours")
	public RedirectView addCours(@ModelAttribute Cours cours) {
		cours.setDate(LocalDateTime.now());
		repoCours.save(cours);
		vue.addData("active", false);
		return new RedirectView("/admin");
	}

	@PostMapping("addInstrument")
	public RedirectView addInstrument(@ModelAttribute Instrument inst) {
		repoInstrument.save(inst);
		vue.addData("active", false);
		return new RedirectView("/admin");
	}

	@GetMapping("deleteInstrument/{id}")
	public RedirectView deleteInstrument(@PathVariable int id) {
		repoInstrument.deleteById(id);
		vue.addData("active", false);
		return new RedirectView("/admin");
	}

//    @GetMapping("editInstrument")
//    public String editInstrument(int id, ModelMap model) {
//        model.put("instrument", repoInstrument.findById(id).get());
//        return "admin/editInstrument";
//    }
}
