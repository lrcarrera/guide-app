package com.example.pathfinderapp.Models;

import java.util.HashMap;
import java.util.Map;

public class Language {

    private String   flag;
    private String   name;
    private String   code;
    private int   picture;
    private boolean added;
    private String id;

    public Language(String flag, String name) {
        this.flag = flag;
        this.name = name;
    }

    public Language(String id, String flag, String name, String code, int picture) {
        this.id = id;
        this.flag = flag;
        this.name = name;
        this.code = code;
        this.picture = picture;
        this.added = false;
    }

    public Language(String flag, String name, String code, int picture) {
        this.flag = flag;
        this.name = name;
        this.code = code;
        this.picture = picture;
        this.added = false;
    }

    public Map<String, Object> AddToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("flag", flag);
        data.put("name", name);
        data.put("code", code);
        data.put("picture", picture);
        data.put("added", added);
        data.put("id", id);
        return data;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
