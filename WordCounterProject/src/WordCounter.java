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
 * Program reads from an input file, goes through the text and stores individual
 * words. It later counts how many times a word has occurred and makes a table
 * of the word and its subsequent count using HTML script.
 *
 * @author Nyja Gowda Date:-08/23/2021
 *
 */

public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    /**
     * a string of separators that will be used to extract words from text.
     */
    private static final String SEPARARTORS = " ,-.!?;:\n\t\r/#$&@%{}[]=^|<>+'";

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

            /*
             * used compareToIgnoreCase to make the list solely alphabetical
             * order. Capital and small versions of the same word will be
             * treated as two separate words (ex. it/It).
             */
        }
    }

    /**
     * Returns an individual word(string) from a long string, which is separated
     * from the string by the presence of separators like ! .,/ that have been
     * stored in a set.
     *
     * @param text
     *            text string from where to get word/separator string
     * @param position
     *            position from where to start looking for separator in the text
     *            string
     * @param separators
     *            set of characters that contains all separators
     * @return string of either word/separator whichever found first from
     *         text.substring(position,index)
     *
     * @requires position!>text.length() and separators!=NULL
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */

    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int index = position;
        boolean sep = separators.contains(text.charAt(position));
        //assigning initial value
        while (index < text.length()
                && sep == separators.contains(text.charAt(index))) {
            /*
             * keeping the assignment as condition in while for sep as it avoids
             * for checking if and else and assigning sep again later
             */
            index++;
        }
        return text.substring(position, index);
        //returns string one index before the occurrence of the separator

    }

    /**
     * Writes to the given outFile the HTML script for all the words extracted
     * from the text paragraph and their count as calculated and stored in the
     * map as a table.
     *
     * @param map
     *            map that contains all the words and the count of the number of
     *            times they appear in the document.
     * @param words
     *            queue of all words extracted from the text paragraph.
     * @param outFile
     *            the name of the file to which the HTML script is being written
     * @param inFile
     *            the name of the file that contains the text and is mentioned
     *            in the heading and title.
     *
     * @requires words!=NULL and maps!=NULL
     * @ensures HTML script for a table of words is written to outFile
     */
    public static void createOpen(Map<String, Integer> map, Queue<String> words,
            String outFile, String inFile) {
        Queue<String> temp = words.newInstance();
        temp.transferFrom(words);
        /*
         * removing words from queue later so having a temporary queue and
         * adding back strings to original queue will restore words.
         */
        SimpleWriter out = new SimpleWriter1L(outFile);
        out.println();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted in " + inFile + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Words Counted in " + inFile + "</h2>");
        out.println("<hr>");
        //table
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");

        Map<String, Integer> mapTemp = map.newInstance();
        mapTemp.transferFrom(map);

        while (temp.length() > 0) {

            String s = temp.dequeue();
            words.enqueue(s);
            if (mapTemp.hasKey(s)) {
                out.println("<tr>");
                Map.Pair<String, Integer> p = mapTemp.remove(s);
                out.println("<td>" + p.key() + "</td>");
                out.println("<td>" + p.value() + "</td>");
                out.println("</tr>");
                map.add(p.key(), p.value());
            }
            /*
             * To restore map, using mapTemp so that I can check if the word
             * from mapTemp is present in queue temp, if it is then we add it to
             * map and write it to outFile.
             */
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * Receives a line of text(testStr) from getWords, which the method extracts
     * individual words/separator string from using nextWordOrseparator and adds
     * only the words to a queue wordList. It also makes a character set from
     * the string SEPARATORS.
     *
     * @param testStr
     *            a line of text from the input file paragraph.
     * @param wordList
     * @ensures wordList.enqueue(all the words in fileName devoid of separators)
     *
     *
     */
    public static void wordSeparator(String testStr, Queue<String> wordList) {
        Set<Character> sep = new Set1L<>();

        for (int i = 0; i < SEPARARTORS.length(); i++) {
            sep.add(SEPARARTORS.charAt(i));
            //converting SEPARATORS into a character set for ease in checking elements
        }
        int position = 0;
        while (position < testStr.length()) {
            String token = nextWordOrSeparator(testStr, position, sep);
            if (!sep.contains(token.charAt(0))) {
                wordList.enqueue(token);
                /*
                 * if the first index of token does not have a separator then it
                 * is added to queue
                 */
            }
            position += token.length();
            //position updated to move on to next word in the string
        }

    }

    /**
     * Counts how many times a particular word occurs in a queue and then stores
     * the word and its count in a map.
     *
     * @param map
     *            map that contains all the words and the count of the number of
     *            times they appear in the document.
     * @param wordList
     *            queue of all alphabetically sorted words extracted from the
     *            text paragraph.
     * @ensures all the words are present in alphabetical order along with their
     *          count in map
     *
     *          if word matches a key in map then make value count+1 else add
     *          new pair of word and its count
     */
    public static void wordCount(Map<String, Integer> map,
            Queue<String> wordList) {
        for (String s : wordList) { //parsing all words present in queue wordList
            if (map.hasKey(s)) {
                //uppercase and lowercase of the same word are not the same
                map.replaceValue(s, map.value(s) + 1); //count updated
            } else {
                map.add(s, 1); //new map element created
            }
        }
    }

    /**
     * Reads from input file line by line and sends each line to wordSeparator
     * to generate and return a queue of words from input file.
     *
     * @param fileName
     *            the input file which contains text
     * @return wordList queue of all words extracted from the text paragraph.
     *
     * @ensures inFile read till last line
     *
     *          wordList = <words from testStr devoid of separators>
     */
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
        inFile.close(); //closes input file
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
        //q now contains all the word in the text present in input file
        Comparator<String> ci = new StringLT();
        q.sort(ci); //sorting alphabetically
        Map<String, Integer> mapWord = new Map1L<>();
        wordCount(mapWord, q);
        createOpen(mapWord, q, outFile, inFile);//calling method to write to output file
        in.close();
        out.close();
    }

}
