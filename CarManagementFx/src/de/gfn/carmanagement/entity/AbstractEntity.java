package de.gfn.carmanagement.entity;

/**
 *
 * @author tlubowiecki
 */
public abstract class AbstractEntity {
    
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
