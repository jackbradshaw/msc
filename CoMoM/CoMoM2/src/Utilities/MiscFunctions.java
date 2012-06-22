package Utilities;

import DataStructures.BigRational;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements several basic math functions.
 *
 * @author Michail Makaronidis, 2010
 */
public class MiscFunctions {

    /**
     * This method calculates the binomial coefficient C(n, k) ("n choose k")
     * @param n The first number
     * @param k The second number
     * @return The binomial coefficient
     */
    public static int binomialCoefficient(int n, int k) {
        int[] b = new int[n + 1];
        b[0] = 1;

        for (int i = 1; i <= n; ++i) {
            b[i] = 1;
            for (int j = i - 1; j > 0; --j) {
                b[j] += b[j - 1];
            }
        }
        return b[k];
    }

    /**
     * Computes the factorials up to a given number and returns them as a
     * Map<Integer,BigRational>.
     *
     * @param n The number up to which the factorials are computed
     * @return The Map<Integer,BigRational> containing the computed values
     */
    public static Map<Integer, BigRational> computeFactorials(int n) {
        Map<Integer, BigRational> toReturn = new HashMap<Integer, BigRational>();
        BigRational val = BigRational.ONE;
        for (Integer i = 0; i <= n; i++) {
            toReturn.put(i, val);
            val = val.multiply(new BigRational(i + 1));
        }
        return toReturn;
    }

    /**
     * Prints a 2-dimensional matrix
     * @param A A 2-dimensional matrix of ints
     */
    public static void printMatrix(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.format("%3d ", A[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a 2-dimensional matrix
     * @param A A 2-dimensional matrix of BigRationals
     */
    public static void printMatrix(BigRational[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j].isUndefined()) {
                    System.out.print("*");
                }
                System.out.format("%2s ", A[i][j].toString());
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Prints a 2-dimensional matrix
     * @param A A 2-dimensional matrix of BigRationals
     */
    public static void dotprintMatrix(BigRational[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j].isUndefined()) {
                    System.out.print("*");
                }
                if (A[i][j].equals(BigRational.ZERO)) {
                	 System.out.format("%2s ",".");
                }else{
                System.out.format("%2s ", A[i][j].toString());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a 2-dimensional matrix of BigRationals as doubles.
     * @param A A 2-dimensional matrix of BigRationals
     */
    public static void printPrettyMatrix(BigRational[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.format("%3s ", A[i][j].approximateAsDouble());
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a 1-dimensional matrix.
     * @param A A 1-dimensional matrix of ints
     */
    public static void printMatrix(int[] A) {
        int matrixSize = A.length;
        for (int i = 0; i < matrixSize; i++) {
            System.out.format("%3d ", A[i]);
        }
        System.out.println();
    }

    /**
     * Prints a 1-dimensional matrix.
     * @param A A 1-dimensional matrix of BigRationals
     */
    public static void printMatrix(BigRational[] A) {
        int matrixSize = A.length;
        for (int i = 0; i < matrixSize; i++) {
            if (A[i].isUndefined()) {
                System.out.print("*");
            }
            System.out.format("%3s \n", A[i].toString());
        }
        System.out.println();
    }

    /**
     * Prints a 1-dimensional matrix of BigRationals as doubles.
     * @param A A 1-dimensional matrix of BigRationals
     */
    public static void printPrettyMatrix(BigRational[] A) {
        int matrixSize = A.length;
        for (int i = 0; i < matrixSize; i++) {
            System.out.format("%3s ", A[i].approximateAsDouble());
        }
        System.out.println();
    }

    /**
     * Checks whether a double has a zero fractional part, i.e. it represents an
     * integer number.
     *
     * @param d The number to check
     * @return True if it is an integer, false otherwise
     */
    public static boolean hasNoFractionalPart(double d) {
        if (d == (int) d) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Transforms an array of ints to an array of BigRationals.
     * @param s The source array of ints
     * @return The resulting array of BigRationals
     */
    public static BigRational[] createBigRationalArray(int[] s) {
        BigRational[] A = new BigRational[s.length];

        for (int i = 0; i < s.length; i++) {
            A[i] = new BigRational(s[i]);
        }
        return A;
    }

    /**
     * Transforms an array of ints to an array of BigRationals.
     * @param d The source array of ints
     * @return The resulting array of BigRationals
     */
    public static BigRational[][] createBigRationalArray(int[][] d) {
        BigRational[][] A = new BigRational[d.length][d[0].length];


        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                A[i][j] = new BigRational(d[i][j]);
            }
        }

        return A;
    }

    /**
     * Multiplies a two-dimensional matrix of BigRationals with a one-dimensional one.
     * It does not perform a generic multiplication, but it rather checks for
     * linear system inconsistencies as well.
     * @param A The two-dimensional matrix
     * @param v The one-dimensional matrix
     * @return The multiplication result
     */
    public static BigRational[] matrixVectorMultiply(BigRational[][] A, BigRational[] v) {
        //TODO: Matrix multiplication must be parallelised
        int rowsA = A.length;
        int columnsA = A[0].length;
        int rowsV = v.length;

        if (columnsA == rowsV) {
            BigRational[] c = new BigRational[rowsA];
            for (int i = 0; i < rowsA; i++) {
                c[i] = BigRational.ZERO;
                for (int j = 0; j < columnsA; j++) {
                    if (!A[i][j].isZero()) {
                        if (v[j].isPositive()) {
                            c[i] = c[i].add(A[i][j].multiply(v[j]));
                        } else if (v[j].isUndefined()) {
                            c[i] = new BigRational(-1);
                            c[i].makeUndefined();
                            break;
                        }
                    }
                }
            }
            return c;
        } else {
            throw new ArithmeticException("Cannot multiply matrices with wrong sizes! (" + rowsA + "x" + columnsA + ")x(" + rowsV + "x1)");
        }
    }

    /**
     * Copy a source array to a destination one. Arrays must be of same size and
     * initialised.
     * @param source Source array
     * @param destination Destination array
     */
    public static void arrayCopy(Object[][] source, Object[][] destination) {
        for (int a = 0; a < source.length; a++) {
            System.arraycopy(source[a], 0, destination[a], 0, source[a].length);
        }
    }

    /**
     * Copy a source array to a destination one. Arrays must be of same size and
     * initialised.
     * @param source Source array
     * @param destination Destination array
     */
    public static void arrayCopy(Object[] source, Object[] destination) {
        System.arraycopy(source, 0, destination, 0, source.length);
    }
    /*
    private static BigDecimal getInitialApproximation(BigDecimal n) {
    BigInteger integerPart = n.toBigInteger();
    int length = integerPart.toString().length();
    if ((length % 2) == 0) {
    length--;
    }
    length /= 2;
    BigDecimal guess = BigDecimal.ONE.movePointRight(length);
    return guess;
    }

    public static BigInteger sqrtApproximation(BigDecimal n) {
    // Make sure n is a positive number
    int comparisonResult = n.compareTo(BigDecimal.ZERO);
    if (comparisonResult < 0) {
    throw new IllegalArgumentException();
    } else if (comparisonResult == 0) {
    return BigInteger.ZERO;
    }

    int scale = 1;
    BigDecimal initialGuess = getInitialApproximation(n);
    BigDecimal lastGuess = BigDecimal.ZERO;
    BigDecimal guess = new BigDecimal(initialGuess.toString());
    BigDecimal TWO = BigDecimal.ONE.add(BigDecimal.ONE);
    int maxIterations = 50;

    // Iterate
    int iterations = 0;
    boolean more = true;
    while (more) {
    lastGuess = guess;
    guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
    guess = guess.add(lastGuess);
    guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
    BigDecimal error = n.subtract(guess.multiply(guess));
    if (++iterations >= maxIterations) {
    more = false;
    } else if (lastGuess.equals(guess)) {
    more = error.abs().compareTo(BigDecimal.ONE) >= 0;
    }
    }
    return guess.toBigInteger();
    }*/

    private MiscFunctions() {
        super();
    }

    public static String memoryUsage() {
        long mem;
        // Find memory usage:
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        mem = Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory();
        String res;
        if (mem < 1024){
            res = mem+" B";
        } else if (mem < 1024*1024){
            res = mem/1024.0+" kB";
        } else if (mem < 1024*1024*1024){
            res = mem/1024.0/1024.0+" MB";
        } else {
            res = mem/1024.0/1024.0/1024.0+" GB";
        }
        return res;
    }
}
