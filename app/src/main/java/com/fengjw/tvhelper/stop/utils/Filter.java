package com.fengjw.tvhelper.stop.utils;

/**
 * Created by fengjw on 2017/10/19.
 */

public class Filter {

    private String name;

    public Filter(){
        this.name = "";
    }

    public Filter(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.name;
    }

}
