package bda;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestLuceneSearch {

    public static void main(String[] args) throws Exception {
        Path textDir = Paths.get("data/text/site");
        Path indexDir = Paths.get("data/index/site");

        // (Optionnel mais conseillé) recréer l'index à chaque test
        LuceneIndexer.createIndex(textDir, indexDir);
        System.out.println("✅ Index prêt");

        // Test 1 : chercher volcan/cascade
        System.out.println("---- Query: volcan OR cascade ----");
        LuceneSearcher.searchAndPrint(indexDir, "volcan OR cascade", 10);

        // Test 2 : chercher randonnee
        System.out.println("---- Query: randonnee ----");
        LuceneSearcher.searchAndPrint(indexDir, "randonnee", 10);

        // Test 3 : chercher mot absent (doit renvoyer 0)
        System.out.println("---- Query: plongee ----");
        LuceneSearcher.searchAndPrint(indexDir, "plongee", 10);
    }
}
