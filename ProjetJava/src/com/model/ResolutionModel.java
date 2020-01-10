package com.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.model.entite.*;


public class ResolutionModel {

	private  DataModel modelDonnée;
	private  Modeldecision modelDecision;
	private Set<Conflit>mesConflit;
	private Set<Conflit>conflitRes;
	public ResolutionModel(DataModel modelDonnée) {
		super();
		this.modelDonnée = modelDonnée;
		this.modelDecision=new Modeldecision(this.modelDonnée);
		Conflit.setModel(modelDecision);
		conflitRes=new HashSet<Conflit>();
		init();
	}
	
	/**
	 * construire un conflit selon le type
	 */
	BiFunction<Chirurgie,Chirurgie,Conflit>Construct=(C1,C2)->{
		
			boolean ubiquite=C1.getChirurgien().equals(C2.getChirurgien());
			boolean interference=C1.getSalle().equals(C2.getSalle());
			boolean chevauchement=ubiquite && interference;
			
			if(chevauchement) return new ConflitChevauchement(C1,C2);
			
			else if(ubiquite)return new ConflitUbiquite(C1,C2);
			
			else return new ConflitInterference(C1,C2);
		
	};
	/**
	 * construire l'ensemble des conflit
	 */
	private void init() {
		Set<Conflit>allConflits=new HashSet<Conflit>();
		modelDonnée.getConflitsChirurgies().entrySet()
		.forEach(e->e.getValue()
				.forEach(c->{
					
				    allConflits.add(Construct.apply(e.getKey(), c));
				}
				));
		this.mesConflit=allConflits;
	}
	/**
	 * 
	 */
	public Set<Conflit> allOf(Set<Conflit> l,TypeConflit t){
		return l.stream().filter(e->e.getType().equals(t)).collect(Collectors.toSet());
	}
	public Set<Conflit>allOf(TypeConflit t){
		return allOf(this.mesConflit,t);
	}
    /**
     * 
     * @param list
     * resourdre l'ensemble des chirugies en param
     */
	public void solve(Set<Conflit>list) {
		list.stream().forEach(e->this.solve(e));
	}
	
	/**
	 * 
	 * @param t
	 * @return List des conflit selon le type
	 */
	public Set<Conflit>all(TypeConflit t){
		return this.mesConflit.stream().filter(e->e.getType().equals(t)).collect(Collectors.toSet());
	}
	
	/**
	 * 
	 * @param t type de conflit
	 * resourdre les conflit de ce type
	 */
	public void solveByType(TypeConflit t) {
		this.all(t).stream().forEach(e->this.solve(e));
	}
	public void solve(Conflit c) {
		if(c.solve()) {
			conflitRes.add(c);
		    this.mesConflit.remove(c);
		}
	}
	/**
	 * resoudre les conflit par type
	 */
	public void solveAllbyType() {
		Arrays.stream(TypeConflit.values()).forEach(e->this.solveByType(e));
	}
	/**
	 * resoudre tous les conflits
	 */
	public void solvAll() {
	  this.mesConflit.stream().collect(Collectors.toSet()).stream().forEach(e->this.solve(e));
	}
	//ce qui suit c'est pour l'evaluation et l'interpretation de resultat
	/**
	 * 
	 * @param t le type de conflits
	 * @return le nobre de conflits de type t
	 */
	public int countOf(TypeConflit t) {
		return allOf(getMesConflit(), t).size()
		+allOf(getConflitRes(), t).size();
	}
	
	/**
	 * 
	 * @param t le type de de conflit
	 * @return le nombre de conflits resolus de type t
	 */
	public int solvedOf(TypeConflit t) {
		return allOf(getConflitRes(), t).size();
	}
	/**
	 * 
	 * @return le nombre total de conflits
	 */
	public int count() {
		return mesConflit.size()+conflitRes.size();
	}
	
	/**
	 * 
	 * @return le nombre totale de conflits resolus
	 */
	public int solved() {
		return conflitRes.size();
	}
	/**
	 * afficher les information resultats de la correction
	 */
	public void printResult() {
		System.out.println("tous les conflit   :"+count());
		System.out.println("tous les conflit resolu   :"+solved());
		System.out.println("le pourcentage de rsolution   :"+(float)100*solved()/count());
				
		Arrays.stream(TypeConflit.values()).forEach(e->{
			System.out.println("tous les conflits de type   :"+e+"  "+countOf(e));
			System.out.println("tous les conflits resolus de type   :"+e+"  "+solvedOf(e));
			System.out.println("le pourcentage de rsolution les conflits resolus de type   :"
			+e+"  "+(float)100*solvedOf(e)/countOf(e));
			
			
		});
		
	}
	public DataModel getModelDonnée() {
		return modelDonnée;
	}

	public void setModelDonnée(DataModel modelDonnée) {
		this.modelDonnée = modelDonnée;
	}

	public Modeldecision getModelDecision() {
		return modelDecision;
	}

	public void setModelDecision(Modeldecision modelDecision) {
		this.modelDecision = modelDecision;
	}

	public Set<Conflit> getMesConflit() {
		return mesConflit;
	}

	public void setMesConflit(Set<Conflit> mesConflit) {
		this.mesConflit = mesConflit;
	}

	public Set<Conflit> getConflitRes() {
		return conflitRes;
	}

	public void setConflitRes(Set<Conflit> conflitRes) {
		this.conflitRes = conflitRes;
	}
	
	
	
	
	
}
