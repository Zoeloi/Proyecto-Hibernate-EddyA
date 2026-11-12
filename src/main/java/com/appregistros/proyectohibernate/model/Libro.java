package com.appregistros.proyectohibernate.model;

import jakarta.persistence.*;
@Entity
@Table(name = "libro", schema = "catalogos")
public class Libro {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "titulo", nullable = false, length = 150)
        private String titulo;
        @Column(name = "autor", nullable = false, length = 120)
        private String autor;
        @Column(name = "anio_publicacion",nullable = false)
        private int anio_publicacion;
        @Column(name = "precio", nullable = false, columnDefinition = "numeric(10,2)")
        private double precio;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "categoria_id", nullable = false)
        private Categoria categoria;

    public Libro() {
    }

    public Libro(long id, String titulo, String autor, int anio_publicacion, double precio) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anio_publicacion = anio_publicacion;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio_publicacion() {
        return anio_publicacion;
    }

    public void setAnio_publicacion(int anio_publicacion) {
        this.anio_publicacion = anio_publicacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
