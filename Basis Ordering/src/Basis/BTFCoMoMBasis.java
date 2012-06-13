package Basis;

import java.util.Collections;

import DataStructures.QNModel;

public class BTFCoMoMBasis extends CoMoMBasis {

	public BTFCoMoMBasis(QNModel m) {
		super(m);
	}
	
	public BTFCoMoMBasis( int classes, int queues) {
		super(classes, queues);
	}
	
	@Override
	protected void sort() {
		Collections.sort(order);
	}

}
