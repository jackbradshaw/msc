/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package LinearSystem;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 * @author michalis
 */
public class ModuloSelectionTask implements Callable<BigInteger>{

    private int bitLength;

    public ModuloSelectionTask(int bitLength) {
        this.bitLength = bitLength;
    }

    @Override
    public BigInteger call() throws Exception {
        Random rnd = new Random();
        return BigInteger.probablePrime(bitLength, rnd);
    }

}
