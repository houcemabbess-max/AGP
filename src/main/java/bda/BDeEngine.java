package bda;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class BDeEngine {

    // Configuration BDe
    private String tableName;  // T (ex: "site")
    private String keyAttr;    // clé de T (ex: "id")
    private Path textDir;      // R (ex: data/text/site)
    private Path indexDir;     // répertoire index Lucene (ex: data/index/site)

    // Configuration DB (alwaysdata)
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    // 1) Déclarer table T, clé, répertoire R
    public void setTextTable(String tableName, String keyAttr, Path textDir) {
        this.tableName = tableName;
        this.keyAttr = keyAttr;
        this.textDir = textDir;
    }

    // (utile) définir où stocker l'index Lucene
    public void setIndexDir(Path indexDir) {
        this.indexDir = indexDir;
    }

    // (utile) configurer la connexion DB
    public void setDbConfig(String url, String user, String pass) {
        this.dbUrl = url;
        this.dbUser = user;
        this.dbPass = pass;
    }

    // 2) Ajouter un texte t à la clé c : crée/écrase le fichier R/c
    public void addText(String c, String t) throws Exception {
        if (textDir == null) {
            throw new IllegalStateException("textDir (R) non configuré. Appelle setTextTable(...) d'abord.");
        }
        Files.createDirectories(textDir);
        Path file = textDir.resolve(c);

        Files.write(file, t.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // 3) Créer l'index Lucene sur disque à partir des fichiers de R
    public void createIndex() throws Exception {
        if (textDir == null) {
            throw new IllegalStateException("textDir (R) non configuré. Appelle setTextTable(...) d'abord.");
        }
        if (indexDir == null) {
            throw new IllegalStateException("indexDir non configuré. Appelle setIndexDir(...) d'abord.");
        }
        LuceneIndexer.createIndex(textDir, indexDir);
    }

    // 4) Exécuter une requête : SQL normal ou mixte (WITH)
    // Pour le moment : SQL normal seulement (WITH sera fait après)
    public BDeResult executeQuery(String query) throws Exception {
        QueryWithParser.Parts parts = QueryWithParser.parse(query);

        if (!parts.isMixed()) {
            return executeSql(parts.sql);
        }

        // WITH pas encore implémenté dans cette étape de l'API
        throw new UnsupportedOperationException(
            "Requête WITH détectée mais pas encore implémentée. SQL=" + parts.sql + " WITH=" + parts.text
        );
    }

    // Exécution SQL classique via JDBC
    private BDeResult executeSql(String sql) throws Exception {
        if (dbUrl == null) {
            throw new IllegalStateException("DB non configurée. Appelle setDbConfig(...) d'abord.");
        }

        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection cnx = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= n; i++) {
                    String col = md.getColumnLabel(i);
                    row.put(col, rs.getObject(i));
                }
                rows.add(row);
            }
        }

        return new BDeResult(rows);
    }

    // getters optionnels (pratique pour debug / rapport)
    public String getTableName() { return tableName; }
    public String getKeyAttr() { return keyAttr; }
    public Path getTextDir() { return textDir; }
    public Path getIndexDir() { return indexDir; }
}
