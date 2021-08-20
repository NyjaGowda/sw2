import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code NN}.
 *
 * @author NYJA GOWDA date:- 3/16/2021
 *
 */
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
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
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";
        NaturalNumber num, sOper;
        //if label is "number" then operand value obtained otherwise recursion
        if (exp.child(0).label().equals("number")) {
            num = new NaturalNumber2(exp.child(0).attributeValue("value"));
        } else {
            num = evaluate(exp.child(0));
        }
        //if label is "number" then operand value obtained otherwise recursion
        if (exp.child(1).label().equals("number")) {
            sOper = new NaturalNumber2(exp.child(1).attributeValue("value"));
        } else {
            sOper = evaluate(exp.child(1));
        }
        String s = exp.label();
        if (s.equals("plus")) {
            num.add(sOper);
        } else {
            if (s.equals("minus")) {
                if (num.compareTo(sOper) < 0) { //checking for this<n
                    Reporter.fatalErrorToConsole(
                            "Error! Cannot subtract by larger number!");
                } else {
                    num.subtract(sOper);
                }
            } else {
                if (s.equals("times")) {
                    num.multiply(sOper);

                } else {
                    if (s.equals("divide")) {
                        //checking for divide by 0 error
                        if (sOper.compareTo(new NaturalNumber2(0)) == 0) {
                            Reporter.fatalErrorToConsole(
                                    "Error! Divide By 0 occuring!");
                        } else {
                            num.divide(sOper);
                        }
                    }
                }
            }
        }
        NaturalNumber res = new NaturalNumber2();
        res.copyFrom(num);
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
