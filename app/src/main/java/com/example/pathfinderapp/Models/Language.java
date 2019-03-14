package com.example.pathfinderapp.Models;

public class Language {

    private String flag;
    private String name;

    public Language(String flag, String name) {
        this.flag = flag;
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
