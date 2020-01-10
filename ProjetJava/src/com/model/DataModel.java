package com.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.model.entite.Chirurgie;
import com.model.entite.Chirurgien;
import com.model.entite.Salle;

public class DataModel {
	
	/**
	 * l'ensemble de toutes les chirurgie
	 */
     private List<Chirurgie>myChirurgies;
     
     /**
      * list des chirurgies qui ne sont en conflit avec aucune autre chirurgie
      */
     private List<Chirurgie>valideChirurgies;
     /**
      * une map qui associe pour toute chirurgie la list des chirurgies avec lesquelles est en conflit
      */
     private Map<Chirurgie, ArrayList<Chirurgie>> conflitsChirurgies;
	
     public DataModel(List<Chirurgie> myChirurgies) {
		super();
		this.myChirurgies = myChirurgies;
		this.valideChirurgies=new ArrayList<Chirurgie>();
		this.conflitsChirurgies=new HashMap<Chirurgie,ArrayList<Chirurgie>>();
		this.init();
	}
	/**
	 * pour separer les chirurgie valide et celles qui sont en conflit
	 */
     public void init() {
    	 myChirurgies.stream().forEach(c->{
    		 ArrayList<Chirurgie> list=c.inConflit(myChirurgies);
    		 if(list.isEmpty()) {
    			 
    			 this.valideChirurgies.add(c);
    		 }
    		 else {
    			 
    			 this.conflitsChirurgies.put(c, list);
    		 }
    	 });
    	 
    	 this.conflitsChirurgies.entrySet().stream().forEach(
    			 e->e.getValue().forEach(c->this.conflitsChirurgies.get(c).remove(e.getKey()))
    			 );
    	 this.conflitsChirurgies.keySet().stream().filter(e->!this.conflitsChirurgies.get(e).isEmpty());
    	 Collections.sort(this.valideChirurgies);
     }
     
     public void filter(Chirurgien c) {
     	this.setMyChirurgies(this.getMyChirurgies().stream()
     			.filter(e->e.getChirurgien().equals(c)).collect(Collectors.toList()));
     	this.init();
     }
     public void filter(Salle s) {
     	this.setMyChirurgies(this.getMyChirurgies().stream()
     			.filter(e->e.getSalle().equals(s)).collect(Collectors.toList()));
     	this.init();
     }
     public void filter(boolean b) {
    	 if(!b) this.setMyChirurgies(this.valideChirurgies);
    	 else this.setMyChirurgies(this.myChirurgies.stream()
    			 .filter(e->!this.valideChirurgies.contains(e)).collect(Collectors.toList()));
     }
    
     /**
 	 * @param c
 	 * un chirurgien donné
 	 * @param inf
 	 * borne inf de la periode
 	 * @param sup
 	 * borne sup de la periode
 	 * @return boolean
 	 * si une salle est disponible dans la periode designée par les borne données en param
 	 */
 	public static boolean isDispo(List<Chirurgie> list,Chirurgien c,LocalDateTime inf,LocalDateTime sup) {
 		return list.stream()
 		.anyMatch(e->e.getChirurgien().equals(c) &&
 				(e.getDebut().isBefore(inf) && sup.isAfter(e.getFin())
 				|| inf.isBefore(e.getDebut()) && e.getFin().isAfter(sup)));
 	}
 	/**
 	 * @param s
 	 * un salle donné
 	 * @param inf
 	 * borne inf de la periode
 	 * @param sup
 	 * borne sup de la periode
 	 * @return boolean
 	 * si une salle est disponible dans la periode designée par les borne données en param
 	 */
 	public static boolean isDispo(List<Chirurgie> list,Salle s,LocalDateTime inf,LocalDateTime sup) {
 		return list.stream()
 		.anyMatch(e->e.getSalle().equals(s) &&
 		(e.getDebut().isBefore(inf) && sup.isAfter(e.getFin())
 				|| inf.isBefore(e.getDebut()) && e.getFin().isAfter(sup)));
 	}
 	/**
 	 * @param inf
 	 * borne inf de la periode
 	 * @param sup
 	 * borne sup de la periode
 	 * @return Set<Salle>
 	 * elle renvoie l'ensemble des salle disponible entre deux dates fournies en param
 	 */
 	public static Set<Salle> allSalleDispo(List<Chirurgie> list,LocalDateTime inf,LocalDateTime sup){
 		Set<Salle>salleDispo=new HashSet<Salle>();
 		List<Chirurgie> l=list.stream().filter(e->!(e.getDebut()
 				                                      .isBefore(inf) && 
 				                                       sup.isAfter(e.getFin())
 				|| inf.isBefore(e.getDebut()) && e.getFin().isAfter(sup)))
 				.collect(Collectors.toList());
 		l.stream().forEach(e->{
 			salleDispo.add(e.getSalle());
 		});
 		return salleDispo;
 	}
 	/**
 	 * @param inf
 	 * borne inf de la periode
 	 * @param sup
 	 * borne sup de la periode
 	 * @return Set<Salle>
 	 * elle renvoie l'ensemble des  disponible entre deux dates fournies en param
 	 */
 	
 	public static Set<Chirurgien> allChirurgienDispo(List<Chirurgie> list,LocalDateTime inf,LocalDateTime sup){
 		Set<Chirurgien>chirurgienDispo=new HashSet<Chirurgien>();
 		List<Chirurgie> l=list.stream().filter(e->!(e.getDebut()
 				                                      .isBefore(inf) && 
 				                                       sup.isAfter(e.getFin())
 				|| inf.isBefore(e.getDebut()) && e.getFin().isAfter(sup)))
 				.collect(Collectors.toList());
 		l.stream().forEach(e->{
 			chirurgienDispo.add(e.getChirurgien());
 		});
 		return chirurgienDispo;
 	}
 	
 	//des methodes pour des statistiques
 	/**
 	 * 
 	 * @param list la liste qu on parcourt
 	 * @param c un chirurgien donné
 	 * @return l ensemble des salle ou le chirurgien c a deja travaillé
 	 */
     public static Set<Salle>salleOf(List<Chirurgie>list,Chirurgien c){
    	 Set<Salle>salles=new HashSet<Salle>();
    	 list.stream().filter(e->e.getChirurgien().equals(c)).forEach(ch->{
    		 salles.add(ch.getSalle());
    	 });
    	 return salles;
    	 
     }
     /**
      * 
      * @param list  la liste qu on parcourt
      * @param c Chirurgien
      * @param s une salle
      * @return l'ensemble des jour de la semaine sur lesquels le chirurgien c a travaillé dans la salle s
      */
     public static Set<DayOfWeek>dayOf(List<Chirurgie>list,Chirurgien c,Salle s){
    	 Set<DayOfWeek>days=new HashSet<DayOfWeek>();
    	 list.stream().filter(e->e.getChirurgien().equals(c)&& e.getSalle().equals(s)).forEach(ch->{
    		 days.add(ch.getDebut().getDayOfWeek());
    	 });
    	 return days;
     }
     /**
      * 
      * @param l la list à parcourir
      * @param c un chirurgien
      * @param salle une salle
      * @param d un jour de la semaine
      * @return la list des chirurgie effectuées par le chirurgien dans cette salle ,ce jour de la semain
      */
 	public static List<Chirurgie> allOf (List<Chirurgie> l,Chirurgien c,Salle salle,DayOfWeek d){
    	 List<Chirurgie>res=new ArrayList<Chirurgie>();
    	 Predicate<Chirurgie> p=e->e.getChirurgien().equals(c)&& 
    			                   e.getSalle().equals(salle)&& 
    			                   e.getDebut().getDayOfWeek().equals(d);
    	 res=l.stream().filter(p).collect(Collectors.toList());
    	 Collections.sort(res);
    	 return res;
     }
     
 	/**
 	 * 
 	 * @param list la list à parcourir
 	 * @param date la date
 	 * @return la list des chirurgie de cette date
 	 */
 	public static List<Chirurgie>allOf(List<Chirurgie> list,LocalDate date){
    	 return list.stream()
    			 .filter(c->c.getDebut().toLocalDate().equals(date))
    			 .collect(Collectors.toList());
     }
 	
     
    //moyen et ecart
    //applicable partt
 	/**
 	 * 
 	 * @param list la list des chirurgies
 	 * @return la dure moyenne d'une chirurgie dans cette liste
 	 */
    public  static long Avg(List<Chirurgie> list) {
 	   return (long)list.stream().mapToLong(e->e.dure().toMinutes()).average().getAsDouble();
    }
    
    /**
     * 
     * @param list à mappée
     * @return la map regroupant les chirurgies par date
     */
    public static Map<LocalDate,List<Chirurgie>> mapToDate(List<Chirurgie>list) {
		Map<LocalDate,List<Chirurgie>>map=new HashMap<LocalDate,List<Chirurgie>>();
    	list.stream().forEach(e->{
    		map.putIfAbsent(e.getDebut().toLocalDate(), DataModel.allOf(list, e.getDebut().toLocalDate()));
    	});
    	return map;
	}
    
    /**
     * 
     * @param list
     * @return la duré maximale d'une chirurgie dans cette liste
     */
    public  static Duration max(List<Chirurgie>list) {
    	Comparator<Chirurgie>c=(a,b)->a.dure().compareTo(b.dure());
    	return Collections.max(list, c).dure();
    }

    /**
     * 
     * @param list
     * @return la dure minimale d'une chirurgie dans cette liste
     */
    public  static Duration min(List<Chirurgie>list) {
    	Comparator<Chirurgie>c=(a,b)->a.dure().compareTo(b.dure());
    	return Collections.min(list, c).dure();
    }
    
    //la moyenne de chirurgie qu il effectue par jour
    /**
     * 
     * @param list
     * @return les date associées au nombre de chirurgies effectuées
     */
    public static Map<LocalDate,Integer> mapToLocalDate(List<Chirurgie>list){
    	Map<LocalDate,Integer>dest=new HashMap<LocalDate,Integer>();
    	for(Chirurgie e:list) {
    		dest.putIfAbsent(e.getDebut().toLocalDate(), 0);
    		dest.put(e.getDebut().toLocalDate(), dest.get(e.getDebut().toLocalDate())+1);
    	}
    	return dest;
    }
    
    /**
     * 
     * @param list
     * @param c une chirurgie donnée
     * @return l'ecart entre le nombre de chirurgies effctuer le jour de la chirurgie c et 
     * le nombre moyenne de chirurgie par jour pour cette liste
     * elle retourne la valeur normalisé
     */
    public static float nb(List<Chirurgie>list,Chirurgie c) {
    	Map<LocalDate,Integer>dest=mapToLocalDate(list);
    	
    	int max=Collections.max(dest.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    	int min=Collections.min(dest.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    	float res=dest.values().stream().mapToInt(e->e).sum();
    	int in_day=DataModel.allOf(list,c.getDebut().toLocalDate()).size();
    	res=res/dest.keySet().size();
    	if(max==min)return Math.abs((in_day-res)/(max));;
    	res=Math.abs((in_day-res)/(max-min));
    	return res;
    }
    
     
    
    
    public List<Chirurgie> getMyChirurgies() {
		return myChirurgies;
	}
	public void setMyChirurgies(List<Chirurgie> myChirurgies) {
		this.myChirurgies = myChirurgies;
	}
	public List<Chirurgie> getValideChirurgies() {
		return valideChirurgies;
	}
	public void setValideChirurgies(List<Chirurgie> valideChirurgies) {
		this.valideChirurgies = valideChirurgies;
	}
	public Map<Chirurgie, ArrayList<Chirurgie>> getConflitsChirurgies() {
		return conflitsChirurgies;
	}
	public void setConflitsChirurgies(Map<Chirurgie, ArrayList<Chirurgie>> conflitsChirurgies) {
		this.conflitsChirurgies = conflitsChirurgies;
	}
     
}
