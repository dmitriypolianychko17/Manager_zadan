package com.dmytro.manager_zadan.adapter;

public class Zadania {
    private int id;
    private int action;
    private String date;
    private String desc;
    private String backColor;

    public Zadania(){

    }

    public Zadania(String date, String desc, String backColor, int action){
        this.action = action;
        this.date = date;
        this.desc = desc;
        this.backColor = backColor;
    }

    public Zadania(int id, String date, String desc, String backColor, int action) {
        this.id = id;
        this.action = action;
        this.date = date;
        this.desc = desc;
        this.backColor = backColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
}
