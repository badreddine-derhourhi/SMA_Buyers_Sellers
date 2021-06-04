package com.derhourhi.Other;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private double price;
    private boolean exist;


    public boolean isExist() {
        return this.exist;
    }

    public boolean getExist() {
        return this.exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }



    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Book() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", exist='" + isExist() + "'" +
            "}";
    }


}
