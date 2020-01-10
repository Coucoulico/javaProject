package com.model.utilitair;

import java.io.*;
import java.util.*;

/**
 * 
 * c'est une classe utlitaire qui permet d'acceder à tout fichier 
 * et recuperer son contenu ligne par ligne (lecture du fichier)
 * @author  baa hamza
 * @version 1.0 
 * @see CsvFile
 */
public class CsvUtilitaire {
	
	/**
	 * c'est une methode permettant de construire le chemain absolu du fichier
	 * @param fileName
	 *         le nom du fichier sera donné en parametre
	 * @return 
	 * renvoyer une chaine de carractere designant le chemin absolu vers le fichier
	 */
	
	public static String getResourcePath(String fileName) {
	       final File f = new File("");
	       final String dossierPath = f.getAbsolutePath() + File.separator + fileName;
	       return dossierPath;
	   }
    
	/**
	 * lecture et creation d'un pointeur vers un fichier dont le nom est donné en param
	 * @param fileName
	 * @see CsvUtilitaire#getResourcePath(String fileName)
	 * 
	 * @return un pointeur vers le fichier donné en param (une instance de File)
	 */
	   public static File getResource(String fileName) {
	       final String completeFileName = getResourcePath(fileName);
	       File file = new File(completeFileName);
	       return file;
	   }
	   
	   /**
	    * cette methode permet d'avoir les donnée d'un fichie donné en param ligen par ligne
	    * 
	    * @param file
	    *     le fichier du quel on veut extraire les données
	    * @return une liste de chaine de carracter chacune correspond à une ligne dans notre fichier
	    * @throw FileNotFoundException si jamais le fichier n'existe pas
	    */
	   public static List<String> readFile(File file) {

	        List<String> result = new ArrayList<String>();
            
	        try {
	        
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
	            result.add(line);
	        }
	        
	        br.close();
	        fr.close();
            }
            
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } 
            catch (IOException e) {
                e.printStackTrace();
              }   
	        

	        

	        return result;
            
              	
	    }
}
