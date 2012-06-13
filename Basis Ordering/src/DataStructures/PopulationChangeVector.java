package DataStructures;

/**
 * This class implements the PopulationChangeVector object, which are used in 
 * in the definition of the CoMoM basis. It extends an EnhancedVector object.
 *
 * @author Jack Bradshaw, 2012
 */
public class PopulationChangeVector extends EnhancedVector implements Comparable<PopulationChangeVector>  {

	 /**
     * Creates an empty PopulationChangeVector object.
     */
    public PopulationChangeVector() {
        super();
    }

    /**
     * Creates an PopulationChangeVector with content equal to the given matrix.
     *
     * @param P The matrix containing the vector elements
     */
    public PopulationChangeVector(Integer[] P){
        super(P);
    }

    /**
     * Creates a new PopulationChangeVector of specific length, where
     * all elements are equal to a specific value.
     *
     * @param k The value of all elements
     * @param length The length of the PopulationChangeVector
     */
    public PopulationChangeVector(int k, int length) {
        super(k, length);
    }
    
    /**
     * This method is used to count the elements of the EnhancedVvector that are 
     * greater than zero.
     *
     * @return The number of non-zero elements
     */
    public int countNonZeroElements() {
        int count = 0;
        for (Integer el : this) {
            if (el > 0) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * This method returns a copy of the current PopulationChangeVector object. Position
     * and delta stacks are disregarded.
     *
     * @return Copy of the initial PopulationChangeVector object.
     */
    @Override
    public PopulationChangeVector copy() {
       PopulationChangeVector c = new PopulationChangeVector();
        this.copyTo(c);
        return c;
    }
    
    /**
     * Compares two PopulationCahngeVector objects according the order required
     * for the fine grain Block Triangular Form.
     * First vectors are compared at the number of non zero elements (h)
     * Then the position of those elements
     * Then the value at those positions, left to right
     * leftmost non-zero is the smaller.
     *
     * @param o The other EnhancedVector object
     * @return -1 if this < o, 0 if this = o, 1 if this > o
     */
    //@Override
    public int compareTo1(PopulationChangeVector o) {
    	PopulationChangeVector v1 = this;
    	PopulationChangeVector v2 = o;

    	if (v1.size() < v2.size()) {
            return -1;
        } else if (v1.size() > v2.size()) {
            return 1;
        }else { 
        	//Vectors have same length
        	//Check 1: Compare number of non zero elements
        	if(v1.countNonZeroElements() < v2.countNonZeroElements()) {
        		return -1;
        	} else if (v1.countNonZeroElements() > v2.countNonZeroElements()) {
        		return 1;
        	}else {
        		// Vectors have the same number of non zero elements
        		// Check 2: Compare non zero element positions interpreted as binary numbers
        		int value1 = 0, value2 = 0;
        		int size = v1.size();
        		for( int i = 0; i < size - 1; i++) { //sum over first R - 1 positions
        			if(v1.get( size - 2 - i) > 0 ) value1 += (int) Math.pow(2,i);
        			if(v2.get( size - 2 - i) > 0 ) value2 += (int) Math.pow(2,i);
        		}
        		if(value1 < value2) {
        			return -1;
        		} else if(value1 > value2) {
        			return 1;
        		} else {
        			// Vectors have non zero elements in same position
        			// Check 3: Compare values of non zero elements, left to right
        			for( int j = 0; j < v1.size() - 1; j++) { //sum over first R - 1 positions
            			if(v1.get(j) < v2.get(j) ) return -1;
            			if(v1.get(j) > v2.get(j) ) return 1;
            		}   			
        		return 0;	
        		}
        	}
        }
    }
    
    /**
     * Compares two PopulationCahngeVector objects according the order required
     * for the fine grain Block Triangular Form.
     * First vectors are compared at the number of non zero elements (h)
     * Then the position of those elements
     * Then the value at those positions, left to right
     * leftmost non-zero is the smaller.
     *
     * @param o The other EnhancedVector object
     * @return -1 if this < o, 0 if this = o, 1 if this > o
     */
    @Override
    public int compareTo(PopulationChangeVector o) {
    	PopulationChangeVector v1 = this;
    	PopulationChangeVector v2 = o;

    	if (v1.size() < v2.size()) {
            return -1;
        } else if (v1.size() > v2.size()) {
            return 1;
        }else { 
        	//Vectors have same length
        	//Check 1: Compare number of non zero elements
        	if(v1.countNonZeroElements() < v2.countNonZeroElements()) {
        		return -1;
        	} else if (v1.countNonZeroElements() > v2.countNonZeroElements()) {
        		return 1;
        	}else {
        		// Vectors have the same number of non zero elements
        		// Check 2: Compare non zero element positions interpreted as binary numbers
        		for (int j = 0; j < v1.size(); j++) {
        	         if (v1.get(j) == 0 && v2.get(j) > 0 ) {
        	             return 1; // v1 has the left-most zero
        	         }
        	         if (v1.get(j) > 0 && v2.get(j) == 0) {
        	             return -1;
        		     }
        		}        		
        		// Vectors have non zero elements in same position
        		// Check 3: Compare values of non zero elements, left to right
        		for( int j = 0; j < v1.size() - 1; j++) { //sum over first R - 1 positions
            		if(v1.get(j) < v2.get(j) ) return -1;
            		if(v1.get(j) > v2.get(j) ) return 1;
            	}   			
        		return 0;	   		
        	}
        }
    }
    public int sum(int from, int to) {
    	int total = 0;
    	for(;from <to; from++) {
    		total += get(from);
    	}
    	return total;
    }
    
    //index inclusive
    public int sumTail(int index) {
    	return sum(index, size());    
    }
    
    //index inclusive
    public int sumHead(int index) {
    	return sum(0, index +1);    
    }
}
