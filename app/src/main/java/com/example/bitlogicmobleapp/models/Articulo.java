package com.example.bitlogicmobleapp.models;

public class Articulo {
    private int id;
    private String nombre;
    private String categoria;
    private int stock;

    // Jackson usa este constructor para crear el objeto al leer el JSON.
    public Articulo() {
    }

    public Articulo(int id, String nombre, String categoria, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
