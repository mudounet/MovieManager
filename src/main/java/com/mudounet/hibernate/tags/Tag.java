package com.mudounet.hibernate.tags;



/**
* @hibernate.joined-subclass
*/
public class Tag extends GenericTag implements java.io.Serializable {

    public Tag() {
        super();
    }

    public Tag(String key) {
        super(key);
    }
    
}