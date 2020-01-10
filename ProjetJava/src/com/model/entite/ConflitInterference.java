package com.model.entite;


public class ConflitInterference extends Conflit {
	
	
	public ConflitInterference(Chirurgie c1, Chirurgie c2) {
		super(c1, c2,TypeConflit.interference);
		//ConflitInterference.init();
		// TODO Auto-generated constructor stub
	}

	public static void init() {
		
		mesCoef.remove(Critere.chirurgien);
		mesCoef.remove(Critere.chirurgien_jour);
	}
	
	public static int sizeUniver(Chirurgie c,Critere cr) {
		
		switch (cr) {
		case chirurgien_salle:
			return model.allOf(c.getChirurgien()).size();
		case chirurgien_salle_jour:
			return model.allOf(c.getChirurgien(), c.getDebut().getDayOfWeek()).size(); 
		default:
			return Conflit.sizeUniver(c, cr);
		}
	}

	@Override
	public boolean solve() {
		// TODO Auto-generated method stub
		if(solveSalle()) {
			model.getMesChirurgies().add(c1);
			model.getMesChirurgies().add(c2);
			model.add(c1);
			model.add(c2);
			return true;
		}
		return false;
	}
	
}
