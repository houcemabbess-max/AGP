package bda;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBDA {

    public static void main(String[] args) throws Exception {

        Path textDir = Paths.get("data/text/site");
        Path indexDir = Paths.get("data/index/site");

        // 1️⃣ Création de l’index Lucene
        LuceneIndexer.createIndex(textDir, indexDir);
        System.out.println("✅ Index Lucene créé");

    }
}
