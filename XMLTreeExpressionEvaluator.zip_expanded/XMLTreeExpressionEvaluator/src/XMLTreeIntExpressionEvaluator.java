import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author NYJA GOWDA date:- 3/16/2021
 *
 */
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";
        int res = 0; //stores final result
        if (exp.numberOfChildren() != 0) {
            int fOper = evaluate(exp.child(0)); //getting first operand
            int sOper = evaluate(exp.child(1)); //getting second operand
            String s = exp.label();
            if (s.equals("plus")) {
                res = fOper + sOper;
            }
            if (s.equals("minus")) {
                res = fOper - sOper;
            }
            if (s.equals("times")) {
                res = fOper * sOper;
            }
            if (s.equals("divide")) {
                if (sOper == 0) { //checking for divide by 0 error
                    Reporter.fatalErrorToConsole("Error! Cannot divide by 0");
                } else {
                    res = fOper / sOper;
                }
            }
        } else { //if no children then tag with operand has been reached
            res = Integer.parseInt(exp.attributeValue("value"));
        }
        return res;
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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
