package Main;

import java.util.ArrayList;

import javax.naming.OperationNotSupportedException;

import Basis.BTFCoMoMBasisMLorder;
import Basis.BTFCoMoMBasisReorder;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import DataStructures.Tuple;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import LinearSystem.SimpleSolver;
import LinearSystem.Solver;
import Matrix.Matrix;
import Matrix.StandardMatrix;
import Utilities.MiscFunctions;

public class Main {
	
	private static StandardMatrix A, B;
	
	private static BTFCoMoMBasisReorder basis;
	
	private static QNModel qnm;
	private static int M,R;	
	
	private static Solver solver;
	
	private static PopulationVector current_N;
	private static PopulationVector target_N;
	
	public static void main(String[] args) throws InputFileParserException, InternalErrorException {
		
		qnm = new QNModel("jack_test.txt");
		M = qnm.M;
		R = qnm.R;	
		target_N = qnm.N;
		
		System.out.println("Model under study:\n");
		qnm.printModel();
		System.out.println("\n");
		
		basis = new BTFCoMoMBasisReorder(qnm);		
		
		A = new StandardMatrix(basis.getSize());
		B = new StandardMatrix(basis.getSize());		
		
		try {
			solve();
		} catch (OperationNotSupportedException ex) {
            ex.printStackTrace();
            throw new InternalErrorException("Error in linear system solver.");
        } catch (InconsistentLinearSystemException ex) {
            ex.printStackTrace();
            throw new InternalErrorException(ex.getMessage());
        }
		
		basis.computePerformanceMeasures();		
		qnm.printPerformaceMeasrues();
		
	}
	
	public static void solve() throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException {
		
		solver = new SimpleSolver();
		 
		current_N = new PopulationVector(0,R);
		
		basis.initialiseBasis();		
		basis.print_values();
				
		for(int current_class = 1; current_class <= R; current_class++) {
			System.out.println("Working on class " + current_class);
			System.out.println("Current Population: " + current_N);
			solveForClass(current_class);		
			//return;
		}
				
		//Store the computed normalsing constant
		BigRational G = basis.getNormalisingConstant();
		System.out.println("G = " + G);
		qnm.setNormalisingConstant(G);		
		
	}
	
	public static void solveForClass(int current_class) throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException {
		
		//If no jobs of current_class in target population, move onto next class
		if(target_N.get(current_class - 1) == 0) {
			return;
		}		
		
		basis.initialiseForClass(current_class);
		
		
		current_N.plusOne(current_class);
		generateAB2(current_N, current_class);
		System.out.println("Intialising A and B:");
		System.out.println("A:");
		A.print();
		System.out.println("B:");
		B.print();
		
		
		solver.initialise(A.getArray(), A.get_update_list(), basis.getUncomputables());
				
		for(int current_class_population = current_N.get(current_class - 1); 
				current_class_population <= target_N.get(current_class - 1); 
				current_class_population++ ) {
			
			
			System.out.println("Solving for population: " + current_N);
			System.out.println(current_class_population);
			
			solver.goToULevel(current_class_population - 1);
			
			solveSystem();
						
			if(current_class_population < target_N.get(current_class - 1)) {
				//System.out.println("Updated A: ");
				//A.update();  Updating now done in solver
				//A.print();
				
				current_N.plusOne(current_class);
			}
		}		
	}
	
	public static void solveSystem() throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {
		System.out.println("Solving System...\n");
		
		BigRational[] sysB = new BigRational[basis.getSize()];
		sysB  = B.multiply(basis.getBasis().toArray(sysB));
		//MiscFunctions.printMatrix(sysB);
		basis.setBasis(solver.solve(sysB));
		basis.print_values();
	}
	
	 public static void generateAB(PopulationVector N, int current_class) throws InternalErrorException {
	 	A.reset();
	 	B.reset();
	 	basis.reset_uncomputables();
	 			
	   	int row = -1;
	   	int col = 0;
	   	PopulationChangeVector n;
	   	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
	   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
	   		if(n.sumTail(current_class-1) > 0) {  //potential negative population
	   			for(int k = 0; k <= M; k++) {
	   				row++;
	   				col = basis.indexOf(n,k);
	   				A.write(row, col, BigRational.ONE);
	   				basis.computatble(col);
	   				//System.out.println("Fisrt n: " + n);
	   				//System.out.println("row: " + row);
	   				if(n.sumTail(current_class) > 0) {  //negative population
	   					//System.out.println("1");
	   					col = basis.indexOf(n, k);
	   					//System.out.println("col: " + col);
	   					B.write(row, col, BigRational.ONE);
	   				} else {
	   					//System.out.println("2");
	   					n.minusOne(current_class);    					
	   					col = basis.indexOf(n, k);
	   					//System.out.println("col: " + col);
	   					B.write(row, col,BigRational.ONE);
	   					n.restore();
	   				}    				
	   			}
	   		} else {
	   			if(n.sumHead(current_class - 2) < M) {    //TODO changed to - 2 from - 1, check it dosn't break it
	   				for(int k = 1; k <= M; k++) {
	   					//add CE corresponding to G(0, N - n) for each queue k
	   					row++;
	   					col = basis.indexOf(n, k);
	   					A.write(row, col, BigRational.ONE);
	   					basis.computatble(col);
	   					col = basis.indexOf(n, 0);
	   					A.write(row, col, BigRational.ONE.negate());
	   					basis.computatble(col);
	   					for(int s = 1; s <= current_class - 1; s++) {  //TODO can change to R - 1 ? No, some coefficients in B
	   						n.plusOne(s);	
	   						//System.out.println("n: " + n);
	   						col = basis.indexOf(n, k);
	   						n.restore();
	   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
	   						basis.computatble(col);
	   						//System.out.println("k-1,s-1: " + qnm.getDemandAsBigRational(k-1, s-1));
	   					}
	   					col = basis.indexOf(n, k);
	  					B.write(row, col, qnm.getDemandAsBigRational(k-1, current_class-1));
	  				}
	   				for(int s = 1; s < current_class; s++) {
	   					//add PC  corresponding to G(0, N - n) for each class s less than the current class
	   					row++;
	    				col = basis.indexOf(n, 0);
	   					A.write(row, col, N.getAsBigRational(s-1).subtract(n.getAsBigRational(s-1))); 
	   					basis.computatble(col);
	   					n.plusOne(s);
	   					col = basis.indexOf(n, 0);
	   					A.write(row, col, qnm.getDelayAsBigRational(s-1).negate());
	   					basis.computatble(col);
	   					for(int k = 1; k <= M; k++) { //loop over all queues k (= sum)
	   						col = basis.indexOf(n, k);
	   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
	   						basis.computatble(col);
	   					}
	   					n.restore();
	   				}
	   			}    			    			
	   		}
	   	}
	   	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
	   		//add PC of class 'current_class'
	   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
	   		if(n.sumTail(current_class-1) <= 0) {  // TODO remove <
	   			row++;
	   			col = basis.indexOf(n, 0);
				A.write(row, col, N.getAsBigRational(current_class-1));
				basis.computatble(col);
				A.toBeUpdated(row, col);					
				B.write(row, col, qnm.getDelayAsBigRational(current_class -1 ));
				for( int k = 1; k <= M; k++) {
					col = basis.indexOf(n, k);
					B.write(row, col, qnm.getDemandAsBigRational( k - 1, current_class -1 ));					
				}
	   		}
	   	}
	}



public static void generateAB2(PopulationVector N, int current_class) throws InternalErrorException {
 	A.reset();
 	B.reset();
 	basis.reset_uncomputables();
 			
   	int row = -1;
   	int col = 0;
   	PopulationChangeVector n;
   	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
   		if(n.sumTail(current_class-1) > 0) {  //potential negative population
   			for(int k = 0; k <= M; k++) {
   				row++;
   				col = basis.indexOf(n,k,current_class);
   				A.write(row, col, BigRational.ONE);
   				basis.computatble(col);
   				//System.out.println("Fisrt n: " + n);
   				//System.out.println("row: " + row);
   				if(n.sumTail(current_class) > 0) {  //negative population
   					//System.out.println("1");
   					col = basis.indexOf(n, k,current_class);
   					//System.out.println("col: " + col);
   					B.write(row, col, BigRational.ONE);
   				} else {
   					//System.out.println("2");
   					n.minusOne(current_class);    					
   					col = basis.indexOf(n, k,current_class);
   					//System.out.println("col: " + col);
   					B.write(row, col,BigRational.ONE);
   					n.restore();
   				}    				
   			}
   		} else {
   			if(n.sumHead(current_class - 2) < M) {    //TODO changed to - 2 from - 1, check it dosn't break it
   				for(int k = 1; k <= M; k++) {
   					//add CE corresponding to G(0, N - n) for each queue k
   					row++;
   					col = basis.indexOf(n, k,current_class);
   					A.write(row, col, BigRational.ONE);
   					basis.computatble(col);
   					col = basis.indexOf(n, 0,current_class);
   					A.write(row, col, BigRational.ONE.negate());
   					basis.computatble(col);
   					for(int s = 1; s <= current_class - 1; s++) {  //TODO can change to R - 1 ? No, some coefficients in B
   						n.plusOne(s);	
   						//System.out.println("n: " + n);
   						col = basis.indexOf(n, k,current_class);
   						n.restore();
   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
   						basis.computatble(col);
   						//System.out.println("k-1,s-1: " + qnm.getDemandAsBigRational(k-1, s-1));
   					}
   					col = basis.indexOf(n, k,current_class);
  					B.write(row, col, qnm.getDemandAsBigRational(k-1, current_class-1));
  				}
   				for(int s = 1; s < current_class; s++) {
   					//add PC  corresponding to G(0, N - n) for each class s less than the current class
   					row++;
    				col = basis.indexOf(n, 0,current_class);
   					A.write(row, col, N.getAsBigRational(s-1).subtract(n.getAsBigRational(s-1))); 
   					basis.computatble(col);
   					n.plusOne(s);
   					col = basis.indexOf(n, 0,current_class);
   					System.out.println("s: " + s);
   					System.out.println("n: " + n);
   					A.write(row, col, qnm.getDelayAsBigRational(s-1).negate());
   					basis.computatble(col);
   					for(int k = 1; k <= M; k++) { //loop over all queues k (= sum)
   						col = basis.indexOf(n, k,current_class);
   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
   						basis.computatble(col);
   					}
   					n.restore();
   				}
   			}    			    			
   		}
   	}
   	for(int i = 0; i < MiscFunctions.binomialCoefficient(M + R - 1 , M); i++)  { //loop over all possible population changes n
   		//add PC of class 'current_class'
   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
   		if(n.sumTail(current_class-1) <= 0) {  // TODO remove <
   			row++;
   			col = basis.indexOf(n, 0,current_class);
			A.write(row, col, N.getAsBigRational(current_class-1));
			basis.computatble(col);
			A.toBeUpdated(row, col);					
			B.write(row, col, qnm.getDelayAsBigRational(current_class -1 ));
			for( int k = 1; k <= M; k++) {
				col = basis.indexOf(n, k,current_class);
				B.write(row, col, qnm.getDemandAsBigRational( k - 1, current_class -1 ));					
			}
   		}
   	}
}
}