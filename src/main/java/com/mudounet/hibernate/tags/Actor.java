package com.mudounet.hibernate.tags;

/**
* @hibernate.joined-subclass
*/
public class Actor extends GenericTag implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

     private String name;

    /**
    * @hibernate.property
    * @return Nom de l'acteur/actrive
    */
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}


