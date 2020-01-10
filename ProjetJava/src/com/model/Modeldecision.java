package com.model;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.model.entite.Chirurgie;
import com.model.entite.Chirurgien;
import com.model.entite.Salle;

/**
 * une classe representant des methode utile pour faire le choix de modification 
 * et de resolution de conflits
 * @author baa hamza
 * @version 1.0
 */
public class Modeldecision {

	/**
	 * le model de donné associé
	 */
	private final DataModel model;
	/**
	 * list des chirurgies valides
	 */
	private static List<Chirurgie>mesChirurgies;
	/**
	 * classification des chirurgie par chirurgien,salle et jour de la semaine
	 */
	private static Map<Chirurgien,Map<Salle,Map<DayOfWeek,List<Chirurgie>>>>entrepot;
	
   
   public Modeldecision(DataModel model) {
		super();
		entrepot=new HashMap<Chirurgien,Map<Salle,Map<DayOfWeek,List<Chirurgie>>>>();
		this.model = model;
		mesChirurgies = model.getValideChirurgies();
		this.init();
	}

   /**
    * 
    * @param c une chirurgie
    * ajouter la chirurgie c à mon model à la liste des chirurgie valides et l entrepot
    */
public void add(Chirurgie c) {
	Modeldecision.entrepot.get(c.getChirurgien())
	.putIfAbsent(c.getSalle(), new HashMap<DayOfWeek,List<Chirurgie>>());
	Modeldecision.entrepot.get(c.getChirurgien()).get(c.getSalle())
	.putIfAbsent(c.getDebut().getDayOfWeek(), new ArrayList<Chirurgie>());
	Modeldecision.entrepot.get(c.getChirurgien())
	.get(c.getSalle())
	   .get(c.getDebut().getDayOfWeek()).add(c);
}
/**
 * pour initialiser l entrepot
 */
   private void init() {
	   Arrays.stream(Chirurgien.values()).forEach(c->{
		   entrepot.put(c, new HashMap<Salle,Map<DayOfWeek,List<Chirurgie>>>());
		   
		   DataModel.salleOf(mesChirurgies, c).stream().forEach(s->{
			   
			   entrepot.get(c).put(s, new HashMap<DayOfWeek,List<Chirurgie>>());
			   
			   DataModel.dayOf(mesChirurgies,c,s).forEach(d->{
				    entrepot.get(c).get(s).put(d, DataModel.allOf(mesChirurgies,c, s, d));
				   
			   });
		   });
	   });
   }
   
   
   // chirurgien
   /**
    * 
    * @param c une chirurgie
    * @return la list des chirurgie de ce chiruriegn
    */
   public List<Chirurgie>allOf(Chirurgien c){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	   entrepot.get(c).keySet().stream().forEach(s->{
		   entrepot.get(c).get(s).keySet().forEach(d->{
			   list.addAll(entrepot.get(c).get(s).get(d));
		   });
	   });
	   Collections.sort(list);
	   return list;
   }
   
   /**
    * 
    * @param c un chirurgien
    * @return l'ensemble des salle ou travaille ce chirurgien
    */
   public Set<Salle> salleOf(Chirurgien c){
	   return entrepot.get(c).keySet();
   }
   //une salle
   /**
    * 
    * @param s une salle
    * @return l'ensemble des chirurgie faites dans cette salle
    */
   public List<Chirurgie>allOf(Salle s){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	   entrepot.keySet().stream().forEach(c->{
		   if(entrepot.get(c).containsKey(s)) {
			   entrepot.get(c).get(s).keySet().forEach(d->{
				   list.addAll(entrepot.get(c).get(s).get(d));
			   });
		   }
	   });
	   Collections.sort(list);
	   return list;
   }
   /**
    * 
    * @param s
    * @return l'ensemble des chirugiens qui ont deja travaillé dans cette salle
    */
   public Set<Chirurgien> chirurgienOf(Salle s){
	   return entrepot.keySet().stream()
			   .filter(c->entrepot.get(c).containsKey(s)).collect(Collectors.toSet());
   }
   //chirurgien salle
   /**
    * 
    * @param c chirurgien
    * @param salle une salle donnée
    * @return la list des chirurgie faites par le chirurgien c dans la salle s
    */
   public List<Chirurgie>allOf(Chirurgien c,Salle salle){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	   if(entrepot.get(c).containsKey(salle))
	       entrepot.get(c).get(salle).keySet().stream().forEach(d->{
		       list.addAll(entrepot.get(c).get(salle).get(d));
		       });
	   
	   Collections.sort(list);
	   return list;
   }
   //chirurgien salle jours
   /**
    * 
    * @param c chirurgien
    * @param salle une salle donnée
    * @param day un jour de la semaine
    * 
    * @return la list des chirurgie faites par le chirurgien c dans la salle s le jour de la semaine
    * 
    * 
    */
   
   public List<Chirurgie>allOf(Chirurgien c,Salle salle,DayOfWeek day){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	   if(entrepot.get(c).containsKey(salle))
	       if (entrepot.get(c).get(salle).containsKey(day))
	    	   list.addAll(entrepot.get(c).get(salle).get(day)); 
	   Collections.sort(list);
	   return list;
   }
   
   //salle jour
   /**
    * 
    * @param salle
    * @param day
    * @return list des chirurgie faites dans la salle ,le jours
    */
   public List<Chirurgie>allOf(Salle salle,DayOfWeek day){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	     entrepot.keySet().forEach(c->{
	    	 if (entrepot.get(c).containsKey(salle)) {
	    		 if(entrepot.get(c).get(salle).containsKey(day)) {
	    			 list.addAll(entrepot.get(c).get(salle).get(day));
	    		 }
	    	 }
	     });
	   Collections.sort(list);
	   return list;
   }

   //chirurgien jour
   /**
    * 
    * @param c un chirurgien
    * @param day
    * @return list des chirurgie faites par ce chirurgien le jour
    */
   public List<Chirurgie>allOf(Chirurgien c,DayOfWeek day){
	   List<Chirurgie>list=new ArrayList<Chirurgie>();
	        entrepot.get(c).keySet().stream().forEach(salle->{
	        	if(entrepot.get(c).get(salle).containsKey(day)) 
	        		list.addAll(entrepot.get(c).get(salle).get(day));
	        });
	   Collections.sort(list);
	   return list;
   }

    public List<Chirurgie> getMesChirurgies() {
		return mesChirurgies;
	}

	public void setMesChirurgies(List<Chirurgie> mesChirurgies) {
		Modeldecision.mesChirurgies = mesChirurgies;
	}

	public Map<Chirurgien, Map<Salle, Map<DayOfWeek, List<Chirurgie>>>> getEntrepot() {
		return entrepot;
	}

	public static void setEntrepot(Map<Chirurgien, Map<Salle, Map<DayOfWeek, List<Chirurgie>>>> entrepot) {
		Modeldecision.entrepot = entrepot;
	}

	
	
	
}
