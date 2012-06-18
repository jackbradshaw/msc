/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LinearSystem;

import DataStructures.BigRational;
import DataStructures.Tuple;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michalis
 */
public class ParallelSolver extends Solver {

    protected int nThreads;
    protected ExecutorService pool;

    public ParallelSolver(int nThreads) {
        this.nThreads = (nThreads <= 1) ? 1 : nThreads;// / 2;
        pool = Executors.newFixedThreadPool(this.nThreads); // If no relevant argument is given by the user, nThreads = Runtime.getRuntime().availableProcessors()
    }

    public void shutdown() {
        pool.shutdown();
        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(ParallelSolver.class.getName()).log(Level.SEVERE, null, ex);
            pool.shutdownNow();
        }
    }
    /*
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
    if (A[i][j].abs().greaterThan(maxOfAColumns[j])) {
    maxOfAColumns[j] = A[i][j];
    }
    }
    curULevel = level;
    }
    }*/
}
