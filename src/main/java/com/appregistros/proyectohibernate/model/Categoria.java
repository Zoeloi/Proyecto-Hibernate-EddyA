package com.appregistros.proyectohibernate.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria", schema = "catalogos")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(Long id, String nombre, String descripcion) {
this.id = id;
this.nombre = nombre;
this.descripcion = descripcion;
    }

    public Long getId() {
return id;
    }

    public void setId(Long id) {
this.id = id;
    }

    public String getNombre() {
return nombre;
    }

    public void setNombre(String nombre) {
this.nombre = nombre;
    }

    public String getDescripcion() {
return descripcion;
    }

    public void setDescripcion(String descripcion) {
this.descripcion = descripcion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
