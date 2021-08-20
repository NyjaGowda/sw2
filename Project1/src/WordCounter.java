import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class WordCounter {
    private static final String SEPARARTORS = " ,-.!?;:\n\t\r/";

    /**
     *
     * String comparator written to sort strings in alphabetical order.
     *
     */
    public static class StringLT implements Comparator<String> {
        /**
         * Sorts a queue in alphabetical order.
         *
         * @param s1
         *            first string to be compared.
         * @param s2
         *            second string to be compared.
         */
        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2); //returns -1/1/0 based on comparison
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int index = position;
        boolean sep = separators.contains(text.charAt(position)); //word sep
        while (index < text.length()
                && sep == separators.contains(text.charAt(index))) {
            index++;
        }
        return text.substring(position, index);

    }

    public static void createOpen(Map<String, Integer> map, Queue<String> words,
            String outFile) {
        Queue<String> temp = words.newInstance();
        temp.transferFrom(words);
        SimpleWriter out = new SimpleWriter1L(outFile);
        out.print("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + outFile + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Words counted in data/" + outFile + "</h1>");
        out.println("<hr>");
        //table
        out.println("<style>");
        out.println("table, th, td " + "\n" + "{ border: 1px solid black }");
        out.println("</style>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Word</th>");
        out.println("<th>Count</th>");
        out.println("</tr>");

        Map<String, Integer> mapTemp = map.newInstance();
        mapTemp.transferFrom(map);

        while (temp.length() > 0) {

            String s = temp.dequeue();
            words.enqueue(s);
            if (mapTemp.hasKey(s)) {
                out.println("<tr>");
                Map.Pair<String, Integer> p = mapTemp.remove(s);
                out.println("<td> " + p.key() + " </td>");
                out.println("<td> " + p.value() + " </td>");
                out.println("</tr>");
                map.add(p.key(), p.value());
            }
            /*
             * I'm using temp because after removal from temp im adding the pair
             * back to original map so that each pair doesnt get repeated all
             * the number of times s is in the queue. If tempmap doesnt contain
             * the s pair another time even if tis there like 7 times we're fine
             * it moves on to the next pair.
             */
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * Put a short phrase describing the static method myMethod here.
     */
    private static void wordSeparator(String testStr, Queue<String> wordList) {
        Set<Character> sep = new Set1L<>();

        for (int i = 0; i < SEPARARTORS.length(); i++) {
            sep.add(SEPARARTORS.charAt(i));
        }
        int position = 0;
        while (position < testStr.length()) {
            String token = nextWordOrSeparator(testStr, position, sep);
            if (!sep.contains(token.charAt(0))) {
                wordList.enqueue(token);
            }
            position += token.length();
        }

    }

    private static void wordCount(Map<String, Integer> map,
            Queue<String> wordList) {
        for (String s : wordList) {
            if (map.hasKey(s)) {
                map.replaceValue(s, map.value(s) + 1);
            } else {
                map.add(s, 1);
            }
        }
    }

    public static Queue<String> getWords(String fileName) {
        assert fileName != null : "Violation of: fileName is not null";
        Queue<String> wordlist = new Queue1L<>();
        SimpleReader inFile = new SimpleReader1L(fileName);
        /*
         * continuing reading till the end of file
         */
        while (!inFile.atEOS()) {
            String word = inFile.nextLine();
            wordSeparator(word, wordlist);
        }
        inFile.close();
        return wordlist;

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.print("Enter input file name:- ");
        String inFile = in.nextLine();
        out.print("Enter output file name:- ");
        String outFile = in.nextLine();
        Queue<String> q = getWords(inFile);
        Comparator<String> ci = new StringLT();
        q.sort(ci); //sorting alphabetically
        Map<String, Integer> mapWord = new Map1L<>();
        wordCount(mapWord, q);
        createOpen(mapWord, q, outFile);
        out.println(q.toString());
        out.println(mapWord.toString());
        in.close();
        out.close();
    }

}
