package MatrixBuild;

import java.util.LinkedList;
import java.util.List;

import Utilities.MiscFunctions;
import Basis.BTFCoMoMBasis;
import Basis.BTFCoMoMBasisMLorder;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import DataStructures.Tuple;
import Exceptions.InternalErrorException;

public class CoMoMAB {
	 /**
     * The matrix A of the algorithm.
     */
    /**
     * The matrix B of the algorithm.
     */
    private BigRational[][] A, B;
    
    private List<Tuple<Integer, Integer>> UList;
    
    /**
     * Queueing Network Model
     */
    private QNModel qnm;
    
    private int M, R;
    
    private int matrix_size;
    
    private CoMoMBasis basis;
    
    public CoMoMAB (QNModel q) {
    	qnm = q;
    	M = qnm.M;
    	R = qnm.R;
    	matrix_size = (M + 1)*MiscFunctions.binomialCoefficient(M + R - 1 , M);    
    	System.out.println("matrix_size: " + matrix_size);
    }
    
    public void initialise() {
    	 
    	basis = new BTFCoMoMBasisMLorder(R,M);
    	basis.print();
    	
         A = new BigRational[matrix_size][matrix_size];         
         B = new BigRational[matrix_size][matrix_size];
    		 for (int i = 0; i < matrix_size; i++) {
    			 for (int j = 0; j < matrix_size; j++) {
    				 A[i][j] = BigRational.ZERO; 
    		         B[i][j] = BigRational.ZERO;
    		     }
    		     //uncomputables.add(i);
    		 }
    		 UList = new LinkedList<Tuple<Integer, Integer>>();
    }
    
    public void generateAB(PopulationVector N, int current_class) throws InternalErrorException {
    	int row = -1;
    	int col = 0;
    	PopulationChangeVector n;
    	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
    		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
    		if(n.sumTail(current_class-1) > 0) {  //potential negative population
    			for(int k = 0; k <= M; k++) {
    				row++;
    				col = basis.indexOf(n,k);
    				A[row][col] = BigRational.ONE;
    				if(n.sumTail(current_class) > 0) {  //negative population
    					col = basis.indexOf(n, k);
    					B[row][col] = BigRational.ONE;
    				} else {
    					n.minusOne(current_class);
    					col = basis.indexOf(n, k);
    					B[row][col] = BigRational.ONE;
    					n.restore();
    				}    				
    			}
    		} else {
    			if(n.sumHead(current_class - 1) < M) {
    				for(int k = 1; k <= M; k++) {
    					//add CE corresponding to G(0, N - n) for each queue k
    					row++;
    					col = basis.indexOf(n, k);
    					A[row][col] = BigRational.ONE;
    					col = basis.indexOf(n, 0);
    					A[row][col] = BigRational.ONE.negate();
    					for(int s = 1; s < current_class; s++) {
    						n.plusOne(s);	
    						System.out.println("k: " + k);
    						col = basis.indexOf(n, k);
    						n.restore();
    						A[row][col] = qnm.getDemandAsBigRational(k-1, s-1).negate();
    						System.out.println("k-1,s-1: " + qnm.getDemandAsBigRational(k-1, s-1));
    					}
    					col = basis.indexOf(n, k);
    					B[row][col] = qnm.getDemandAsBigRational(k-1, current_class-1);
    				}
    				for(int s = 1; s < current_class; s++) {
    					//add PC  corresponding to G(0, N - n) for each class s less than the current class
    					row++;
    					col = basis.indexOf(n, 0);
    					A[row][col] = N.getAsBigRational(s-1);
    					n.plusOne(s);
    					col = basis.indexOf(n, 0);
    					A[row][col] = qnm.getDelayAsBigRational(s-1).negate();
    					for(int k = 1; k <= M; k++) { //loop over all queues k (= sum)
    						col = basis.indexOf(n, k);
    						A[row][col] = qnm.getDemandAsBigRational(k-1, s-1).negate();
    					}
    					n.restore();
    				}
    			}    			    			
    		}
    	}
    	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
    		//add PC of class 'current_class'
    		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
    		if(n.sumTail(current_class-1) <= 0) {  //potential negative population TODO remove <
    			row++;
    			col = basis.indexOf(n, 0);
				A[row][col] = N.getAsBigRational(current_class-1);
				UList.add(new Tuple<Integer, Integer>(row, col));
				B[row][col] = qnm.getDelayAsBigRational(current_class -1 );
				for( int k = 1; k <= M; k++) {
					col = basis.indexOf(n, k);
					B[row][col] = qnm.getDemandAsBigRational( k - 1, current_class -1 );
					System.out.println("adding");
				}
    		}
    	}
    }
    
    public void print() {
    	System.out.println("A:");
    	MiscFunctions.printMatrix(A);
    	System.out.println("B:");
    	MiscFunctions.printMatrix(B);
    }

}
