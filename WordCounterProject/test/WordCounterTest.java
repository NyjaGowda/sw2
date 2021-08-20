import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;

public class WordCounterTest {
    private static final String SEPARARTORS = " ,-.!?;:\n\t\r/";

    @Test
    public void test1() {
        Set<Character> sep = new Set1L<>();

        for (int i = 0; i < SEPARARTORS.length(); i++) {
            sep.add(SEPARARTORS.charAt(i));
        }
        String test = "Hello!";
        String res = WordCounter.nextWordOrSeparator(test, 0, sep);
        assertEquals(res, "Hello");
    }

}
