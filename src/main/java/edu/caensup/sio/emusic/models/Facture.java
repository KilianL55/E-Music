package edu.caensup.sio.emusic.models;

import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String nom;

    private String prenom;

    private String addresse;

    private int codePostale;

    private String ville;

    private int instrument;


    @ManyToOne(fetch = FetchType.EAGER)
    private Responsable responsable;

    private String description;

    private String quantite;

    private int quotientFamilial;

    private int prix;

}
