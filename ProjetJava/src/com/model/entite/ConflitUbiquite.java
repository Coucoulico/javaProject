package com.model.entite;


public class ConflitUbiquite extends Conflit {
   
	
	
	public ConflitUbiquite(Chirurgie c1, Chirurgie c2) {
		
		super(c1, c2,TypeConflit.ubiquité);
		//init();
		// TODO Auto-generated constructor stub
	}
	public static void init() {
		mesCoef.remove(Critere.salle);
		mesCoef.remove(Critere.salle_jour);
		
	}
	
	public static int sizeUniver(Chirurgie c,Critere cr) {
		switch (cr) {
		case chirurgien_salle:
			return model.allOf(c.getSalle()).size();
		case chirurgien_salle_jour:
			return model.allOf(c.getSalle(),c.getDebut().getDayOfWeek()).size(); 
		default:
			return Conflit.sizeUniver(c, cr);
		}
	}
	@Override
	public boolean solve() {
		// TODO Auto-generated method stub
		if(solveChirurgien()) {
			model.getMesChirurgies().add(c1);
			model.getMesChirurgies().add(c2);
			model.add(c1);
			model.add(c2);
			return true;
		}
		return false;
	}
	
}
