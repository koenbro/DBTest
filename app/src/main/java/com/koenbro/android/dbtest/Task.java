package com.koenbro.android.dbtest;

/**
 * Data object Task. Inherits {@code long id} and {@code String name} from DataObject.
 */
public class Task extends DataObject {
    private int taskProject;

    public Task() {
    }

    public int getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(int taskProject) {
        this.taskProject = taskProject;
    }
}
