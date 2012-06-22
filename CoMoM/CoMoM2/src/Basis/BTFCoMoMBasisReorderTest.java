package Basis;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Utilities.MiscFunctions;

import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class BTFCoMoMBasisReorderTest {
	
	private BTFCoMoMBasisReorder basis;
	private QNModel qnm; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		qnm = new QNModel("test_model_2.txt");
		basis = new BTFCoMoMBasisReorder(qnm);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InternalErrorException {
		
		int size = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M)*( qnm.M + 1 );
		
		PopulationChangeVector n;
		int value = 0;
		for(int current_class = 1; current_class <= qnm.R; current_class++ ) {
					
			Iterator<PopulationChangeVector> it = basis.getOrder().iterator();
			while(it.hasNext()) {
				n = it.next();
				for(int k = 0; k <= qnm.M; k++ ) {
					value = basis.indexOf(n, k, current_class);
					assertTrue("Exceeded upper bound, with n = " + n + " and queue = " + k + ", value = " + value , value < size);
				}
			}
		}
		
	}

}
