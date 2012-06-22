package Basis;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class CoMoMReorderingBasis extends CoMoMBasis {
	
	
	private int[] order_delta;
	
	public CoMoMReorderingBasis(QNModel m) {
		super(m);
		order_delta = new int[size];
	}	

	@Override
	public void initialiseForClass(int current_class) throws InternalErrorException {
		update_delta(current_class);

	}
	
	private void update_delta(int current_class) {
		
	}

}
