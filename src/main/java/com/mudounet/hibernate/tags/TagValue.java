package com.mudounet.hibernate.tags;

/**
* @hibernate.joined-subclass
*/
public class TagValue extends Tag implements java.io.Serializable {


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
    */
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }




}


