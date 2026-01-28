package bda;

import java.nio.file.Paths;

public class TestBDeAPI {
    public static void main(String[] args) throws Exception {
        BDeEngine engine = new BDeEngine();

        engine.setTextTable("site", "id", Paths.get("data/text/site"));
        engine.setIndexDir(Paths.get("data/index/site"));

        engine.setDbConfig(
            "jdbc:mysql://mysql-abbes.alwaysdata.net:3306/abbes_reunion_db2?useSSL=true&serverTimezone=UTC",
            "abbes",
            "agpgroupe7"
        );

        // test SQL normal via l'API
        BDeResult r = engine.executeQuery("select id, name from island");
        r.init();
        while (r.next()) {
            System.out.println(r.getInt("id") + " " + r.getString("name"));
        }

        // test index
        engine.createIndex();
        System.out.println("✅ createIndex OK");
    }
}
