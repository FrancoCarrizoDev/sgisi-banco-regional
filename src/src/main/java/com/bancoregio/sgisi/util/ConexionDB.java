package com.bancoregio.sgisi.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Punto central de configuración de conexiones JDBC.
 *
 * Aplica el patrón Singleton para cargar una sola vez los parámetros de conexión
 * y exponerlos al resto de la aplicación. Cada llamada a getConnection abre una
 * conexión nueva, lo que permite que los servicios manejen transacciones propias
 * sin compartir estado entre operaciones.
 */
public final class ConexionDB {
    private static ConexionDB instance;
    private final String url;
    private final String user;
    private final String password;

    private ConexionDB() {
        Properties p = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (Exception ignored) {
        }
        // Las variables de entorno tienen prioridad para facilitar pruebas o
        // despliegues sin tocar el archivo application.properties.
        this.url = System.getenv().getOrDefault("SGISI_DB_URL", p.getProperty("db.url", "jdbc:mysql://localhost:3306/sgisi"));
        this.user = System.getenv().getOrDefault("SGISI_DB_USER", p.getProperty("db.user", "sgisi_app"));
        this.password = System.getenv().getOrDefault("SGISI_DB_PASSWORD", p.getProperty("db.password", "sgisi_app_pass"));
    }

    /**
     * Acceso sincronizado para inicializar la configuración una sola vez, incluso
     * si la aplicación pidiera la conexión desde más de un hilo Swing.
     */
    public static synchronized ConexionDB getInstance() {
        if (instance == null) {
            instance = new ConexionDB();
        }
        return instance;
    }

    /**
     * Abre una conexión nueva para que cada operación pueda controlar su propia
     * transacción y cerrar recursos con try-with-resources.
     */
    public Connection getConnection() throws java.sql.SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
