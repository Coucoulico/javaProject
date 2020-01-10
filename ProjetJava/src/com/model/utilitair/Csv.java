package com.model.utilitair;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * c'est une interafce permetant de modeliser les methode dont on a besoin pour
 * implementer le contenue d'un fichier Csv en memoire
 * Ex:recuperer les donnée en ligne
 *   recuperer le fichier source base de donnée
 *   recuperer les headers (les titres de colonne)
 *   recuperer les données ligne par ligne 
 *   recuperer les données mappée
 * @author  baa hamza
 * @version 1.0 
 * @see CsvFile
 */

public interface Csv {
	/**
	 * c'est une methode qui nous permettra d'avoir le fichier à partir
	 * duquele on avait extrait nos données les fichier pointé
	 * @see CsvFile#getFile()
	 */
	
	File getFile();
    
	/**
      * à partir du fichier on extrait les données ligne par ligne 
      * puis on renvoie les ligne dans une liste
      * @see CsvFile#getData()
    
      */
    List<String[] > getData();
    
    /**
     *cette methode permet de recuperer l'entete de mon fichier csv
     *les titre de colonne dans un tableau
     *@see CsvFile#getTitle
     * 
     */
    String[] getTitles();
    
    /**
     * c'est une methode qui nous permet d'avoir les données de mon fichier
     * mais cette fois ci elle seront mappées 
     * Ex:attribut1->val1  ;attribut2->val2  .....Attribut N->val N ...etc
     * @see  
     */
    List<Map<String,String>> getMappeddata();
}
