package bda;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer {

    public static void createIndex(Path textDir, Path indexDir) throws Exception {
        Files.createDirectories(indexDir);

        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(indexDir);

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (IndexWriter writer = new IndexWriter(directory, config);
             Stream<Path> files = Files.list(textDir)) {

            files.filter(Files::isRegularFile).forEach(path -> {
                try {
                    String docId = path.getFileName().toString();
                    byte[] bytes = Files.readAllBytes(path);
                    String content = new String(bytes, StandardCharsets.UTF_8);

                    Document doc = new Document();
                    doc.add(new StringField("docId", docId, Field.Store.YES));
                    doc.add(new TextField("content", content, Field.Store.NO));

                    writer.addDocument(doc);
                } catch (Exception e) {
                    throw new RuntimeException("Erreur indexation: " + path, e);
                }
            });
        }
    }
}
