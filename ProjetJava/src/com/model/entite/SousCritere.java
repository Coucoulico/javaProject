package com.model.entite;
/**
 * 
 * @author hamza
 * c'est une enumeration des critere utiliser pour le calcule du score
 * @version 1.0
 *
 */
public enum SousCritere {
	/**
	 * la probabilité
	 */
     proba("proba",10),
     /**
      * la durée moyenne des chirurgie
      */
     avg("ecart entre la durée moyenne et la durée d une chirurgie donnée",-3),
     /**
      * le nombre moyenne de chirurgie effectuer par jour 
      */
     nb("ecart entre le nombre moyen de chirugie effectuer par jour et le nombre de chirurgie "
     		+ "effectuées en un date donnée",-1);
     
     
	
	private String name = "";
	private int poids;
    
    SousCritere(String name, int poids)
    {
    	this.name = name;
        this.poids = poids;
    }
    /**
     * 
     * @return int un entier representant le poid associe au critere
     */
    public int getPoids()
    {
        return this.poids;
    }
	
    /**
     * 
     * @return une chaine de carractere definissant la designation du critere
     */
    public String getName()
    {
    	return name;
    }
}
