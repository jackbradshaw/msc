/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LinearSystem;

import DataStructures.BigRational;
import java.util.concurrent.Callable;

/**
 *
 * @author michalis
 */
public class RowTask implements Callable<Object> {

    private BigRational[][] Amod;
    private BigRational[] bmod;
    private int k, startIndex, endIndex, Ncols;

    public RowTask() {
        super();
    }

    public void prepare(BigRational[][] Amod, BigRational[] bmod, int k, int startIndex, int endIndex, int Ncols) {
        this.Amod = Amod;
        this.bmod = bmod;
        this.k = k;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.Ncols = Ncols;
    }

    @Override
    public Object call() throws Exception {
        for (int i = startIndex; i < endIndex; i++) {
            // Make row operation
            BigRational f = /*(Amod[k][k].isZero()) ? Amod[i][k] :*/ Amod[i][k].divide(Amod[k][k]);
            bmod[i] = bmod[i].subtract(f.multiply(bmod[k]));
            for (int j = k; j < Ncols; j++) {
                if (!Amod[k][j].isZero()) {
                    Amod[i][j] = Amod[i][j].subtract(f.multiply(Amod[k][j]));
                }
            }
        }
        return null;
    }
}
