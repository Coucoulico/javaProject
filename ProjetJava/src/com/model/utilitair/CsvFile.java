package com.model.utilitair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * c'est ma classe detaillant ma modelisation standard d'un fichier csv 
 * @author  baa hamza
 * @version 1.0 
 * @see CsvFile
 */

public class CsvFile implements Csv {
    
	/**
	 * le separateur par defaut des valeur d'un fichier csv est ;
	 */
	public static final char DEFAULT_SEPARATOR=';';
    
	/**
	 * le fichier à modeliser en tant que fichier csv
	 */
    private File file;
    
    /**
     * les lignes de donnée que le fichier contient rq:les headers sont inclus
     */
    
    private List<String>dataLignes;
    
    /**
     * les entetes de mes colonnes
     */
    private String[] headers;
    
    /**
     * les lignes de données en champs separés en exclu les titre de mes colonnes ,
     */
    private List<String[]> dataRows;
    
    /**
     * les données mappée
     */
    private List<Map<String,String>> mappedData;
    
    
    /**
     * constructeur de mon model
     * @param file
     * corresond au fichier source
     * 
     */
    public CsvFile(File file) {
    	this.file=file;
    	
    	init();
    }
    
    /**
     * cette methode permet d'initialiser les different champs de mon model CsvFile
     * elle est appelée dans le constructeur apres avoir creer l'instance de mon fichier
     * 
     */
    
    private void init() {
    	String separator =""+DEFAULT_SEPARATOR+"";
    	dataLignes=CsvUtilitaire.readFile(file);
    	this.headers=dataLignes.get(0).split(separator);
    	
    	dataRows=new ArrayList<String[]>();
    	mappedData = new ArrayList<Map<String, String>>();
    	
    	
    	
    	dataLignes.remove(0);

    	for(String line: dataLignes) {
    		if(line.isEmpty()) continue;
            
           
           String[] row=line.trim().split(separator);
           final Map<String,String> map=new HashMap<String,String>();
           for(int i=0;i<row.length;i++) {
        	    String value=row[i];
        	    String key=headers[i];
        	    
        	   map.put(key, value);
        	   
           }
           mappedData.add(map);
           dataRows.add(row);
    	   
    	}
    	mappedData.remove(0);
    	dataRows.remove(0);
    	
    
    	
    }
   
	
    /**
     * (non-Javadoc)
     * @see com.model.utilitair.Csv#getFile()
     * @return renvoyer les fichier source de mes données
     */
    @Override
	public File getFile() {
		// TODO Auto-generated method stub
		return this.file;
	}

    /**
     * (non-Javadoc)
     * @see com.model.utilitair.Csv#getData()
     * @return renvoyer les lignes de mes données
     * @see Csvfile#dataRows
     */
	@Override
	public List<String[]> getData() {
		// TODO Auto-generated method stub
		return this.dataRows;
	}
	
	/**
     * (non-Javadoc)
     * @see com.model.utilitair.Csv#getData()
     * @return renvoyer les titre de mes colonnes 
     * @see Csvfile#headers
     */

	@Override
	public String[] getTitles() {
		// TODO Auto-generated method stub
		return this.headers;
	}

    /**
     * (non-Javadoc)
     * @see com.model.utilitair.Csv#getMappeddata()
     * @return  List<Map<String, String>> les données mappées
     * @see CsvFile#mappedData
     */
	@Override
	public List<Map<String, String>> getMappeddata() {
		// TODO Auto-generated method stub
		return this.mappedData;
	}
	
	

}
