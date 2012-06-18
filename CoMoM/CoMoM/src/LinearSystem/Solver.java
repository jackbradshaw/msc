package LinearSystem;

import DataStructures.BigRational;
import DataStructures.Tuple;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import Utilities.MiscFunctions;
import Utilities.Timer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.OperationNotSupportedException;

/**
 * Class which defines several methods every Solver object must
 * support.
 *
 * @author Michail Makaronidis, 2010
 */
public class Solver implements SolverInterface {

    protected Timer t = new Timer();
    /**
     * The matrix A of the linear system Ax = b.
     */
    /**
     * Holds the initial version of A, as calling solve(..) destroys
     * A's contents (the calculations are performed in-place).
     */
    protected BigRational[][] A, cleanA;
    /**
     * The vector b of the linear system Ax = b.
     */
    protected BigRational[] b;
    /**
     * Represents how many times the elements in the positions list UList have
     * been increased by 1.
     */
    protected int curULevel;
    /**
     * The positions of elements of A that may need to be increased between
     * successive invocations of solve(..). This is a requirement of the MoM
     * algorithm, which was embedded in the Solver for performance reasons.
     */
    protected List<Tuple<Integer, Integer>> UList;
    protected Set<Integer> uncomputables;
    //private Set<Integer> oldSpecial;
    //protected Map<Integer, Integer> special;
    //protected BigRational[] maxOfAColumns;
    /**
     * The number of unknowns.
     */
    protected int N;
    /**
     * It is used to determine wheter the matrix A is singular.
     */
    protected static final BigRational EPSILON = new BigRational(1e10).reciprocal();

    /**
     * Constructs a Solver object.
     */
    public Solver() {
        super();
    }

    /**
     * Initialises the solver object.
     * @param A The matrix A of the linear system Ax = b
     * @throws OperationNotSupportedException Thrown when the matrix A is not square
     */
    @Override
    public void initialise(BigRational[][] A) throws OperationNotSupportedException {
        t.start();
        List<Tuple<Integer, Integer>> l = new ArrayList<Tuple<Integer, Integer>>();
        Set<Integer> emptyUncomputables = new HashSet<Integer>();
        t.pause();
        this.initialise(A, l, emptyUncomputables);
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
    public void initialise(BigRational[][] A, List<Tuple<Integer, Integer>> UList, Set<Integer> uncomputables) throws OperationNotSupportedException {
        t.start();
        this.A = A;
        N = A.length;
        if (A[0].length != N) {
            throw new OperationNotSupportedException("Matrix A of linear system is not square.");
        }
        this.UList = UList;
        this.uncomputables = uncomputables;
        //special = new HashMap<Integer,Integer>();
        //oldSpecial = new HashSet<Integer>();
        //findSpecial();
        curULevel = 0;

        cleanA = new BigRational[N][N];
        MiscFunctions.arrayCopy(A, cleanA);
        t.pause();
    }

    public void initialise(BigRational[][] A, List<Tuple<Integer, Integer>> UList, Set<Integer> uncomputables, int maxA, BigInteger maxb, BigRational maxG) throws OperationNotSupportedException, InternalErrorException {
        initialise(A, UList, uncomputables);
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
        BigRational delta = new BigRational(level - curULevel);

        if (!delta.isZero()) {
            for (Tuple<Integer, Integer> tuple : UList) {
                i = tuple.getX();
                j = tuple.getY();
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
     * @throws OperationNotSupportedException Thrown always, as this Solver does not support actual solution of the system
     * @throws InconsistentLinearSystemException Thrown when the algorithm should return an undeterminable, from the initial linear system, value.
     */
    @Override
    public BigRational[] solve(BigRational[] b) throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {
        throw new OperationNotSupportedException();
    }

/*
    protected void findSpecial() {
        for (int j = 0; j < N; j++) {
            if (!uncomputables.contains(j) && !oldSpecial.contains(j)) {
                BigRational sumj = BigRational.ZERO;
                int positionOfNonZero=-1;
                for (int i = 0; i < N; i++) {
                    if (!uncomputables.contains(i) && !oldSpecial.contains(i)) {
                        sumj = sumj.add(A[i][j].abs());
                    } else {
                        positionOfNonZero = i;
                    }
                }
                if (sumj.isZero()) {
                    special.put(j,positionOfNonZero); // Saved in the order <Special element, Element causing it>
                    oldSpecial.add(j);
                    findSpecial();
                }
            }
        }
    }*/

    protected Set<Integer> findMoreUncomputables(BigRational[] b) {
        Set<Integer> toReturn = new HashSet<Integer>();
        for (int i = 0; i < N; i++) {
            if (b[i].isUndefined()) {
                // For each non-zero of row i
                for (int j = 0; j < N; j++) {
                    if (!A[i][j].isZero()) {
                        // if the column j has only A[i][j] as a non-zero, consider j as uncomputable
                        boolean isOnlyNZ = true;
                        for (int k = 0; k < N; k++) {
                            if ((k != i) && (!A[k][j].isZero())) {
                                isOnlyNZ = false;
                                break;
                            }
                        }
                        if (isOnlyNZ) {
                            toReturn.add(j);
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    @Override
    public Timer getTimer() {
        return t;
    }

    public void shutdown(){
    }
}
