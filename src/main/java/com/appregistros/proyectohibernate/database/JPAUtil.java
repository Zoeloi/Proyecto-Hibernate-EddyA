package com.appregistros.proyectohibernate.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory entityManagerFactory;

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static String getJdbcUrl() {
        return getEnvOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/bdbiblioteca");
    }

    public static String getJdbcUser() {
        return getEnvOrDefault("DB_USER", "andresmejia");
    }

    public static String getJdbcPassword() {
        return getEnvOrDefault("DB_PASSWORD", "amejia62");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), getJdbcUser(), getJdbcPassword());
    }

    public static synchronized void iniciar() {
        if (entityManagerFactory == null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("jakarta.persistence.jdbc.url", getJdbcUrl());
            properties.put("jakarta.persistence.jdbc.user", getJdbcUser());
            properties.put("jakarta.persistence.jdbc.password", getJdbcPassword());
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.show_sql", "false");

            entityManagerFactory = Persistence.createEntityManagerFactory("bibliotecaPU", properties);
        }
    }

    public static EntityManager getEntityManager() {
        iniciar();
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void cerrar() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
