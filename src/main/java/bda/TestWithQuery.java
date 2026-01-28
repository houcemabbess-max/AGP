package bda;

import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class TestWithQuery {

    public static void main(String[] args) throws Exception {

        // -------------------
        // 1) Partie TEXTUELLE (Lucene)
        // -------------------
        TopDocs topDocs = LuceneSearcher.search(
                Paths.get("data/index/site"),
                "volcan OR cascade",
                10
        );

        // stocker (id -> score) en mémoire
        Map<Integer, Float> scores = new LinkedHashMap<>();

        try (DirectoryReader reader =
                     DirectoryReader.open(FSDirectory.open(Paths.get("data/index/site")))) {

            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document d = reader.document(sd.doc);
                int id = Integer.parseInt(d.get("docId"));
                scores.put(id, sd.score);
            }
        }

        // -------------------
        // 2) Partie SQL
        // -------------------
        Connection cnx = DriverManager.getConnection(
                "jdbc:mysql://mysql-abbes.alwaysdata.net:3306/abbes_reunion_db2?useSSL=true&serverTimezone=UTC",
                "abbes",
                "agpgroupe7"
        );

        String sql = "select id, name from site where type='NATURE'";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        // -------------------
        // 3) Jointure SQL + TEXTE (WITH)
        // -------------------
        System.out.println("---- Résultat requête SQL + WITH ----");

        while (rs.next()) {
            int id = rs.getInt("id");
            if (scores.containsKey(id)) {
                System.out.println(
                        "id=" + id +
                        " name=" + rs.getString("name") +
                        " score=" + scores.get(id)
                );
            }
        }

        rs.close();
        st.close();
        cnx.close();
    }
}
