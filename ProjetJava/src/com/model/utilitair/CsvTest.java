package com.model.utilitair;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CsvTest {
	private final static String FILE_EXIST = "/MiniBase.csv";
	private final static String FILE_NOT_EXIST="/DoesNtExist.csv";
	
	@Test
	void testGetResourcePath() {
				// Param
	    String fileName = FILE_EXIST;

	    // Result est ce que le fichier existe ou pas
	    // ...

	    // Appel
	     File file = CsvUtilitaire.getResource(fileName);

	    // Test
	    // On sait que le fichier existe bien puisque c'est avec lui qu'on travaille depuis le début.
	    assertTrue(file.exists());
	    // on test un fichier qui n'exist pas
	    fileName = FILE_NOT_EXIST;
	    
	    //Appel
	    
	    file = CsvUtilitaire.getResource(fileName);
	    
	    //on sait à priori que le fichier n'exist plus
	    assertFalse(file.exists());
	}

	

	@Test
	void testReadFile() {
		final int nombreLigne = 26;
        String fileName=FILE_EXIST;
	    
        // Appel
	    final File file = CsvUtilitaire.getResource(fileName);
	    List<String> lines = CsvUtilitaire.readFile(file);

	    // Test
	    Assert.assertEquals(nombreLigne, lines.size());
	}
	}

