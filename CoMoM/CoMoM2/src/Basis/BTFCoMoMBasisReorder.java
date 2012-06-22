package Basis;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import Utilities.MiscFunctions;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class BTFCoMoMBasisReorder extends BTFCoMoMBasis{

	public BTFCoMoMBasisReorder(QNModel qnm) {
		super(qnm);
	}

	
	public int indexOf(PopulationChangeVector n, int m, int current_class) throws InternalErrorException {
		System.out.println("\n indexOf \n");
		int total_moved = MiscFunctions.binomialCoefficient(M + current_class - 1 , M); //correct
		System.out.println("total_moved: " + total_moved);
		System.out.println("current_class: " + current_class);
		int position = 0;
		int moved_count = 0;
		
		for(int index = 0; index < order.size(); index++) {
			System.out.println("moved n: " + order.get(index) + ", moved count = " + moved_count );
			if(order.get(index).sumTail(current_class-1) <= 0) {
				moved_count++;
			}			
			if(order.get(index).equals(n)) {
				if(n.sumTail(current_class-1) <= 0){
					if(m == 0) {
						//return MiscFunctions.binomialCoefficient(M + R - 1 , M)*(M+1) - total_moved +  moved_count - 1;
						return MiscFunctions.binomialCoefficient(M + R - 1 , M)*( M + 1 ) - total_moved +  moved_count - 1;  //correct
					} else {
						return position + m - 1;
					}
				} else {
					return position + m;
				}
			}
			if( order.get(index).sumTail(current_class-1) <= 0) {
				position +=  M;
			} else {
				position += M + 1;
			}
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return -1;
	}
	
	/**
	 * Initialises the basis for population (0,...0)
	 * @throws InternalErrorException 
	 */	
	@Override
	public void initialiseBasis() throws InternalErrorException {
		
		System.out.println("Intialising Basis");		
		// Negative populations have normalising constant equal to ZERO
		for( int i = 0 ; i < size; i++) {
			basis.add(i, BigRational.ZERO);
			previous_basis.add(i, BigRational.ZERO);
		}
		
		// Zero populations have normalising constant equal to ONE
		PopulationChangeVector zero_population = new PopulationChangeVector(0,R);
		for(int k = 0; k <= M; k++) {
			int index = indexOf(zero_population,k,1);
			basis.set(index, BigRational.ONE);	
		}	
	}
	@Override
	public void initialiseForClass(int current_class) throws InternalErrorException {
		
		
		if(current_class > 1) {
			
			ArrayList<BigRational> new_basis = new ArrayList<BigRational>(basis);
		
			PopulationChangeVector n;
		
			Iterator<PopulationChangeVector> it = order.iterator();
			while(it.hasNext()) {
				n = it.next();
				for(int k = 0; k <= qnm.M; k++ ) {
					new_basis.set(indexOf(n, k, current_class), basis.get(indexOf(n,k,current_class -1)));
							
				}
			}
			basis = new_basis;
		}
		
		/*
		BigRational value;
		ArrayList<BigRational> to_append = new ArrayList<BigRational>();
		if(current_class > 0) {			
		
			int index = 0;	
			int end = basis.size();
			while(index < end) {
				if(order.get(index).sumTail(current_class-1) <= 0) {
					to_append.add(basis.get(index));
					basis.remove(index);
					end--;
				}
			}
			basis.addAll(to_append);
			
		}
		*/
	}		
		
}
