import com.panayotis.gnuplot.JavaPlot;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Exercise1 {
    private static Map<String, Integer> stringIntegerHashMap = new HashMap<>();

    private static ArrayList<String> cleanedWords = new ArrayList<>();
    private static ArrayList<String> words;

    public static void main(String [] args) {
        JavaPlot p = new JavaPlot();

        p.addPlot("sin(x)");

        p.plot();


        String content = getFile("/Users/tgdflto1/Google Drive/FH/7. Semester/Data Science/Übungen/pg1524.txt");
        words = new ArrayList<>(Arrays.asList(content.split(" ")));
        words.forEach((word) -> cleanedWords.add(tokenize(word)));

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
        try {
            return new String(Files.readAllBytes(
                    Paths.get(path))).replaceAll("\\r|\\n","");
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
                replaceAll("\\[", "").trim();
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
