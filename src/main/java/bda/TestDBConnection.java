package bda;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {

    private static final String URL =
        "jdbc:mysql://mysql-abbes.alwaysdata.net:3306/abbes_reunion_db2"
        + "?useSSL=true&serverTimezone=UTC";

    private static final String USER = "abbes";
    private static final String PASS = "agpgroupe7";

    public static void main(String[] args) {
        try (Connection cnx = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✅ Connexion à la base alwaysdata réussie !");
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion");
            e.printStackTrace();
        }
    }
}
