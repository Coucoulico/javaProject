package com.model.entite;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * 
 * @author hamza
 * c'est une classe qui represente une chirurgie
 * @version 1.0
 */
public class Chirurgie implements Comparable<Chirurgie>  {
  private  int id_chirurgie;
 
  private Salle salle;
  private  Chirurgien chirurgien;
  private LocalDateTime Debut;
  private LocalDateTime Fin;
/**
 * 
 * @param id_chirurgie
 * @param salle
 * @param chirurgien
 * @param debut
 * @param fin
 * un constructeur initialisant tous les champs
 */
 public Chirurgie(int id_chirurgie, Salle salle, Chirurgien chirurgien, LocalDateTime debut, LocalDateTime fin) {
	this.id_chirurgie = id_chirurgie;
	this.salle = salle;
	this.chirurgien = chirurgien;
	this.Debut = debut;
	this.Fin = fin;
}

 /**
  * 
  * @return la duree de la chirurgie en minutes
  */
 public Duration dure() {
	 return Duration.between(Debut, Fin);
 }
 

public Chirurgie() {
	// TODO Auto-generated constructor stub
}

@Override
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	if (obj==null) return false;
	if (obj instanceof Chirurgie) {
		Chirurgie c=(Chirurgie)obj;
		return c.id_chirurgie==this.id_chirurgie;
	}
    return false;
}

@Override
public String toString() {
	return "Chirurgie [id_chirurgie=" + id_chirurgie + ", salle=" + salle + ", chirurgien=" + chirurgien + ", Date="
			+ Debut.toLocalDate() + ", debut=" + Debut + ", Fin=" + Fin + "]";
}

BiConsumer <LocalDateTime,LocalDateTime> changedate=(LocalDateTime d,LocalDateTime f)->{
	this.Debut=d;
	this.Fin=f;
};

/**
 * 
 * @param c la chirurgie avec laquelle on veut voir si il y a conflit ou pas
 * @return true si il y a conflit false sinn
 */
public boolean isThereConflit(Chirurgie c) {
	
	BiFunction<Chirurgie,Chirurgie,Boolean>test=(a,b)->{
		boolean ubiquite= a.salle.equals(b.salle);
		boolean interference = a.chirurgien.equals(b.chirurgien);
		boolean yes=ubiquite || interference;
		
		Chirurgie ant;
		Chirurgie post;
		if (a.Debut.isBefore(b.Debut)||a.Debut.equals(b.Debut)) {
			ant=a;
			post=b;
		}
		else {
			ant=b;
			post=a;
		}
		if(ant.Fin.isAfter(post.Debut)|| ant.Fin.equals(post.Debut)) {
			return yes;
		}
			return false;
	};
	
	return test.apply(this, c);	
	
}

/**
 * 
 * @param list la  liste des chirurgie qu'on veut tester
 * @return list des chirurgie qui sont en conflit avec this
 */
public ArrayList<Chirurgie> inConflit (List<Chirurgie> list ){
	ArrayList<Chirurgie> res=new ArrayList<Chirurgie>();
	list.stream()
	.forEach(e->{if(e.isThereConflit(this)&& !e.equals(this)) res.add(e); });
	return res;
}



@Override
public Chirurgie clone() {
	// TODO Auto-generated method stub
	Chirurgie c=new Chirurgie(this.id_chirurgie,this.salle,this.chirurgien,this.Debut,this.Fin);
	return c;
}

public Salle getSalle() {
	return salle;
}


public Chirurgie setSalle(Salle salle) {
	this.salle = salle;
	return this;
}
public Chirurgien getChirurgien() {
	return chirurgien;
}

public Chirurgie setChirurgien(Chirurgien chirurgien) {
	this.chirurgien = chirurgien;
	return this;
}

public LocalDateTime getDebut() {
	return Debut;
}

public void setDebut(LocalDateTime debut) {
	Debut = debut;
}

public LocalDateTime getFin() {
	return Fin;
}

public void setFin(LocalDateTime fin) {
	Fin = fin;
}



@Override
public int compareTo(Chirurgie o) {
	// TODO Auto-generated method stub
	return this.Debut.compareTo(o.Debut);
}


  
  

  




  
  
}
