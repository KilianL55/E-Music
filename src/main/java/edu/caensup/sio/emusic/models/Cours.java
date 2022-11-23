package edu.caensup.sio.emusic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 50)
    private String libelle;

    @Column(length = 5)
    private int ageMin;

    @Column(length = 5)
    private int ageMax;

    @Column(length = 5)
    private int nbPlaceMax;

    @Column
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private Instrument instrument;

    public boolean isInscrit(User user) {
        return user.getCours().contains(this);
    }
}
