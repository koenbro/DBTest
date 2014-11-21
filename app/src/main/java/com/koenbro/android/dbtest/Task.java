package com.koenbro.android.dbtest;

/**
 * Data object Task
 */
public class Task {
    private long id;
    private String name;
    private int taskProject;

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(int taskProject) {
        this.taskProject = taskProject;
    }
}
