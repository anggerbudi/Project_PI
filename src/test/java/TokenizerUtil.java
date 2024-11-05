import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TokenizerUtil {

    private static final Logger logger = Logger.getLogger(TokenizerUtil.class.getName());
    private final Tokenizer tokenizer;
    private final PorterStemmer stemmer;
    private final Set<String> stopwords;


    public TokenizerUtil(String modelPath, Set<String> stopwords) throws IOException {
        try (InputStream modelIn = new FileInputStream(modelPath)) {
            TokenizerModel model = new TokenizerModel(modelIn);
            this.tokenizer = new TokenizerME(model);
            this.stemmer = new PorterStemmer();
            this.stopwords = stopwords;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading tokenizer model: " + modelPath, e);
            throw e;
        }
    }

    public String[] tokenize(String text) {
        String[] tokens = tokenizer.tokenize(text);
        return Arrays.stream(tokens)
                .map(String::toLowerCase)
                .filter(token -> !stopwords.contains(token))
                .map(stemmer::stem)
                .toArray(String[]::new);
    }

    public void processDocuments(String folderPath, PostingList postingList) {
        ReadFile readFile = new ReadFile();
        Map<String, String> fileContents = readFile.readFiles(folderPath);

        if (fileContents.isEmpty()) {
            logger.log(Level.INFO, "No files found in the folder.");
            return;
        }

        for (Entry<String, String> entry : fileContents.entrySet()) {
            String fileName = entry.getKey();
            if (fileName.endsWith(".txt")) {
                fileName = fileName.substring(0, fileName.length() - 4);
            }
            String content = entry.getValue();
            String[] tokens = tokenize(content);
            postingList.addTerms(fileName, tokens);
        }
    }
}
