package DataStructures;

/**
 * This class implements the PopulationVector object, which is used to
 * store a populations vector. It extends an EnhancedVector object.
 *
 * @author Michail Makaronidis, 2010
 */
public class PopulationVector extends EnhancedVector {

    /**
     * Creates an empty PopulationVector object.
     */
    public PopulationVector() {
        super();
    }

    /**
     * Creates an PopulationVector with content equal to the given matrix.
     *
     * @param P The matrix containing the vector elements
     */
    public PopulationVector(Integer[] P){
        super(P);
    }

    /**
     * Creates a new PopulationVector of specific lenth, where
     * all elements are equal to a specific value.
     *
     * @param k The value of all elements
     * @param length The length of the PopulationVector
     */
    public PopulationVector(int k, int length) {
        super(k, length);
    }

    /**
     * This method fills the current PopulationVector with the values
     * contained in a QNModel object (quening network model).
     *
     * @param qnm The QNModel object that represents the queing network we are
     * working on
     */
    public void fillFromQNModel(QNModel qnm) {
        this.fillFromMyVector(qnm.N);
    }


    /**
     * This method returns a copy of the current PopulationVector object. Position
     * and delta stacks are disregarded.
     *
     * @return Copy of the initial PopulationVector object.
     */
    @Override
    public PopulationVector copy() {
        PopulationVector c = new PopulationVector();
        this.copyTo(c);
        return c;
    }
    @Override
    public PopulationVector addVec(EnhancedVector b) {
        return (PopulationVector) super.addVec(b);
    }

}
