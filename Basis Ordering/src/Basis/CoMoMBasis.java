package Basis;

import java.util.ArrayList;

import DataStructures.MultiplicitiesVector;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;
import Utilities.MiscFunctions;

public class CoMoMBasis {
	
	protected QNModel model;
	
	protected int R;
	protected int M;
	
	protected ArrayList<PopulationChangeVector> order;
	
	public CoMoMBasis(QNModel m) {
		super();
		model = m;
		initialise();
	}
	
	public CoMoMBasis( int classes, int queues) {
		super();
		R = classes;
		M = queues;
		initialise();
	}
	
	
	private void initialise() {
		order = new ArrayList<PopulationChangeVector>();
		generate(M, R);
		sort();
	}
	
	private void generate(int level, int R) {
		
		PopulationChangeVector v = new PopulationChangeVector(0,R);
		R = R-1;
		order.add(v);
		int current_position = 1;
		for(int i = 0; i < level; i++) {
			for(int j = 0; j < R; j++) {
				for(int k = current_position - MiscFunctions.binomialCoefficient(R - j + i - 1, i); 
					k < current_position; k++) {
						v = order.get(k);
						//System.out.println("V: " + v);
						v.plusOne(j+1);
						order.add( v.copy());
						//System.out.println(v);
						v.restore();
				}				
			}
			current_position += MiscFunctions.binomialCoefficient(R + i , i + 1);
			//System.out.println("CP: " + current_position);
		}
	}

	protected void sort() {
		//Do nothing, but subclasses can if they want
		return;
	}
	
	public int indexOf(PopulationChangeVector n, MultiplicitiesVector m) throws InternalErrorException {		
		int queue_added = m.whichSingleQueueAdded();
		return indexOf(n,queue_added);		
	}
	
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {
		int population_position = order.indexOf(n);
		int queue_added = m;
		//order multiplicities 1,2,3,...M,0
		System.out.println("pop_pos: " + population_position);
		System.out.println("queue_added: " + queue_added);		
		
		int multiplicity_order;
		if(queue_added == 0) multiplicity_order = M;
		else multiplicity_order = queue_added - 1;
		System.out.println("index: " + (population_position + multiplicity_order*MiscFunctions.binomialCoefficient(M + R - 1 , M)));
		return population_position + multiplicity_order*MiscFunctions.binomialCoefficient(M + R - 1 , M);		
	}
	
	public PopulationChangeVector getPopulationChangeVector(int index) {
		return order.get(index);
	}
	
	public void print() {
		int total = 0;
		 for (PopulationChangeVector m : order) {
			 m.print();			 
			 total++;
		 }
		 System.out.println("total: " + total);
	}
	
		
}
