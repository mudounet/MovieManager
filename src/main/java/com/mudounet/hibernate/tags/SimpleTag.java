package com.mudounet.hibernate.tags;



/**
* @hibernate.subclass
* discriminator-value="S"
*/
public class SimpleTag extends GenericTag implements java.io.Serializable {

    public SimpleTag() {
        super();
    }

    public SimpleTag(String key) {
        super(key);
    }
    
}