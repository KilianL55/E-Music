package edu.caensup.sio.emusic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class CoursMembre {

    private Responsable responsables;

    private Cours cours;

//    public static  List<CoursMembre> initFromCours(Responsable resp, List<Cours> cours) {
//
//    }

    public boolean isInscrit() {
        return responsables.getCours().contains(cours);
    }
}
