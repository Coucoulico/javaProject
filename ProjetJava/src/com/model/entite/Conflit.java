package com.model.entite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.model.DataModel;
import com.model.Modeldecision;

/**
 * 
 * @author Hamza
 * cette classe represente les conflits
 * version 1.0
 *
 */
public abstract class Conflit {
	/**
	 * les model à partir du quel je calcul des stat et je calcule des statistiques 
	 * @see com.model.Modedecision
	 */
    protected static Modeldecision model;
    /**
     * les deux chirurgies 
     * @see com.model.entite.Chirurgie
     */
	protected Chirurgie c1;
	protected Chirurgie c2;
	/**
	 * structure des critere et de sous critere
	 * @see com.model.entite.Critere
	 * @see com.model.entite.SousCritere
	 */
	protected static Map<Critere,List<SousCritere>>mesCoef;
	/**
	 * le type du conflit
	 * @see @see com.model.entite.TypeConflit
	 */
	protected final TypeConflit type;
	
	/**
	 * la chirurgie à corriger
	 */
	protected Chirurgie toCorect;
	
	public Conflit(Chirurgie c1, Chirurgie c2,TypeConflit t) {
		this.c1 = c1;
		this.c2 = c2;
		type=t;
		Conflit.init();
	} 
	/**
	 * cette methode permet d'initialiser les critere
	 * elle sera appelée apres chaque creation d'une instance
	 */
	protected static void init() {
		mesCoef=new HashMap<Critere,List<SousCritere>>();
		Arrays.stream(Critere.values()).forEach(e->{
			mesCoef.put(e, Arrays.stream(SousCritere.values()).collect(Collectors.toList()));
		});
        
		
	}
	/**
	 * 
	 * @param c une chirurgie
	 * @return l'ensemble des critere associer à cette chirurgie avec leur valeurs
	 */
	public static Map<Critere,Map<SousCritere,Float>> init(Chirurgie c){
		Map<Critere,Map<SousCritere,Float>>res=new HashMap<Critere,Map<SousCritere,Float>>();
		mesCoef.entrySet().forEach(e->{
			res.put(e.getKey(), new HashMap<SousCritere,Float>());
			e.getValue().forEach(s->{
				res.get(e.getKey()).put(s, valOf(c, e.getKey(), s));
			});
		});
		return res;
	}
	
	
	/**
	 * 
	 * @param c une chirurgie
	 * @param cr une combinaison
	 * @return la list des chirurgie associée à ce critere
	 */
	protected static List<Chirurgie> allOf(Chirurgie c,Critere cr) {
		List<Chirurgie>list=new ArrayList<Chirurgie>();
		switch (cr) {
			case chirurgien:
				return model.allOf(c.getChirurgien());
			case salle:
				return model.allOf(c.getSalle());
			case chirurgien_salle:
				return model.allOf(c.getChirurgien(), c.getSalle());
			case chirurgien_salle_jour:
				return model.allOf(c.getChirurgien(), c.getSalle(), c.getDebut().getDayOfWeek());
			case salle_jour:
				return model.allOf(c.getSalle(), c.getDebut().getDayOfWeek());
			case chirurgien_jour:
				return model.allOf(c.getChirurgien(), c.getDebut().getDayOfWeek());
			default:
				return list;
			
		}
        
	}
	
	/**
	 * 
	 * @param c chirurgie
	 * @param cr combinaison
	 * @return un entier qui est la cardinalité de l'espace des chirurgie selon la combinaison 
	 * il nous sert aux calcul des proba
	 */
	protected static int sizeUniver(Chirurgie c,Critere cr) {
		switch (cr) {
		case chirurgien:
			return model.getMesChirurgies().size();
		case salle:
			return model.getMesChirurgies().size(); 
		case salle_jour:
			return model.allOf(c.getSalle()).size();
		case chirurgien_jour:
			return model.allOf(c.getChirurgien()).size();
		case chirurgien_salle:
			return (model.allOf(c.getChirurgien()).size()+model.allOf(c.getSalle()).size())/2;
		case chirurgien_salle_jour:
			return (model.allOf(c.getChirurgien(), c.getDebut().getDayOfWeek()).size()
					+model.allOf(c.getSalle(),c.getDebut().getDayOfWeek()).size())/2; 
		default:
			return Integer.MAX_VALUE;
		}
      
		
	}
	/**
	 * 
	 * @param c une chirurgie
	 * @param cr une combinaison
	 * @param sc une critere
	 * @return la valeur associé au critere de la combinaison 
	 */
	public static float valOf(Chirurgie c,Critere cr,SousCritere sc) {
		if(Conflit.allOf(c, cr).isEmpty()||Conflit.sizeUniver(c,cr)==0)
			return 0f;
		List<Chirurgie>list=Conflit.allOf(c, cr);
		list.add(c);
		switch (sc) {
		case proba:
			return (float)list.size()/Conflit.sizeUniver(c,cr);
		case avg:
			long max=DataModel.max(list).toMinutes();
			long min=DataModel.min(list).toMinutes();
			long d=DataModel.Avg(list);
			
			float moyenne=Math.abs((float)(d-min)/(max-min));
			float dure=Math.abs((float)(c.dure().toMinutes()-min)/(max-min));
			
			return Math.abs(dure-moyenne);	
		case nb:
			return DataModel.nb(list,c);	
		default:
			return 0;
			
		}
		
	}
	
	/**
	 * 
	 * @param c la chirurgie
	 * @param cr la combinaison
	 * @return le score de la chirurgie c selon la combinaison
	 */
	public static float Vallof(Chirurgie c,Critere cr) {
		if(Conflit.allOf(c, cr).isEmpty())return 0f;
		else {
				return (float)Conflit.init(c).get(cr).entrySet().stream()
		.mapToDouble(e->e.getValue()*e.getKey().getPoids()).sum();
		}
		
	}
	/**
	 * 
	 * @param c une chirurgie
	 * @return le score de la chirurgie score
	 */
	public static float score(Chirurgie c) {
		return (float)Conflit.init(c).entrySet().stream()
		.mapToDouble(e->e.getKey().getVal()*Conflit.Vallof(c, e.getKey())).sum();
	}
	Comparator<Chirurgie>comp=(a,b)->Float.compare(score(a),score(b));
	public Chirurgie wichChirurgie() {
		
		if(comp.compare(c1, c2)>0||score(c1)==score(c2)) return c2;
		else return c1;
		
	}
	
	/**
	 * 
	 * @return si le conflit est resolu ou pas
	 * c'est la methode qui permet la resolution des conflit 
	 */
	public abstract boolean solve();
	/**
	 * resoudre le conflit en changeant la salle 
	 * @return si le conflit est resolu ou pas
	 * 
	 */
	public boolean solveSalle() {
		
		Set<Salle>toc1=DataModel.allSalleDispo(model.getMesChirurgies(), c1.getDebut(), c1.getFin());
		Set<Salle>toc2=DataModel.allSalleDispo(model.getMesChirurgies(), c2.getDebut(), c2.getFin());
		
		toc2.remove(c1.getSalle());
		toc1.remove(c1.getSalle());
		
		if(toc1.isEmpty()&&toc2.isEmpty())return false;
		else {
		if(!toc1.isEmpty()&& !toc2.isEmpty()) this.toCorect=this.wichChirurgie();
		if(toc1.isEmpty()&& !toc2.isEmpty()) this.toCorect=c2;
		else this.toCorect=c1;
		Set<Salle>ens=DataModel.allSalleDispo(model.getMesChirurgies(), toCorect.getDebut(), toCorect.getFin());
		ens.remove(toCorect.getSalle());
		Salle toUpdate=ens.stream()
				.max((a,b)->Float.compare(score(toCorect.setSalle(a)), score(toCorect.setSalle(a)))).get();		      
		toCorect.setSalle(toUpdate);
		return true;
		}
	}
	
	/**
	 * resoudre le conflit en changeant le chirurgien
	 * @return si le conflit est resolu ou pas
	 */
public boolean solveChirurgien() {
		
		Set<Chirurgien>toc1=DataModel.allChirurgienDispo(model.getMesChirurgies(), c1.getDebut(), c1.getFin());
		Set<Chirurgien>toc2=DataModel.allChirurgienDispo(model.getMesChirurgies(), c2.getDebut(), c2.getFin());
		toc1.remove(c1.getChirurgien());
		toc2.remove(c1.getChirurgien());
		if(toc1.isEmpty()&&toc2.isEmpty()) {
			return false;
		}
		else if(!toc1.isEmpty()&& !toc2.isEmpty()) this.toCorect=this.wichChirurgie();
		else if(toc1.isEmpty()) this.toCorect=c2;
		else this.toCorect=c1;
		Set<Chirurgien>mesChirurgien=DataModel.allChirurgienDispo(model.getMesChirurgies(), toCorect.getDebut(), toCorect.getFin());
		mesChirurgien.remove(toCorect.getChirurgien());
		if(mesChirurgien.isEmpty())return false;
		Chirurgien toUpdate=mesChirurgien.stream()
				.max((a,b)->Float.compare(score(toCorect.setChirurgien(a)), score(toCorect.setChirurgien(a)))).get();		      
		toCorect.setChirurgien(toUpdate);
		return true;				
	}
	

	public static void setModel(Modeldecision model) {
		Conflit.model=model;
	}
	public static Modeldecision getModel() {
		return Conflit.model;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj==null) return false;
		if((     obj instanceof ConflitUbiquite 
				|| obj instanceof ConflitInterference
				|| obj instanceof ConflitChevauchement))
			
		 {
			Conflit c=(Conflit)obj;
			return (this.c1.equals(c.c1)&& this.c2.equals(c.c2) ||this.c2.equals(c.c1)&& this.c1.equals(c.c2));
		}
		 return false;
		
	}

	
	public String toString(Chirurgie c) {
		String res=c.toString()+"\ntype    : "+this.type+"\n"+"score=   :"+Conflit.score(c)+"\n";
		res+=Conflit.init(c).entrySet().stream()
		.map(e->new String(e.getKey()+"  :"+Conflit.Vallof(c, e.getKey())+"\n   "+e.getValue().toString()+"\n")
		).collect(Collectors.joining());;
		return res;
	}
	
	
	@Override
	public String toString() {
		
	    return "C1: "+toString(this.c1)+"C2   :"+toString(this.c2)+"\n"
	    		+ "             *******************************$********************************************";
	}

	public Chirurgie getC1() {
		return c1;
	}

	public void setC1(Chirurgie c1) {
		this.c1 = c1;
	}

	public Chirurgie getC2() {
		return c2;
	}

	public void setC2(Chirurgie c2) {
		this.c2 = c2;
	}

	public Chirurgie getCorrectionChirurgie() {
		return toCorect;
	}

	public void setCorrectionChirurgie(Chirurgie correctionChirurgie) {
		this.toCorect = correctionChirurgie;
	}

	public TypeConflit getType() {
		return type;
	}
	
	
	
}
