package com.model.utilitair;

import java.util.List;
/*
 * une interface pour passer de mon model de donné à un model plus concret 
 * avoir une list d'objet precisement une liste globale contenant toutes
 * mes chirurgies
 * @author  baa hamza
 * @version 1.0 
 * @see CsvFile
 */

import com.model.entite.Chirurgie;
public interface ChirurgieDAO {
	/**
	 * @see CsvChirurgieDAO#findAllChirurgies()
	 */
    List<Chirurgie> findAllChirurgies();
}
