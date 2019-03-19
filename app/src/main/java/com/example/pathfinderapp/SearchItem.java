package com.example.pathfinderapp;

public class SearchItem {

    private String title;
    private String info;
    private int picture;

    public SearchItem(){

    }

    public SearchItem(String title, String infol, int picture) {
        this.title = title;
        this.info = infol;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String infol) {
        this.info = infol;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
