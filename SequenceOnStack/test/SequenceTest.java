import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.sequence.Sequence;

/**
 * JUnit test fixture for {@code Sequence<String>}'s constructor and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class SequenceTest {

    /**
     * Invokes the appropriate {@code Sequence} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new sequence
     * @ensures constructorTest = <>
     */
    protected abstract Sequence<String> constructorTest();

    /**
     * Invokes the appropriate {@code Sequence} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new sequence
     * @ensures constructorRef = <>
     */
    protected abstract Sequence<String> constructorRef();

    /**
     *
     * Creates and returns a {@code Sequence<String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the entries for the sequence
     * @return the constructed sequence
     * @ensures createFromArgsTest = [entries in args]
     */
    private Sequence<String> createFromArgsTest(String... args) {
        Sequence<String> sequence = this.constructorTest();
        for (String s : args) {
            sequence.add(sequence.length(), s);
        }
        return sequence;
    }

    /**
     *
     * Creates and returns a {@code Sequence<String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the entries for the sequence
     * @return the constructed sequence
     * @ensures createFromArgsRef = [entries in args]
     */
    private Sequence<String> createFromArgsRef(String... args) {
        Sequence<String> sequence = this.constructorRef();
        for (String s : args) {
            sequence.add(sequence.length(), s);
        }
        return sequence;
    }

    /**
     * Test 1 for just constructors with a method and its reference
     * implementation.
     */
    @Test
    public void test1() {
        Sequence<String> s1 = this.constructorTest();
        Sequence<String> s2 = this.constructorRef();
        assertEquals(s1, s2);
    }

    /**
     * Test for add method 1
     **/
    @Test
    public void test2() {
        Sequence<String> s1 = this.createFromArgsTest("Hello ", "my ", "name ");
        Sequence<String> s2 = this.createFromArgsRef("Hello ", "my ", "name ",
                "is ", "Nyja ");
        s1.add(s1.length(), "is ");
        s1.add(s1.length(), "Nyja ");
        assertEquals(s1, s2);
    }

    /**
     * Test for add method 2
     **/
    @Test
    public void test3() {
        Sequence<String> s1 = this.createFromArgsTest("Today ", "Monday ");
        Sequence<String> s2 = this.createFromArgsRef("Today ", "is ",
                "Monday ");
        s1.add(1, "is ");
        assertEquals(s1, s2);
    }

    /**
     * Test for remove method 1
     **/
    @Test
    public void test4() {
        Sequence<String> s1 = this.createFromArgsTest("Today ", "is ",
                "Monday ", "and ");
        Sequence<String> s2 = this.createFromArgsRef("Today ", "is ",
                "Monday ");
        s1.remove(s1.length() - 1);
        assertEquals(s1, s2);
    }

    /**
     * Test for length method 1
     **/
    @Test
    public void test5() {
        Sequence<String> s1 = this.createFromArgsTest("Today ", "is ",
                "Monday ", "and ");
        int x = s1.length();
        assertEquals(x, 4);
    }

    /**
     * Test for length method 2
     **/
    @Test
    public void test6() {
        Sequence<String> s1 = this.createFromArgsTest();
        int x = s1.length();
        assertEquals(x, 0);
    }

    @Test
    public void testforFlip() {
        Sequence<String> s1 = this.createFromArgsTest("1", "2", "3", "4", "5");
        Sequence<String> s2 = this.createFromArgsTest("5", "4", "3", "2", "1");
        s1.flip();
        assertEquals(s1, s2);
    }

    @Test
    public void testForFlip2() {
        Sequence<String> s1 = this.createFromArgsTest("Today ", "is ",
                "Monday ", "and ");
        Sequence<String> s2 = this.createFromArgsTest("and ", "Monday ", "is ",
                "Today ");
        s1.flip();
        assertEquals(s1, s2);
    }

}
