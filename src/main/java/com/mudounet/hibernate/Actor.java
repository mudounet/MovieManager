/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.hibernate;

/**
 *
 * @hibernate.class
 */
public class Actor implements java.io.Serializable {

    private Long id;
    private String name;

    /**
     * @hibernate.id
     * generator-class="native"
     */
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
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
