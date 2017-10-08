package com.stc07.gubarkovag.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerPostgreSQL implements IConnectionManager {
    private static final ConnectionManagerPostgreSQL INSTANCE = new ConnectionManagerPostgreSQL();

    public static ConnectionManagerPostgreSQL getInstance() {
        return INSTANCE;
    }

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    private ConnectionManagerPostgreSQL() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/bookmanager"
                    , "postgres", "postgres");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
