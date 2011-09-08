/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate.tags;

/**
 *
 * @hibernate.joined-subclass
 */
public class Actor extends Tag implements java.io.Serializable {

    private String name;

    /**
     * @hibernate.property
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
