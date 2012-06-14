package DataStructures;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class is a representation of rational numbers. Essentially, it uses two 
 * BigIntegers to represent any rational number. It also contains the necessary
 * methods for the basic arithmetic operations.
 * 
 * Initial Source: http://www.cs.princeton.edu/introcs/92symbolic/BigReal.java.html
 * Adapted by me
 * 
 * @author Michail Makaronidis, 2010
 * 
 */
public class BigReal implements Comparable<BigReal> {

    /**
     * This is a variable equal to zero.
     */
    public final static BigReal ZERO = new BigReal(0);
    /**
     * This is a variable equal to one.
     */
    public final static BigReal ONE = new BigReal(1);
    private BigInteger num;   // the numerator
    private BigInteger den;   // the denominator

    /**
     * The constructor creates and initialises a new BigReal object
     * @param numerator The numerator of the rational number
     * @param denominator The denominator of the rational number
     */
    public BigReal(int numerator, int denominator) {
        this(new BigInteger("" + numerator), new BigInteger("" + denominator));
    }

    /**
     * The constructor creates and initialises a new BigReal object where the
     * rational is an integer (no denominator is needed).
     *
     * @param numerator The number
     */
    public BigReal(int numerator) {
        this(numerator, 1);
    }

    // 
    /**
     * The constructor creates and initialises a new BigReal object from a
     * string, e.g., "-343/1273".
     *
     * @param s The string representing the number
     */
    public BigReal(String s) {
        String[] tokens = s.split("/");
        if (tokens.length == 2) {
            init(new BigInteger(tokens[0]), new BigInteger(tokens[1]), false);
        } else if (tokens.length == 1) {
            init(new BigInteger(tokens[0]), BigInteger.ONE, true);
        } else {
            throw new RuntimeException("Parse error in BigRational");
        }
    }

    /**
     * The constructor creates and initialises a new BigReal object
     * @param numerator The numerator of the rational number
     * @param denominator The denominator of the rational number
     */
    public BigReal(BigInteger numerator, BigInteger denominator) {
        init(numerator, denominator, false); // we do not know if the fraction is
        // in reduced form.
    }

    /**
     * This private constructor creates and initialises a new BigReal object. It
     * is used by the other constructors, because we do not always need to
     * reduce the fraction. Control over this can speed the creation of such
     * objects.
     *
     * @param numerator The numerator of the rational number
     * @param denominator The denominator of the rational number
     * @param isNormal True if the fraction is already in reduced form, false otherwise.
     *
     */
    private BigReal(BigInteger numerator, BigInteger denominator, boolean isNormal) {
        init(numerator, denominator, isNormal);
    }

    /**
     * The constructor creates and initialises a new BigReal object
     * @param numerator The numerator of the rational number
     * @param denominator The denominator of the rational number
     */
    public BigReal(BigDecimal numerator, BigDecimal denominator) {
        int numeratorDecimalDigits = numerator.scale();
        int denominatorDecimalDigits = denominator.scale();
        int maxDecimalDigits = Math.max(numeratorDecimalDigits, denominatorDecimalDigits);
        if (maxDecimalDigits >= 0) {
            BigDecimal multiplicationFactor = BigDecimal.ONE.pow(maxDecimalDigits);
            BigInteger newNumerator = numerator.multiply(multiplicationFactor).toBigIntegerExact(); // no exception should be throwed
            BigInteger newDenominator = denominator.multiply(multiplicationFactor).toBigIntegerExact(); // no exception should be throwed
            init(newNumerator, newDenominator, false);
        } else {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    /**
     * The constructor creates and initialises a new BigReal object and sets it
     * equal to a BigDecimal. No denominator is needed as input, it is added
     * automatically.
     *
     * @param a The number
     */
    public BigReal(BigDecimal a) {
        this(a, BigDecimal.ONE);
    }

    /**
     * The constructor creates and initialises a new BigReal object and sets it
     * equal to a double. No denominator is needed as input, it is added
     * automatically.
     *
     * @param d The number
     */
    public BigReal(double d) {
        this(new BigDecimal(d), BigDecimal.ONE);
    }

    /**
     * The constructor creates and initialises a new BigReal object with the
     * default value of zero.
     *
     */
    public BigReal(){
        this(BigDecimal.ZERO);
    }

    /**
     * Returns a copy of the current BigReal.
     *
     * @return The BigReal object copy
     */
    public BigReal copy(){
        return new BigReal(num, den);
    }

    /**
     * This method is the core of all constructors. It takes the numerator and
     * the  denominator as BigIntegers and produces a BigReal, reducing the
     * fraction if needed.
     *
     * @param numerator The numerator of the rational number
     * @param denominator The denominator of the rational number
     * @param isNormal True if the fraction is already in reduced form, false otherwise.
     */
    private void init(BigInteger numerator, BigInteger denominator, boolean isNormal) {
        // If isNormal, then we do not need to reduce the fraction and compute the gcd
        // deal with x / 0
        if (denominator.equals(BigInteger.ZERO)) {
            throw new RuntimeException("Denominator is zero");
        }

        if (!isNormal) {
            // reduce fraction
            BigInteger g = numerator.gcd(denominator);

            num = numerator.divide(g);
            den = denominator.divide(g);
        } else {
            num = numerator;
            den = denominator;
        }
    }

    /**
     * This method ensures that the denominator of the fraction is positive. It
     * is used for easy comparison.
     */
    private void ensureDenoimatorPositive() {
        // to ensure invariant that denominator is positive
        if (den.compareTo(BigInteger.ZERO) < 0) {
            den = den.negate();
            num = num.negate();
        }
    }

    /**
     * Return string representation of the BigReal
     * @return String representation of the BigReal
     */
    @Override
    public String toString() {
        if (den.equals(BigInteger.ONE)) {
            return num + "";
        } else {
            return num + "/" + den;
        }
    }

    /**
     * Compares the BigReal with another.
     * @param b The other number
     * @return Returns { -1, 0, + 1 } if a < b, a = b, or a > b
     */
    @Override
    public int compareTo(BigReal b) {
        this.ensureDenoimatorPositive();
        BigReal a = this;
        return a.num.multiply(b.den).compareTo(a.den.multiply(b.num));
    }

    /**
     * Checks whether this number is greater than another.
     * @param b The other number
     * @return True if this number is greater than the other, else false.
     */
    public boolean greaterThan(BigReal b) {
        if (compareTo(b) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether this number is smaller than another.
     * @param b The other number
     * @return True if this number is smaller than the other, else false.
     */
    public boolean smallerThan(BigReal b) {
        if (compareTo(b) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether this number is greater than or equal to another.
     * @param b The other number
     * @return True if this number is greater than or equal to the other, else false.
     */
    public boolean greaterOrEqualThan(BigReal b) {
        if (greaterThan(b) || equals(b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether this number is smaller than or equal to another.
     * @param b The other number
     * @return True if this number is smaller than or equal to the other, else false.
     */
    public boolean smallerOrEqualThan(BigReal b) {
        if (smallerThan(b) || equals(b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decides if this BigReal is equal to zero.
     *
     * @return True if equal to zero, false otherwise
     */
    public boolean isZero() {
        //return compareTo(ZERO) == 0;
        // This is faster:
        return this.toString().equals("0");
    }

    /**
     * Decides if this BigReal is positive.
     *
     * @return True if positive, false otherwise
     */
    public boolean isPositive() {
        return compareTo(ZERO) > 0;
    }

    /**
     * Decides if this BigReal is negative.
     *
     * @return True if negative, false otherwise
     */
    public boolean isNegative() {
        return compareTo(ZERO) < 0;
    }

    /**
     * Returns a BigReal object equal to the absolute value of this BigReal.
     * @return The absolute value BigReal
     */
    public BigReal abs() {
        if (isNegative()) {
            return this.negate();
        } else {
            return this;
        }
    }

    /**
     * Decides if this BigReal is equal to another.
     *
     * @param y The other BigReal
     * @return True if equal, false otherwise
     */
    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        BigReal b = (BigReal) y;
        b.ensureDenoimatorPositive();
        this.ensureDenoimatorPositive();
        return compareTo(b) == 0;
    }

    /**
     * Returns a hashCode consistent with equals() and compareTo().
     * @return The hashCode
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Multiplies this number (a) with another (b)
     * @param b The number b
     * @return The number a*b
     */
    public BigReal multiply(BigReal b) {
        BigReal a = this;
        return new BigReal(a.num.multiply(b.num), a.den.multiply(b.den));
    }

    /**
     * Adds this number (a) to another (b)
     *
     * @param b The number b
     * @return The number a+b
     */
    public BigReal add(BigReal b) {
        BigReal a = this;
        BigInteger numerator = a.num.multiply(b.den).add(b.num.multiply(a.den));
        BigInteger denominator = a.den.multiply(b.den);
        return new BigReal(numerator, denominator);
    }

    /**
     * Negates this number (a)
     *
     * @return The number -a
     */
    public BigReal negate() {
        BigReal toReturn = new BigReal(num.negate(), den, true);
        toReturn.ensureDenoimatorPositive();
        return toReturn;
    }

    /**
     * Subtracts a number (b) from this one (a)
     *
     * @param b The number b
     * @return The number a-b
     */
    public BigReal subtract(BigReal b) {
        BigReal a = this;
        return a.add(b.negate());
    }

    /**
     * Return the reciprocal (1/a) of this number (a)
     *
     * @return The number 1/a
     */
    public BigReal reciprocal() {
        return new BigReal(den, num, true);
    }

    /**
     * Divides this number (a) by another (b)
     * @param b The number b
     * @return The number a/b
     */
    public BigReal divide(BigReal b) {
        if (!b.isZero()) {
            BigReal a = this;
            BigReal toRet = a.multiply(b.reciprocal());
            toRet.ensureDenoimatorPositive();
            return toRet;
        } else {
            throw new ArithmeticException("Division by zero!");
        }
    }

    /**
     * Computes this number raised to the power of another
     * @param n The exponent
     * @return The result of the exponentiation
     */
    public BigReal pow(int n) {
        BigInteger newNumerator = num.pow(n);
        BigInteger newDenominator = den.pow(n);
        return new BigReal(newNumerator, newDenominator);
    }

    /**
     * Returns the number as a BigDecimal. This can only work if the division of
     * the numerator by the denominator does not have an infinite decimal
     * expansion. This can be ensured by checking isBigDecimal() first.
     *
     * @return The division result
     */
    public BigDecimal asBigDecimal() {
        BigDecimal result = new BigDecimal(num);
        result = result.divide(new BigDecimal(den));
        return result;
    }

    /**
     * Returns whether the number can be expressed as a BigDecimal. This can
     * only happen if the division of the numerator by the denominator does not
     * have an infinite decimal expansion.
     *
     * @return True if it can be expressed as a BigDecimal, false otherwise.
     */
    public boolean isBigDecimal() {
        try {
            // TODO: Speed up the process by storing the result of the division,
            // as usually a call to isBigDecimal() is followed by a call to
            // asBigDecimal
            BigDecimal result = new BigDecimal(num);
            result = result.divide(new BigDecimal(den));
        } catch (ArithmeticException ex) {
            return false;
        }
        return true;
    }
}
