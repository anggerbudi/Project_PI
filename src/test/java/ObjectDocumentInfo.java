import java.text.DecimalFormat;

public class ObjectDocumentInfo {
    
    private final String documentID;
    private int TF;
    private double TFIDF;
    private static final DecimalFormat df = new DecimalFormat("0.0000");
    
    public ObjectDocumentInfo(String documentID) {
        this.documentID = documentID;
        this.TF = 1;
        this.TFIDF = 0.0;
    }
    
    public String getDocumentID() {
        return documentID;
    }
    
    public int getTF() {
        return TF;
    }
    
    public void incrementTF() {
        TF++;
    }
    
    public double getTFIDF() {
        return TFIDF;
    }
    
    public void setTFIDF(double tfidf) {
        this.TFIDF = tfidf;
    }
    
    @Override
    public String toString() {
        return documentID + " (tfidf: " + df.format(TFIDF) + ")";
    }
}
