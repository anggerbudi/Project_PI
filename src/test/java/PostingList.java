import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostingList {

    private static final Logger logger = Logger.getLogger(PostingList.class.getName());

    private final Map<String, TermInfo> termInfoMap;
    private final Map<TermInfo, List<DocumentInfo>> invertedIndex;
    private int totalDocuments;

    public PostingList() {
        this.termInfoMap = new ConcurrentHashMap<>();
        this.invertedIndex = new TreeMap<>();
        this.totalDocuments = 0;
    }

    public void addTerms(String documentID, String[] tokens) {
        totalDocuments++;
        try {
            for (String token : tokens) {
                TermInfo termInfo = termInfoMap.computeIfAbsent(token, TermInfo::new);

                List<DocumentInfo> documentInfos = invertedIndex.computeIfAbsent(termInfo, _ -> new ArrayList<>());

                boolean found = false;
                for (DocumentInfo documentInfo : documentInfos) {
                    if (documentInfo.getDocumentID().equals(documentID)) {
                        documentInfo.incrementTF();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    documentInfos.add(new DocumentInfo(documentID));
                    termInfo.incrementDocumentFrequency();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding terms to posting list for document: " + documentID, e);
        }
    }

    public void calculateTfidf() {
        for (Map.Entry<TermInfo, List<DocumentInfo>> entry : invertedIndex.entrySet()) {
            TermInfo termInfo = entry.getKey();
            List<DocumentInfo> documentInfos = entry.getValue();
            double idf = Math.log((double) totalDocuments / termInfo.getDocumentFrequency());

            for (DocumentInfo documentInfo : documentInfos) {
                double tf = documentInfo.getTF();
                double tfidf = tf * idf;
                documentInfo.setTFIDF(tfidf);
            }
        }
    }

    public Map<TermInfo, List<DocumentInfo>> getInvertedIndex() {
        return invertedIndex;
    }
}