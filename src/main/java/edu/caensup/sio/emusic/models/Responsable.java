package edu.caensup.sio.emusic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Responsable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String prenom;

    @Column(length = 50)
    private String adresse;

    @Column(length = 50)
    private String ville;

    @Column(length = 50)
    private String ville2;

    @Column(length = 50)
    private String password;

    @Column(length = 5)
    private String code_postal;

    @Column(length = 50)
    private String email;

    @Column(length = 11)
    private int quotient_familial;

    @Column(length = 50)
    private String tel1;

    @Column(length = 50)
    private String tel2;

    @Column(length = 50)
    private String tel3;

    @Column
    private String code_verification;

    private boolean enabled;

    @OneToMany(mappedBy = "responsable")
    private List<Enfant> enfant;


}
