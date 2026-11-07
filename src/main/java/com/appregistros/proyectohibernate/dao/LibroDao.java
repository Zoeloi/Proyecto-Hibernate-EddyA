package com.appregistros.proyectohibernate.dao;

import com.appregistros.proyectohibernate.database.JPAUtil;
import com.appregistros.proyectohibernate.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class LibroDao {

    public void guardar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(libro);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(libro);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, id);
            if (libro != null) {
                tx.rollback();
            }
            em.remove(libro);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Libro buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public List<Libro> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Libro l ORDER BY l.id", Libro.class).getResultList();
        } finally {
            em.close();
        }
    }
    public boolean modificar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            //Opcion 1
           // em.merge(libro);
            //Opcion 2
            Libro libroBD = em.find(Libro.class, libro.getId());
            if (libroBD != null) {
                tx.rollback();
                return false;
            }
            libroBD.setTitulo(libro.getTitulo());
            libroBD.setAutor(libro.getAutor());
            libroBD.setAnio_publicacion(libro.getAnio_publicacion());
            libroBD.setPrecio(libro.getPrecio());
            tx.commit();

            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
}
