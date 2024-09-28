package lmrkonjic.leapwisehiringtask.services;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LuceneExample {

    private static final float SIMILARITY_THRESHOLD = 0.7f;


    public List<String> findSimilarStrings(String originalWord, List<String> stringList) throws Exception {
        List<String> similarStrings = new ArrayList<>();
        Directory indexDirectory = new ByteBuffersDirectory();  // In-memory directory
        StandardAnalyzer analyzer = new StandardAnalyzer();     // Analyzer for tokenizing the strings

        // Indexing the strings
        try (IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
            for (String text : stringList) {
                indexString(writer, text);
            }
        }

        // Searching for similar strings
        try (IndexReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", analyzer);

            // Parse the original word into a query
            Query query = parser.parse(QueryParser.escape(originalWord));

            // Perform the search, limiting to 100 results
            TopDocs topDocs = searcher.search(query, 100);

            // Process the search results
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                float score = scoreDoc.score;

                // Only include results above the similarity threshold
                if (score > SIMILARITY_THRESHOLD) {
                    similarStrings.add(doc.get("content"));
                }
            }
        }

        return similarStrings;
    }

    // Index a string by adding it as a document
    private void indexString(IndexWriter writer, String text) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("content", text, TextField.Store.YES));
        writer.addDocument(doc);
    }
}
