package com.example.mayank.rooms.Front;

/**
 * Created by mayan on 9/28/2017.
 */

public class eventContainer {

    private String title;
    private String description;

    public eventContainer(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
