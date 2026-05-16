package com.bancoregio.sgisi.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/** Singleton de conexiones JDBC para SGISI. */
public final class ConexionDB {
    private static ConexionDB instance;
    private final String url; private final String user; private final String password;
    private ConexionDB(){ Properties p=new Properties(); try(InputStream in=getClass().getClassLoader().getResourceAsStream("application.properties")){ if(in!=null){p.load(in);} } catch(Exception ignored){}
        this.url=System.getenv().getOrDefault("SGISI_DB_URL", p.getProperty("db.url","jdbc:mysql://localhost:3306/sgisi"));
        this.user=System.getenv().getOrDefault("SGISI_DB_USER", p.getProperty("db.user","sgisi_app"));
        this.password=System.getenv().getOrDefault("SGISI_DB_PASSWORD", p.getProperty("db.password","sgisi_app_pass")); }
    /** Devuelve singleton. */
    public static synchronized ConexionDB getInstance(){ if(instance==null){ instance=new ConexionDB(); } return instance; }
    /** Abre y devuelve conexión nueva. */
    public Connection getConnection() throws java.sql.SQLException { return DriverManager.getConnection(url,user,password); }
}
