package com.example.ais_store.Models;

import javafx.beans.property.*;

public class Product {
    private SimpleIntegerProperty idProduct;
    private SimpleIntegerProperty idCategory;
    private SimpleStringProperty nameProduct;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty productNumber;
    private SimpleDoubleProperty sumPrice;

    public Product(int id_product, int id_category, String name_product, double price, int product_number, double sumPrice) {
        this.idProduct = new SimpleIntegerProperty(id_product);
        this.idCategory = new SimpleIntegerProperty(id_category);
        this.nameProduct = new SimpleStringProperty(name_product);
        this.price = new SimpleDoubleProperty(price);
        this.productNumber = new SimpleIntegerProperty(product_number);
        this.sumPrice = new SimpleDoubleProperty(sumPrice);
    }

    public final int getIdProduct() {
        return idProduct.get();
    }

    public final void setIdProduct(int id_product) {
        this.idProduct.set(id_product);
    }

    public final int getIdCategory() {
        return idCategory.get();
    }

    public final void setIdCategory(int id_category) {
        this.idProduct.set(id_category);
    }

    public final String getNameProduct() {
        return nameProduct.get();
    }

    public final void setNameProduct(String nameProduct) {
        this.nameProduct.set(nameProduct);
    }

    public final double getPrice() {
        return price.get();
    }

    public final void setPrice(double price) {
        this.price.set(price);
    }

    public final int getProductNumber() {
        return productNumber.get();
    }

    public final void setProductNumber(int productNumber) { this.productNumber.set(productNumber); }

    public final double getSumPrice() {
        return sumPrice.get();
    }

    public final void setSumPrice(double sumTotal) {
        this.sumPrice.set(sumTotal);
    }
}
