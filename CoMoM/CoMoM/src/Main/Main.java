package Main;

import Basis.BTFCoMoMBasisMLorder;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import DataStructures.Tuple;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import Matrix.Matrix;
import Matrix.StandardMatrix;
import Utilities.MiscFunctions;

public class Main {
	
	private static Matrix A, B;
	
	private static CoMoMBasis basis;
	
	private static QNModel qnm;
	private static int M,R;	
	
	private static PopulationVector current_N;
	private static PopulationVector target_N;
	
	public static void main(String[] args) throws InputFileParserException, InternalErrorException {
		
		qnm = new QNModel("test_model_1.txt");
		M = qnm.M;
		R = qnm.R;	
		target_N = qnm.N;
		
		basis = new BTFCoMoMBasisMLorder(qnm);		
		
		A = new StandardMatrix(basis.getSize());
		B = new StandardMatrix(basis.getSize());		
		
		solve();
		
	}
	
	public static void solve() throws InternalErrorException {
		
		current_N = new PopulationVector(0,R);
				
		for(int current_class = 1; current_class <= R; current_class++) {
			System.out.println("Working on class " + current_class);
			current_N.plusOne(current_class);
			solveForClass(current_class);
			if(current_class < R) {
				basis.initialiseForClass(current_class + 1);
			}
		}
	}
	
	public static void solveForClass(int current_class) throws InternalErrorException {
		generateAB(current_N, current_class);
		System.out.println("Intialising A and B:");
		System.out.println("A:");
		A.print();
		System.out.println("B:");
		B.print();
		for(int current_class_population = current_N.get(current_class - 1); 
				current_class_population <= target_N.get(current_class - 1); 
				current_class_population++ ) {
			
			System.out.println("Current Population: " + current_N);
			
			//Solve the system		
			System.out.println("Solving System...\n");
						
			if(current_class_population < target_N.get(current_class - 1)) {
				System.out.println("Updated A: ");
				A.update();
				A.print();
				current_N.plusOne(current_class);
			}
		}
		
	}
	
	 public static void generateAB(PopulationVector N, int current_class) throws InternalErrorException {
	 	A.reset();
	 	B.reset();
	 			
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
	   			if(n.sumHead(current_class - 1) < M) {
	   				for(int k = 1; k <= M; k++) {
	   					//add CE corresponding to G(0, N - n) for each queue k
	   					row++;
	   					col = basis.indexOf(n, k);
	   					A.write(row, col, BigRational.ONE);
	   					col = basis.indexOf(n, 0);
	   					A.write(row, col, BigRational.ONE.negate());
	   					for(int s = 1; s <= current_class - 1; s++) {
	   						n.plusOne(s);	
	   						//System.out.println("n: " + n);
	   						col = basis.indexOf(n, k);
	   						n.restore();
	   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
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
	   					n.plusOne(s);
	   					col = basis.indexOf(n, 0);
	   					A.write(row, col, qnm.getDelayAsBigRational(s-1).negate());
	   					for(int k = 1; k <= M; k++) { //loop over all queues k (= sum)
	   						col = basis.indexOf(n, k);
	   						A.write(row, col, qnm.getDemandAsBigRational(k-1, s-1).negate());
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
				A.toBeUpdated(row, col);					
				B.write(row, col, qnm.getDelayAsBigRational(current_class -1 ));
				for( int k = 1; k <= M; k++) {
					col = basis.indexOf(n, k);
					B.write(row, col, qnm.getDemandAsBigRational( k - 1, current_class -1 ));					
				}
	   		}
	   	}
	}
}
