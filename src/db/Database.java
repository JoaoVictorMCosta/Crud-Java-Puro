// src/db/Database.java
package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {

    public static Connection getConnection() {
        try {
            // Tenta ler as propriedades das variaveis de ambiente primeiro
            String dbUrlEnv = System.getenv("DB_URL");
            String dbUserEnv = System.getenv("DB_USER");
            String dbPasswordEnv = System.getenv("DB_PASSWORD");

            if (dbUrlEnv != null && dbUserEnv != null) { // Password pode ser null
                Properties propsEnv = new Properties();
                propsEnv.setProperty("user", dbUserEnv);
                if (dbPasswordEnv != null) {
                    propsEnv.setProperty("password", dbPasswordEnv);
                }
                // Adicione sslmode e channel_binding se necessarios via env ou hardcode aqui
                if (!dbUrlEnv.contains("sslmode=")) {
                    dbUrlEnv += "?sslmode=require&channel_binding=require";
                }

                System.out.println("[INFO] Conectando via variaveis de ambiente...");
                return DriverManager.getConnection(dbUrlEnv, propsEnv);
            } else {
                // Se as variaveis de ambiente nao estiverem definidas, carrega do db.properties
                System.out.println("[INFO] Conectando via db.properties...");
                Properties propsFile = loadProperties();
                String dburlFile = propsFile.getProperty("dburl");
                return DriverManager.getConnection(dburlFile, propsFile);
            }
        } catch (SQLException e) {
            throw new DbException("Error connecting to database: " + e.getMessage());
        }
    }

    // ... (metodos closeStatement, closeResultSet, loadProperties permanecem os mesmos)

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            // Se db.properties nao for encontrado e nao ha variaveis de ambiente, lanca erro
            throw new DbException("db.properties file not found or could not be loaded: " + e.getMessage());
        }
    }
}