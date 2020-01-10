package com.model;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import com.model.entite.Chirurgien;
import com.model.entite.Salle;
import com.model.entite.TypeConflit;
import com.model.utilitair.CsvChirurgieDAO;

public class Interface {
	final String fILE_NAMES="Base.csv";
	final String fILE_NAME2="MiniBase.csv";
	final CsvChirurgieDAO csv=new CsvChirurgieDAO(new File(fILE_NAMES));
	static Modeldecision dec;
	ResolutionModel res;
	DataModel dm;
    public Interface() {
    
    dm=new DataModel(csv.findAllChirurgies());
    this.afficher();
    }
    
    public void initModels(DataModel d) {
    dec=new Modeldecision(d); 
    res=new ResolutionModel(d);
    }
    
    private static void print(Object obj) {
		System.out.println(obj.toString());
	}
    
    
    
    public Chirurgien chooseChirurgien() {
    	print("***** quelle chirurgien    ? ******");
    	Arrays.stream(Chirurgien.values()).forEach(e->print(e.getValue()+"  "+e.getName()));
    	Scanner sc=new Scanner(System.in);
    	
    	
    		int id=sc.nextInt();
    		if (Arrays.stream(Chirurgien.values()).anyMatch(e->e.getValue()==id))
    			return Arrays.stream(Chirurgien.values()).filter(e->e.getValue()==id).findFirst().get();
    		else return chooseChirurgien();	
    	
    }
    
    public Salle chooseSalle() {
    	print("*****    quelle salle     ? ******");
    	Arrays.stream(Salle.values()).forEach(e->print(e.getValue()+"  "+e.getName()));
    	Scanner sc=new Scanner(System.in);
    	
    	
    		int id=sc.nextInt();
    		if (Arrays.stream(Salle.values()).anyMatch(e->e.getValue()==id))
    			return Arrays.stream(Salle.values()).filter(e->e.getValue()==id).findFirst().get();
    		
    		else return chooseSalle();
    		
    }
    
    public boolean chooseCausalite() {
    	print("valides    1");
    	print("en conflit     2");
    	Scanner sc=new Scanner(System.in);
    	switch(sc.nextInt()) {
    	case 1:
    		return false;
    	case 2:
    		return true;
    	default:
    		return chooseCausalite();
    	}

    }
    
    
    public void afficher(){
    	this.dm=new DataModel(this.csv.findAllChirurgies());
    	print("              ******* menu principal  **********                 ");
    	print("traiter chirurgies         1");
       	print("traiter conflit         2");
       	print("pour quiter    0");
       	
       	Scanner sc=new Scanner(System.in);
    	int i=sc.nextInt();
    	switch(i) {
    	case 1:
    		visualiseChirurgie();
    	case 2:
    		visualiserConflit();
    		break;
    	case 0:
    		return;
    	}
       	 
    }
    
    public TypeConflit chooseType() {
    	print("***** quelle type    ? ******");
    	Arrays.stream(TypeConflit.values()).forEach(e->print(e.ordinal()+1+"    "+e));
    	Scanner sc=new Scanner(System.in);
        int i=sc.nextInt();
        if(i>=0 && i<=TypeConflit.values().length)
        	return Arrays.stream(TypeConflit.values()).filter(e->e.ordinal()==i-1).findFirst().get();
        else return chooseType();
    }
    
    public boolean chooseByType() {
    	print("***** filtrer ou afficher    ? ******");
    	print("filtrer    1");
    	print("afficher    2");
    	Scanner sc=new Scanner(System.in);
    	switch(sc.nextInt()) {
    	case 1:
    		return true;
    	case 2:
    		return false;
    	default:
    		return chooseByType();
    	}
    	}
    public void visualiserConflit() {
    	
    	this.res=new ResolutionModel(this.dm);
    	filterConflit();
    	print("*******   visualiser ou resoudre   *******");
    	print("visualiser     1");
    	print("resoudre       2");
    	Scanner sc=new Scanner(System.in);
    	switch(sc.nextInt()) {
    	case 1:
    		if(res.getMesConflit().isEmpty()) {
    			print("aucune conflit   !");
    		}
    		else {
    			res.getMesConflit().stream().forEach(e->print(e));
    		    askToSolve();
    		    askToVisualise();
    		}
    		afficher();
    		break;

    	case 2:
    		solve();
    		askToVisualise();
    		afficher();
    		break;
    	default:
    	}
    	
    	
    }
    
    public boolean chooseYes() {
    	Scanner sc=new Scanner(System.in);
    	print("OUI    1      NON           0");
       	int i=sc.nextInt();
       	switch (i) {
       		case 1:
       			return true;
       		case 0:
       			return false;
       		default:
       			return chooseYes();
       	}
    }
    public void filterConflit() {
    	if(chooseByType()) {
    		res.setMesConflit(res.allOf(chooseType()));
    	}
    }
    
    public void visualiseChirurgie() {
    	if(chooseByType())filterChirurgie();
    	if(dm.getMyChirurgies().isEmpty())print("aucune chirurgie trouvée pour les critere donnés   !");
    	else dm.getMyChirurgies().stream().forEach(e->print(e));
    }
    
    public void filterChirurgie() {
      		 print("Par chirurgien         1");
         	 print("par salle             2");
         	 print("causalité         3"); 
         	 print("valider        4");
      	 Scanner sc=new Scanner(System.in);
       	int i=sc.nextInt();
       	switch (i) {
       	case 1:
       		dm.filter(chooseChirurgien());
       		filterChirurgie();
       		break;
       	case 2:
       		dm.filter(chooseSalle());
       		filterChirurgie();
       		
       		break;
       	case 3:
       		dm.filter(chooseCausalite());
       		filterChirurgie();
       		break;
       	case 4:
       		if(dm.getMyChirurgies().isEmpty())print("aucune chirurgie trouvée pour les critere donnés   !");
        	else dm.getMyChirurgies().stream().forEach(e->print(e));
       		afficher();
       	default:
       		
       	}
      	
      	 
      	 
      	}
      	 
    public void solve() {
    	res.solvAll();
    	print("traitements terminés   !!!");
    }
    public void askToSolve() {
    	print("voulez vous resoudre les conflit  ?");
    	if(chooseYes()) {
    	solve();
    	}
    	else afficher();
    }
    public void askToVisualise() {
    	print("voulez vous visualiser les conflit  ?");
    	if(chooseYes()) {
    	    if(res.getConflitRes().isEmpty())print("acun conflit n est resolus !!!");
    	    else {
    	    	res.getConflitRes().stream().forEach(e->print(e));
    	    }
    	    res.printResult();
    	}	
    	else afficher();
    }
    
}
