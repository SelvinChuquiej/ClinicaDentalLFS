package org.selvinchuquiej.db;

import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import static javafx.application.Application.launch;

public class Conexion {

    private Connection conexion;
    private static Conexion instancia;

    private Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/DBClinicaDentalLFS2018354?useSSL=false", "root", "admin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public static Conexion getInstancia() {
        return instancia;
    }

    public static void setInstancia(Conexion instancia) {
        Conexion.instancia = instancia;
    }

}
