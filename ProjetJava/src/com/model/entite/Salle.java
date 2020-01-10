package com.model.entite;

public enum Salle {
	BLOC_E1("BLOC-E1",1),
	BLOC_E2("BLOC-E2",2),
	BLOC_E3("BLOC-E3",3),
	BLOC_U3("BLOC-U3",4),
	BLOC_U4("BLOC-U4",5),
	BLOC_U5("BLOC-U5",6),
	BLOC_U6("BLOC-U6",7),
	BLOC_U7("BLOC-U7",8),
	BLOC_U8("BLOC-U8",9),
	BLOC_U9("BLOC-U9",10),
	BLOC_U10("BLOC-U10",11);
	
	private String name = "";
	private int value;
    
    Salle(String name, int value)
    {
    	this.name = name;
        this.value = value;
    }
    
    public int getValue()
    {
        return this.value;
    }
	
    public String getName()
    {
    	return name;
    }
}
