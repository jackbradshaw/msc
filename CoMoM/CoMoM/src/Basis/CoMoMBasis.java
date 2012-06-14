package Basis;

import java.util.ArrayList;

import DataStructures.MultiplicitiesVector;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;
import Utilities.MiscFunctions;

public class CoMoMBasis extends Basis{		
	
	
	/**
	 * Data Structure to hold the ordering of PopualtionChangeVectors
	 */
	protected ArrayList<PopulationChangeVector> order;
	
	
	/**
	 * Constructor
	 * @param m The Queueing Network Model under study
	 */
	public CoMoMBasis(QNModel m) {
		super(m);		
		initialise();
	}	
	
	private void initialise() {
		order = new ArrayList<PopulationChangeVector>();
		generate();
		//Will sort the order if sort() is implemented
		sort();
	}
	
	/**
	 * Fills the list 'order' with all positive vectors of the form (n_1, n_2, ..., n_(R-1), 0 ),
	 * where n_1 + n_2 + ... + n_(R-1) <= M
	 */
	private void generate() {
		
		PopulationChangeVector v = new PopulationChangeVector(0,R);		
		order.add(v);
		int current_position = 1;
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < R - 1; j++) {
				for(int k = current_position - MiscFunctions.binomialCoefficient(R - 1 - j + i - 1, i); 
					k < current_position; k++) {
						v = order.get(k);						
						v.plusOne(j+1);
						order.add(v.copy());						
						v.restore();
				}				
			}
			current_position += MiscFunctions.binomialCoefficient(R - 1 + i , i + 1);			
		}
	}
	
	/**
	 * Can be overridden to sort the order
	 */
	protected void sort() {
		//Do nothing, but subclasses can if they want
		return;
	}
	
	/**
	 * Finds the index of Normalising Constant G(N-n, m) as determined by n and m
	 * @param n Change in population vector
	 * @param m Multiplicity vector
	 * @return Index of G(N-n, m) in basis
	 * @throws InternalErrorException
	 */
	public int indexOf(PopulationChangeVector n, MultiplicitiesVector m) throws InternalErrorException {		
		int queue_added = m.whichSingleQueueAdded();
		return indexOf(n,queue_added);		
	}
	
	/**
	 * Finds the index of Normalising Constant G(N-n, m) as determined by n and m
	 * @param n Change in population vector
	 * @param m index of queue added (0 for no queue added)
	 * @return Index of G(N-n, m) in basis
	 * @throws InternalErrorException
	 */
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {
		int population_position = order.indexOf(n);
		int queue_added = m;
		//order multiplicities 1,2,3,...M,0			
		int multiplicity_order;
		if(queue_added == 0) multiplicity_order = M;
		else multiplicity_order = queue_added - 1;		
		return population_position + multiplicity_order* MiscFunctions.binomialCoefficient(M + R - 1, M);
	}
	
	/**
	 * @param index index in order
	 * @return Vector store in 'order' at 'index'
	 */
	public PopulationChangeVector getPopulationChangeVector(int index) {
		return order.get(index);
	}
	
	
	/**
	 * Prints the vectors in 'order'
	 */
	public void print() {
		int total = 0;
		 for (PopulationChangeVector m : order) {
			 m.print();			 
			 total++;
		 }
		 System.out.println("total: " + total);
	}

	/**
	 * Initialises the basis for the begin of the recursion on next class
	 * @param next_class the next class to be recursed on
	 */	
	@Override
	public void initialiseForClass(int next_class) {
		System.out.println("Intialisng for class " + next_class);	
	}
	
	/**
	 * Calculates the size of the basis to be store in variable size
	 */
	@Override
	public void setSize() {
		size  = (M + 1)*MiscFunctions.binomialCoefficient(M + R - 1 , M);		
	}		
}
