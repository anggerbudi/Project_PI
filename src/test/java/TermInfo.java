public class TermInfo implements Comparable<TermInfo> {
    
    private final String term;
    private int documentFrequency;
    
    public TermInfo(String term) {
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
    public int compareTo(TermInfo other) {
        return term.compareTo(other.term);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TermInfo other = (TermInfo) obj;
        return term.equals(other.term);
    }
    
    @Override
    public int hashCode() {
        return term.hashCode();
    }
}
