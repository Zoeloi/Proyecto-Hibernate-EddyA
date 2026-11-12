package com.appregistros.proyectohibernate;

import com.appregistros.proyectohibernate.database.JPAUtil;
import com.appregistros.proyectohibernate.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JpaEntityRegistrationTest {

    @Test
    void LibroDebeEstarRegistradoEnElPersistenceUnit() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EntityType<?> entityType = em.getMetamodel().entity(Libro.class);
            assertNotNull(entityType, "La entidad Libro no está registrada en el persistence unit");
        } finally {
            em.close();
        }
    }
}
