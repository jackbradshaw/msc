package LinearSystem;

import DataStructures.BigReal;
import Utilities.MiscFunctions;
import javax.naming.OperationNotSupportedException;

/**
 * Class which defines a SimpleLinearSystemSolver object, which solves the
 * system using Gaussian elimination with partial pivoting.
 *
 * @author Michail Makaronidis, 2010
 */
public class SimpleLinearSystemSolver extends LinearSystemSolver {

    /**
     * Constructs a SimpleLinearSystemSolver object.
     */
    public SimpleLinearSystemSolver() {
        super();
    }

    /**
     * Solves the linear system using Gaussian elimination with partial pivoting
     * @param b The vector b of the linear system Ax = b
     * @return A vector containing the solutions of the linear system
     * @throws OperationNotSupportedException Thrown when the system cannot be solved due to bad vector b size
     */
    @Override
    public BigReal[] solve(BigReal[] b) throws OperationNotSupportedException {
        /*System.out.println("Matrix A:");
        MiscFunctions.printMatrix(A);
        System.out.println("Matrix b:");
        MiscFunctions.printMatrix(b);*/
        
        if (N != b.length) {
            throw new OperationNotSupportedException("Wrong size of vector b.");
        }
        if (N > 100) {
            System.out.print("Solving LinSys.");
        }

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                //if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                if (A[i][p].abs().greaterThan(A[max][p].abs())) {
                    max = i;
                }
            }
            BigReal[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            BigReal t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            //if (Math.abs(A[p][p]) <= EPSILON) {
            BigReal element;
            if (A[p][p].abs().isZero()) {
                //MiscFunctions.printMatrix(A);
                //throw new RuntimeException("Matrix is singular");
                element = BigReal.ONE;
            } else {
                element = A[p][p];
            }

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                BigReal alpha = A[i][p].divide(element);
                b[i] = b[i].subtract(alpha.multiply(b[p]));
                for (int j = p; j < N; j++) {
                    A[i][j] = A[i][j].subtract(alpha.multiply(A[p][j]));
                }
            }
        }

        // back substitution
        BigReal[] x = new BigReal[N];
        for (int i = N - 1; i >= 0; i--) {
            BigReal sum = BigReal.ZERO;
            for (int j = i + 1; j < N; j++) {
                sum = sum.add(A[i][j].multiply(x[j]));
            }
            if (!A[i][i].isZero()) {
                x[i] = (b[i].subtract(sum)).divide(A[i][i]);
            } else {
                //x[i] = BigReal.ZERO;
                x[i] = new BigReal(-1);
            }
        }

        if (N > 100) {
            System.out.print(".");
        }

        // Restore original matrices
        MiscFunctions.arrayCopy(cleanA, A);

        if (N > 100) {
            System.out.println(".OK!");
        }
        return x;
    }
}
