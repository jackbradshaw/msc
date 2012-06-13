package Main;

import java.util.ArrayList;
import java.util.Collections;

import Basis.BTFCoMoMBasis;
import Basis.CoMoMBasis;
import DataStructures.PopulationChangeVector;
import DataStructures.MultiplicitiesVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import MatrixBuild.CoMoMAB;
import Utilities.MiscFunctions;

public class Main {

	static ArrayList<PopulationChangeVector> L;
	/**
	 * @param args
	 * @throws InternalErrorException 
	 */
	public static void main(String[] args) throws InternalErrorException {
		CoMoMBasis b = new CoMoMBasis(3,3);
		//b.print();
		
		//b = new BTFCoMoMBasis(4,3);
		//b.print();
		
		Integer[] A = {1,1,1,0};
		Integer[] B = {0,0,1,0};
		PopulationChangeVector n = new PopulationChangeVector(A);
		MultiplicitiesVector m = new MultiplicitiesVector(B);
		try {
			System.out.println(b.indexOf(n, m));
		} catch(InternalErrorException e){}

		QNModel qnm;
		try {
			qnm = new QNModel("matlabtest.txt");
			System.out.println("Parsing Sucessful! R: " + qnm.R);
			System.out.println("Parsing Sucessful! M: " + qnm.M);
			
			Integer[] NA = {6,1};
			PopulationVector N = new PopulationVector(NA);
			
			CoMoMAB matrices = new CoMoMAB(qnm);
			matrices.initialise();
			matrices.generateAB(N,1);
			matrices.print();
		} catch(InputFileParserException e){
			System.out.println("Parsing Failed!");
		}
		
		
	}

}
