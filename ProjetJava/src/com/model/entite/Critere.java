package com.model.entite;

public enum Critere {
chirurgien("stat chirurgien",0.25f),
chirurgien_salle("stat pour la combinaison chirurgien salle",0.30f),
chirurgien_jour("stat pour la combinaison chirurgien jour",0.25f),
chirurgien_salle_jour("stat pour la combinaison chirurgien salle jour",0.2f),
salle("stat pour une salle",0.15f),
salle_jour("stat pour une salle dans un jour de la semaine",0.15f),
;

private String description;
private float val;
Critere(String desc,float val){
	 this.description=desc;
	 this.val=val;
 }
public String getDescription() {
	return this.description;
}

public float getVal() {
	return this.val;
}
}
