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

}
