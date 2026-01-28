package bda;

import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearcher {

    // Méthode utilisée par le WITH
    public static TopDocs search(Path indexDir, String queryText, int topK) throws Exception {

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(queryText);

        try (DirectoryReader reader = DirectoryReader.open(FSDirectory.open(indexDir))) {
            IndexSearcher searcher = new IndexSearcher(reader);
            return searcher.search(query, topK);
        }
    }

    // (Optionnel) méthode d'affichage simple
    public static void searchAndPrint(Path indexDir, String queryText, int topK) throws Exception {
        TopDocs topDocs = search(indexDir, queryText, topK);
        System.out.println("Nombre de résultats: " + topDocs.totalHits);
    }
}

