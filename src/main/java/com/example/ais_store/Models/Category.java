package com.example.ais_store.Models;

import com.example.ais_store.DB.DbUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Category {
    private SimpleIntegerProperty idCategory;
    private SimpleStringProperty name;
    private SimpleDoubleProperty sumPrice;

    public Category(int id_category, String name) {
        this.idCategory = new SimpleIntegerProperty(id_category);
        this.name = new SimpleStringProperty(name);
        this.sumPrice = new SimpleDoubleProperty(DbUtils.getTotalSumOfProductsInCategory(id_category));
    }

    public int getIdCategory() {
        return idCategory.get();
    }

    public void setIdCategory(int id_category) {
        this.idCategory.set(id_category);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getSumPrice() {
        return sumPrice.get();
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice.set(sumPrice);
    }
}
