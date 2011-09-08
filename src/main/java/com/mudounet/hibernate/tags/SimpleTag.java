package com.mudounet.hibernate.tags;



/**
* @hibernate.joined-subclass
*/
public class SimpleTag extends GenericTag implements java.io.Serializable {

    public SimpleTag() {
        super();
    }

    public SimpleTag(String key) {
        super(key);
    }
    
}