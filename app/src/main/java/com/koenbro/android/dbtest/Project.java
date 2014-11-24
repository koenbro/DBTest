package com.koenbro.android.dbtest;

/**
 * Data object Project. Inherits {@code long id} and {@code String name} from DataObject.
 */
public class Project extends DataObject {
    private int completed; // percent completed

    public Project() {
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
