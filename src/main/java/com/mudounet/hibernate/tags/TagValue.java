package com.mudounet.hibernate.tags;

/**
* @hibernate.joined-subclass
*/
public class TagValue extends GenericTag implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


     /**
      * Value of this tag
     */
     private String value;

     public TagValue() {
    }


    public TagValue(String key) {
        super(key);        
    }
   
    /**
    * @hibernate.property
    * @return get value of tag
    */
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }




}


