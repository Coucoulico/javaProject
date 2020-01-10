package com.model.utilitair;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import com.model.entite.Chirurgie;
import com.model.entite.Chirurgien;
import com.model.entite.Salle;

/**
 * @author  baa hamza
 * @version 1.0 
 * @see CsvFile
 */
public class CsvChirurgieDAO implements ChirurgieDAO {

	/*
	 * mon fichier source
	 */
	
	private File file;
	
	/*
	 * mon model associer à ce fichier
	 */
	private CsvFile csvfile;
	
	/*
	 * construir mon model à partir de mon fichier source
	 */
	public CsvChirurgieDAO(File file) {
		this.file = file;
		this.csvfile=new CsvFile(this.file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public CsvFile getCsvfile() {
		return csvfile;
	}

	public void setCsvfile(CsvFile csvfile) {
		this.csvfile = csvfile;
	}
    
	/**
	 * (non-Javadoc)
	 * @see com.model.utilitair.ChirurgieDAO#findAllChirurgies()
	 * @return List<Chirurgie>
	 *         construire une liste de mes chirurger et la renvoyer 
	 */
	@Override
	public List<Chirurgie> findAllChirurgies() {
		// TODO Auto-generated method stub
		this.csvfile=new CsvFile(this.file);
		List<Chirurgie>myList=new ArrayList<Chirurgie>();
		for(Map<String,String> map :csvfile.getMappeddata()) {
			
			myList.add(mapToChirurgie(map));
		}
		return myList;
	}
	
	/**
	 * construire une instance d'un objet à partir d'une map
	 * @param map 
	 *       la map contenant les valeur associé à leur attribue 
	 */
	private static Chirurgie mapToChirurgie(Map<String,String> map) {
		int id_chirurgie=0;
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
	     id_chirurgie=Integer.parseInt(map.get("ID CHIRURGIE"));
        }
        catch(Exception e) {
        	
        }
       
        Salle s=EnumSet.allOf(Salle.class).stream()
        		.filter(e->e.getName().equals(map.get("SALLE")))
        		.findFirst()
        		.get();
	    Chirurgien chirurgien=EnumSet.allOf(Chirurgien.class).stream()
        		.filter(e->e.getName().equals(map.get("CHIRURGIEN")))
        		.findFirst()
        		.get();
	    //les formatter de temps et de date
	    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
	    LocalTime tdebut=LocalTime.parse(map.get("HEURE_DEBUT CHIRURGIE"), formatterTime);
	    LocalTime tfin=LocalTime.parse(map.get("HEURE_FIN CHIRURGIE"), formatterTime);
	    
	    LocalDate debut=LocalDate.parse(
	    		map.get("DATE CHIRURGIE"), formatterDate
	    		);
		LocalDate fin=LocalDate.parse(map.get("DATE CHIRURGIE"), formatterDate
				);
		LocalDateTime dtdebut=LocalDateTime.of(debut, tdebut);
		LocalDateTime dtfin=LocalDateTime.of(fin, tfin);
		//pour les chirurgie qui commence avant minuit et qui se termine apres minuit
	    if(tdebut.isAfter(tfin)) { 
	    	if(Duration.between(dtfin.plusDays(1), dtdebut).toMinutes()<Duration.ofDays(1).toMinutes())
			dtfin=dtfin.plusDays(1);
			
			
		}
	    
		//pour traiter le cas ou une chirurgie commence avant minuit et se termine apres
		//ce qui est equivaut à ajouter un jour pour la date de fin
		
		return new Chirurgie(id_chirurgie,s,chirurgien,dtdebut,dtfin);
	}
	

}
