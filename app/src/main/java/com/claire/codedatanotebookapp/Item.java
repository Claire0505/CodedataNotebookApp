package com.claire.codedatanotebookapp;

import java.io.Serializable;

public class Item implements Serializable {

    private String title;

    public Item(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
