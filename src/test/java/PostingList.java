import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostingList {

    private static final Logger logger = Logger.getLogger(PostingList.class.getName());

    private final Map<String, ObjectTermInfo> termInfoMap;
    private final Map<ObjectTermInfo, List<ObjectDocumentInfo>> invertedIndex;
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
                ObjectTermInfo objectTermInfo = termInfoMap.computeIfAbsent(token, ObjectTermInfo::new);

                List<ObjectDocumentInfo> objectDocumentInfos = invertedIndex.computeIfAbsent(objectTermInfo, _ -> new LinkedList<>());
                
                boolean found = false;
                for (ObjectDocumentInfo objectDocumentInfo : objectDocumentInfos) {
                    if (objectDocumentInfo.getDocumentID().equals(documentID)) {
                        objectDocumentInfo.incrementTF();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    objectDocumentInfos.add(new ObjectDocumentInfo(documentID));
                    objectTermInfo.incrementDocumentFrequency();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding terms to posting list for document: " + documentID, e);
        }
    }

    public void calculateTfidf() {
        double idf;
        for (Map.Entry<ObjectTermInfo, List<ObjectDocumentInfo>> entry : invertedIndex.entrySet()) {
            ObjectTermInfo objectTermInfo = entry.getKey();
            List<ObjectDocumentInfo> objectDocumentInfos = entry.getValue();
            idf = Math.log((double) totalDocuments / objectTermInfo.getDocumentFrequency());

            for (ObjectDocumentInfo objectDocumentInfo : objectDocumentInfos) {
                double tf = objectDocumentInfo.getTF();
                double tfidf = tf * idf;
                objectDocumentInfo.setTFIDF(tfidf);
            }
        }
    }

    public Map<ObjectTermInfo, List<ObjectDocumentInfo>> getInvertedIndex() {
        return invertedIndex;
    }
    
    public void sortDocumentIDs() {
        for (List<ObjectDocumentInfo> documentIDs : invertedIndex.values()) {
            documentIDs.sort(Comparator.comparing(ObjectDocumentInfo::getDocumentID));
        }
    }
}