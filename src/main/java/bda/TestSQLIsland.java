package bda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestSQLIsland {

    private static final String URL =
        "jdbc:mysql://mysql-abbes.alwaysdata.net:3306/abbes_reunion_db2"
        + "?useSSL=true&serverTimezone=UTC";

    private static final String USER = "abbes";
    private static final String PASS = "agpgroupe7";

    public static void main(String[] args) {
        try (
            Connection cnx = DriverManager.getConnection(URL, USER, PASS);
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM island");
        ) {
            System.out.println("---- Résultat SELECT id, name FROM island ----");
            while (rs.next()) {
                System.out.println(
                    "id=" + rs.getInt("id") +
                    " name=" + rs.getString("name")
                );
            }
            System.out.println("✅ Test SQL island réussi");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test SQL");
            e.printStackTrace();
        }
    }
}
