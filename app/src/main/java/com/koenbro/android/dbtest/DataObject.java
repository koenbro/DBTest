package com.koenbro.android.dbtest;

/**
 * Abstract data object.
 */
public abstract class DataObject {
    private long id;
    private String name;

    public DataObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
