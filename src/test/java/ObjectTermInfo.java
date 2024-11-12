public class ObjectTermInfo implements Comparable<ObjectTermInfo> {
    
    private final String term;
    private int documentFrequency;
    
    public ObjectTermInfo(String term) {
        this.term = term;
        this.documentFrequency = 0;
    }
    
    public String getTerm() {
        return term;
    }

    public int getDocumentFrequency() {
        return documentFrequency;
    }
    
    public void incrementDocumentFrequency() {
        documentFrequency++;
    }
    
    @Override
    public int compareTo(ObjectTermInfo other) {
        return term.compareTo(other.term);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ObjectTermInfo other = (ObjectTermInfo) obj;
        return term.equals(other.term);
    }
    
    @Override
    public int hashCode() {
        return term.hashCode();
    }
}
