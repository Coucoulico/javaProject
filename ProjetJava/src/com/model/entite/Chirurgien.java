package com.model.entite;

public enum Chirurgien {
	LAWRENCE_KUTNER("LAWRENCE KUTNER",1),
	ROBERT_CHASE("ROBERT CHASE",2),
	GREGORY_HOUSE("GREGORY HOUSE",3),
	JAMES_WILSON("JAMES WILSON",4),
	ERIC_FOREMAN("ERIC FOREMAN",5),
	LISA_CUDDY("LISA CUDDY",6),
	CHRIS_TAUB("CHRIS TAUB",7),
	REMY_HADLEY("REMY HADLEY",8),
	PRESTON_BURKE("PRESTON BURKE",9),
	DEREK_SHEPHERD("DEREK SHEPHERD",10),
	MEREDITH_GREY("MEREDITH GREY",11),
	ALEX_KAREV("ALEX KAREV",12),
	KATHERINE_HEIGL("KATHERINE HEIGL",13),
	OWEN_HUNT("OWEN HUNT",14),
	MIRANDA_BAILEY("MIRANDA BAILEY",15),
	RICHARD_WEBBER("RICHARD WEBBER",16);
	
	private String name = "";
	private int value;
    
    Chirurgien(String name, int value)
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
