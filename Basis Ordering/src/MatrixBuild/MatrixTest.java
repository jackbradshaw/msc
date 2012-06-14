package MatrixBuild;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Utilities.MiscFunctions;

import DataStructures.BigRational;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;

public class MatrixTest {
	
	CoMoMAB AB;
	QNModel qnm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {		
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateAB() throws InputFileParserException, InternalErrorException {
		
		qnm = new QNModel("test_model_1.txt");
		test_model_against_matlab_files(qnm, "C:\\Users\\Jack\\Desktop\\MSc Project\\Matlab\\08_comom_tse09\\test_matrices\\simple\\test_model_1");
		
		qnm = new QNModel("test_model_2.txt");
		test_model_against_matlab_files(qnm, "C:\\Users\\Jack\\Desktop\\MSc Project\\Matlab\\08_comom_tse09\\test_matrices\\simple\\test_model_2");
		
	}


private void test_model_against_matlab_files(QNModel qnm, String directory_path) throws InternalErrorException {
	System.out.println("Running Tests at " + directory_path);
	AB = new CoMoMAB(qnm);
	File directory = new File(directory_path);
	
	String contents[] = directory.list();
	
	for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
		PopulationVector N = qnm.N.copy();
		N.set(cur_class-1, 1);
		for(int i = cur_class; i < qnm.R; i++) {
			N.set(i,0);
		}
		System.out.println("Generating A and B for poulation: " + N + ", on class: " + cur_class);
		AB.initialise();
		AB.generateAB(N, cur_class);
		BigRational[][] A_from_file = readIntoArray(new File(directory_path, contents[2*(cur_class-1)]));
		BigRational[][] B_from_file = readIntoArray(new File(directory_path, contents[2*(cur_class-1)+1]));
		for(int i = 0; i < AB.getMatrixSize(); i++) {
			for(int j = 0; j < AB.getMatrixSize(); j++) {
				//System.out.println("Asserting on A...");
				Assert.assertEquals("A, class " + cur_class + ", position [" + i + "," +j +"]",A_from_file[i][j], AB.getA()[i][j]);
				//System.out.println("Asserting on B...");
				Assert.assertEquals("B class " + cur_class + ",position [" + i + "," +j +"]",B_from_file[i][j], AB.getB()[i][j]);
			}
		}		
		System.out.println("SUCCESSFUL MATCH!");
	}	
}

private BigRational[][] readIntoArray(File file) {
	
	Scanner input;
	BigRational array[][] = new BigRational[AB.getMatrixSize()][AB.getMatrixSize()];
	try {
		input = new Scanner(file);		
		try {
			int row = 0;
			int col = 0;
			while(input.hasNextLine()) {
				col = 0;
				String line = input.nextLine();
				String ints[] = line.split(",");
				for(String num : ints) {
					array[row][col] = new BigRational(num);
					col++;
				}
				row++;
			}			
		} catch (NoSuchElementException e) {
		
		}
	} catch (FileNotFoundException e) {
		System.out.println("File not opened!");		
	}
	return array;
}
		
}