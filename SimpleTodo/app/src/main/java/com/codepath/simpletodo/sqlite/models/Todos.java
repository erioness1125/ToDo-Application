package com.codepath.simpletodo.sqlite.models;

public class Todos {
    public int id;
    public String value;
    public long dueDate;

    public Todos(int id, String value, long dueDate) {
        this.id = id;
        this.value = value;
        this.dueDate = dueDate;
    }
}
