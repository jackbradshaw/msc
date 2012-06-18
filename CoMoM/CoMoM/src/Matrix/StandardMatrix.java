package Matrix;

import Utilities.MiscFunctions;
import DataStructures.BigRational;
import DataStructures.QNModel;

public class StandardMatrix extends Matrix {

	private BigRational[][] array;
	
	public StandardMatrix( int size) {
		super(size);
		array = new BigRational[size][size];
		
	}

	 /**
	  * Makes every entry in the array 0
	  */
	 public void fillWithZeros() {
		 for (int i = 0; i < size; i++) {
	    	 for (int j = 0; j < size; j++) {
	    		 array[i][j] = BigRational.ZERO; 	    		
	    	 }	    		   
	     }
	 }
	 
	/**
	 * Writes value v at position (row,col) in matrix
	 * @param v value to be written
	 * @param row row to be written at
	 * @param col column two be written at
	*/
	@Override
	public void write(int row, int col, BigRational v) {
		array[row][col] = v.copy();
		//TODO do you need to copy?
	}
	
	/**
	 * Prints the matrix to screen
	 */
	@Override
	public void print() {
		MiscFunctions.printMatrix(array);
	}

	/**
	 * Returns value a (row, col)
	 * @param row row
	 * @param col column
	 * @return array[row][column]
	 */
	@Override
	public BigRational get(int row, int col) {
		return array[row][col];
	}

	/**
	 * Multiplies v by the matrix
	 * @param v
	 * @return
	 */
	@Override
	public BigRational[] multiply(BigRational[] v) {
		//Taken for MiscFunctions
		//TODO: Matrix multiplication must be parallelised
		BigRational[][] A = array;
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

}
