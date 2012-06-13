package Basis;

import Utilities.MiscFunctions;
import DataStructures.PopulationChangeVector;
import Exceptions.InternalErrorException;

public class BTFCoMoMBasisMLorder extends BTFCoMoMBasis{

	public BTFCoMoMBasisMLorder( int classes, int queues) {
		super(classes, queues);
	}

	@Override
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {
		int population_position = order.indexOf(n);
		int queue_added = m;
		//order multiplicities 1,2,3,...M,0
		//System.out.println("pop_pos: " + population_position);
		//System.out.println("queue_added: " + queue_added);	
		
		
		if(queue_added == 0) {
			//System.out.println("index: " + ( MiscFunctions.binomialCoefficient(M + R - 1 , M)*M + population_position));
			return MiscFunctions.binomialCoefficient(M + R - 1 , M)*M + population_position;
		}
 		
		//System.out.println("index: " + ( population_position * (M-1) + queue_added - 1));
		return population_position * M + queue_added - 1;	
	}
}
