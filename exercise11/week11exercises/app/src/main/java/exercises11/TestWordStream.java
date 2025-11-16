//Exercise 10.?
//JSt vers Aug 20, 2025

package exercises11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestWordStream {
    public static void main(String[] args) {
  // 
  String filename = "./src/main/java/resources/english-words.txt";
    System.out.println(readWords(filename).count());

    readWords(filename)
        .limit(100)
        .forEach(System.out::println); // 11.2.2

    readWords(filename)
        .filter(w -> w.length() > 21)
        .forEach(System.out::println); // 11.2.3

    readWords(filename)
        .filter(w -> w.length() > 21)
        .limit(1)
        .forEach(System.out::println); // 11.2.4

    readWords(filename)
        .filter(TestWordStream::isPalindrome)
        .forEach(System.out::println); // 11.2.5

    readWords(filename)
        .parallel()
        .filter(TestWordStream::isPalindrome)
        .forEach(System.out::println); // 11.2.6

    System.out.println(
        readWordStream("https://staunstrups.dk/jst/english-words.txt")
            .count()); // 11.2.7

    IntSummaryStatistics stats = readWords(filename)
        .mapToInt(String::length)
        .summaryStatistics();

    System.out.printf("min=%d, max=%d, mean=%g%n",
        stats.getMin(), stats.getMax(), stats.getAverage()); // 11.2.8
  }

  public static Stream<String> readWords(String filename) {
    try {
      InputStream is = TestWordStream.class.getClassLoader().getResourceAsStream(filename);
      if (is != null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines(); // 11.2.1
      }
    } catch (Exception ignore) {
      // fall through to file-based loading
    }

    // Fallback: try to read from the filesystem using the provided path.
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines(); // 11.2.1
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  public static Stream<String> readWordStream(String source) {
    try {
      URL url = new URL(source);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

      return reader.lines(); // 11.2.1

    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  public static boolean isPalindrome(String s) {
    String lower = s.toLowerCase();
    for (int i = 0; i < lower.length(); i++) {
      char forward = lower.charAt(i);
      char backward = lower.charAt(lower.length() - i - 1);
      if (forward != backward)
        return false;
    }
    return true;
  }

  public static Map<Character, Integer> letters(String s) {
    Map<Character, Integer> res = new TreeMap<>();
    // TO DO: Implement properly
    return res;
  }
}
