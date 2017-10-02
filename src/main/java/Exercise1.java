import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Exercise1 {
    private static Map<String, Integer> stringIntegerHashMap = new HashMap<>();

    private static ArrayList<String> cleanedWords = new ArrayList<>();
    private static ArrayList<String> words;

    public static void main(String [] args) {
        String content = getFile("pg1524.txt").replaceAll("\\r|\\n","");
        words = new ArrayList<>(Arrays.asList(content.split(" ")));
        List<String> stopWords = Arrays.asList(getFile("stopwords_en.txt").split("\\r|\\n"));
        words.forEach((word) -> {
            String tokenizedWord = tokenize(word);
            if(stopWords.indexOf(tokenizedWord) == -1 && tokenizedWord.length() > 0){
                cleanedWords.add(tokenizedWord);
            }
        });
        stringIntegerHashMap = frequency(stringIntegerHashMap, cleanedWords);
        stringIntegerHashMap = sort(stringIntegerHashMap);
        System.out.println(stringIntegerHashMap);
        writeToPath("/tmp");
    }

    private static void writeToPath(String path) {
        try{
            PrintWriter writer = new PrintWriter(path+"/the-file-name.txt", "UTF-8");
            writer.println("# X Y");
            stringIntegerHashMap.forEach((a,b) -> writer.println(a + " " +b));
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    private static String getFile(String path){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            File file = new File(classLoader.getResource(path).getFile());
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String tokenize(String content) {
        return  content.replaceAll("\\?", "").
                replaceAll("--", "").
                replaceAll(",", "").
                replaceAll("!", "").
                replaceAll(";", "").
                replaceAll(":", "").
                replaceAll("\\*", "").
                replaceAll("\\.", "").
                replaceAll("]", "").
                replaceAll("\\[", "").trim().toLowerCase();
    }

    private static Map<String, Integer> sort(Map<String, Integer> hash) {
        return hash.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private static Map<String, Integer> frequency(Map hash, List<String> words){
        for (String word : words) {
            hash.merge(word, 1, (a, b) -> (Integer) a + (Integer) b);
        }
        return hash;
    }
}
