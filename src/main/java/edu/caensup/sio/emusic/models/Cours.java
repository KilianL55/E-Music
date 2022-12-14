package edu.caensup.sio.emusic.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.*;

import edu.caensup.sio.emusic.Converter.DateConverter;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime date;

	@ManyToOne(fetch = FetchType.EAGER)
	private Instrument instrument;

	public boolean isInscrit(User user) {
		return user.getCours().contains(this);
	}

	public Date getDate() {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}
}
