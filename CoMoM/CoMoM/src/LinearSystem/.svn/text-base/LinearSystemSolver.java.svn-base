package LinearSystem;

import DataStructures.BigReal;
import DataStructures.Tuple;
import Utilities.MiscFunctions;
import java.util.ArrayList;
import java.util.List;
import javax.naming.OperationNotSupportedException;

/**
 * Class which defines several methods every LinearSystemSolver object must
 * support.
 *
 * @author Michail Makaronidis, 2010
 */
public class LinearSystemSolver implements LinearSystemSolverInterface {

    /**
     * The matrix A of the linear system Ax = b.
     */
    /**
     * Holds the initial version of A, as calling solve(..) destroys
     * A's contents (the calculations are performed in-place).
     */
    protected BigReal[][] A, cleanA;
    /**
     * The vector b of the linear system Ax = b.
     */
    protected BigReal[] b;
    /**
     * Represents how many times the elements in the positions list UList have
     * been increased by 1.
     */
    private int curULevel;
    /**
     * The positions of elements of A that may need to be increased between
     * successive invocations of solve(..). This is a requirement of the MoM
     * algorithm, which was embedded in the LinearSystemSolver for performance reasons.
     */
    private List<Tuple<Integer, Integer>> UList;
    /**
     * The number of unknowns.
     */
    protected int N;
    /**
     * It is used to determine wheter the matrix A is singular.
     */
    protected static final BigReal EPSILON = new BigReal(1e10).reciprocal();

    /**
     * Constructs a LinearSystemSolver object.
     */
    public LinearSystemSolver() {
        super();
    }

    /**
     * Initialises the solver object.
     * @param A The matrix A of the linear system Ax = b
     * @throws OperationNotSupportedException Thrown when the matrix A is not square
     */
    @Override
    public void initialise(BigReal[][] A) throws OperationNotSupportedException {
        List<Tuple<Integer, Integer>> l = new ArrayList<Tuple<Integer, Integer>>();
        this.initialise(A, l);
    }

    /**
     * Initialises the solver object, taking into account a list of positions of
     * A that may need to be updated when requested. This is needed by the MoM 
     * algorithm.
     * @param A The matrix A of the linear system Ax = b
     * @param UList The list containg these positions as Tuple objects
     * @throws OperationNotSupportedException Thrown when the matrix A is not square
     */
    @Override
    public void initialise(BigReal[][] A, List<Tuple<Integer, Integer>> UList) throws OperationNotSupportedException {
        this.A = A;
        this.UList = UList;
        curULevel = 0;
        N = A.length;
        if (A[0].length != N) {
            throw new OperationNotSupportedException("Matrix A of linear system is not square.");
        }

        cleanA = new BigReal[N][N];
        MiscFunctions.arrayCopy(A, cleanA);
    }

    /**
     * Sets matrices A and cleanA equal to the original A used during
     * initialisation plus "level" increments in the positions needed. See how
     * the MoM algorithm updates A during the iterations.
     * @param level The number of applications with regard to the original A
     */
    @Override
    public void goToULevel(int level) {
        int i, j;
        BigReal delta = new BigReal(level - curULevel);

        if (!delta.isZero()) {
            for (Tuple<Integer, Integer> t : UList) {
                i = t.getX();
                j = t.getY();
                A[i][j] = A[i][j].add(delta);
                cleanA[i][j] = A[i][j];//.copy(); // Copy seems to be unnecessary
            }
            curULevel = level;
        }
    }

    /**
     * Solves the linear system.
     * @param b The vector b of the linear system Ax = b
     * @return A vector containing the solutions of the linear system
     * @throws OperationNotSupportedException Thrown always, as this LinearSystemSolver does not support actual solution of the system
     */
    @Override
    public BigReal[] solve(BigReal[] b) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
