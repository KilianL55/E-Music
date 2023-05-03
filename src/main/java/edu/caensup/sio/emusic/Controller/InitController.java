package edu.caensup.sio.emusic.Controller;

import com.github.javafaker.Faker;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class InitController {

    @Autowired
    IRepoEnfant repoEnfant;

    @Autowired
    IRepoResponsable repoResponsable;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/user/{responsable}/{enfant}/")
    public @ResponseBody String initAction(@PathVariable int responsable, @PathVariable int enfant) {
        for (int o = 0; o < responsable; o++) {
            Responsable resp = new Responsable();
            Faker fake = new Faker();
            resp.setEnabled(true);
            resp.setVille(fake.address().city());
            resp.setPrenom(fake.name().firstName());
            resp.setNom(fake.name().lastName());
            resp.setUsername(fake.internet().emailAddress());
            resp.setTel1(fake.phoneNumber().phoneNumber());
            resp.setTel2(fake.phoneNumber().phoneNumber());
            resp.setTel3(fake.phoneNumber().phoneNumber());
            resp.setPassword(passwordEncoder.encode("1234"));
            resp.setPayData(fake.finance().creditCard().replaceAll("[0-9]{13}","".repeat(13)));
            resp.setPayMethod(fake.finance().creditCard());
            resp.setAdresse(fake.address().fullAddress());
            resp.setAdresse2("");
            resp.setCode_postal("14123");
            System.out.println(resp.getUsername());
            repoResponsable.save(resp);
            for (int g = 0; g < enfant; g++) {
                Enfant enft = new Enfant();
                enft.setDate_naissance(String.valueOf(fake.date().birthday()));
                enft.setEmail_parent(resp.getUsername());
                enft.setEnabled(true);
                enft.setNom(fake.name().lastName());
                enft.setPrenom(fake.name().firstName());
                enft.setUsername(enft.getPrenom()+"."+enft.getNom());
                enft.setPassword(passwordEncoder.encode("1234"));
                enft.setResponsable(resp);
                repoEnfant.save(enft);
            }

        }
        return "init ok";
    }

}
