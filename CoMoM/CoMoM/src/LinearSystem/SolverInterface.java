package LinearSystem;

import DataStructures.BigRational;
import DataStructures.Tuple;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import Utilities.Timer;
import java.util.List;
import java.util.Set;
import javax.naming.OperationNotSupportedException;

/**
 * Interface for classes which define several methods every LinearSystemSolver
 * object must support.
 *
 * @author Michail Makaronidis, 2010
 */
public interface SolverInterface {
/**
     * Initialises the solver object.
     * @param A The matrix A of the linear system Ax = b
     * @throws OperationNotSupportedException Thrown when the matrix A is not square
     */
    public void initialise(BigRational[][] A) throws OperationNotSupportedException;
    /**
     * Initialises the solver object, taking into account a list of positions of
     * A that may need to be updated when requested. This is needed by the MoM
     * algorithm.
     * @param A The matrix A of the linear system Ax = b
     * @param UList The list containg these positions as Tuple objects
     * @throws OperationNotSupportedException Thrown when the matrix A is not square
     */
    public abstract void initialise(BigRational[][] A, List<Tuple<Integer, Integer>> UList, Set<Integer> uncomputables) throws OperationNotSupportedException;
/**
     * Sets matrices A and cleanA equal to the original A used during
     * initialisation plus "level" increments in the positions needed. See how
     * the MoM algorithm updates A during the iterations.
     * @param level The number of applications with regard to the original A
     */
    public void goToULevel(int level);

    /**
     * Gaussian elimination linear system solver with partial pivoting.
     * @param b The vector b of the linear system Ax = b
     * @return A vector containing the solutions of the linear system
     * @throws OperationNotSupportedException Thrown when the system cannot be solved due to bad vector b size
     */
    public BigRational[] solve(BigRational[] b) throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException;
    
    public Timer getTimer();
}
