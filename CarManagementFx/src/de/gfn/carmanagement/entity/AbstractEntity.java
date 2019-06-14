package de.gfn.carmanagement.entity;

//TODO Gerneric ID
public abstract class AbstractEntity {
    
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
