package com.koenbro.android.dbtest;

/**
 * Data object Project
 */
public class Project {
    private long id;
    private String name;
    private int completed; // percent completed

    public Project() {
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
