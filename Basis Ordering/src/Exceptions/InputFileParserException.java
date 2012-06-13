package Exceptions;

/**
 * This class represents an exception that can be thrown if an error is
 * encountered while parsing the input file.
 *
 * @author Michail Makaronidis, 2010
 */
public class InputFileParserException extends Exception {

    /**
     * Creates an InputFileParserException with an exception message.
     *
     * @param string The exception message
     */
    public InputFileParserException(String string) {
        super(string);
    }

}
