package com.model.entite;
/**
 * 
 * @author hamza
 * @version 1.0
 * 
 *
 */

public class ConflitChevauchement extends Conflit {
    
    public ConflitChevauchement(Chirurgie c1, Chirurgie c2) {
		super(c1, c2,TypeConflit.chevauchement);
		// TODO Auto-generated constructor stub
	}
 
	@Override
	public boolean solve() {
		// TODO Auto-generated method stub
		Conflit c=this.clone();
		if(c.solveSalle()&&c.solveChirurgien()) {
			
			this.solveSalle();
			this.solveChirurgien();
			model.getMesChirurgies().add(c1);
			model.getMesChirurgies().add(c2);
			model.add(c1);
			model.add(c2);
			return true;
		}
		else {
			c=this.clone();
			if(c.solveChirurgien()&&c.solveSalle()) {
					this.solveChirurgien();
					this.solveSalle();
					return true;
				
			}
		}
		return false;
	}
	public Conflit clone() {
		Conflit c=new ConflitChevauchement(this.c1.clone(),this.c2.clone());
		if(this.toCorect!=null)c.toCorect=this.toCorect.clone();
		return c;
	}
	
}
