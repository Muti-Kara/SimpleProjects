
public class Network {
	
	
	Matrix init = new Matrix(0, 0); // used for generating matrixes from array lists
	final int[] structure = new int[] {
			4, 12, 12, 4 // structure of neural network
	}; 
	
	Matrix[] w = new Matrix[structure.length]; // matrixes of weights, and vectors of biases
	Matrix[] b = new Matrix[structure.length]; 
	
	
	public Network(Network parent) {
		
		for(int i = 1; i < structure.length; i++) {
			w[i] = parent.w[i].generate();
			b[i] = parent.b[i].generate();
		}
		
	}
	
	
	public Network(){
		for(int i = 1; i < structure.length; i++) {
			w[i] = new Matrix(structure[i], structure[i - 1], true);
			b[i] = new Matrix(structure[i], 1, true);
		}
	}
	
	
	public double[] feedForward(double[] situation) {
		Matrix[] a = new Matrix[structure.length]; // node's activation after values.
		a[0] = init.getVector(situation);
		for(int layer = 1; layer < structure.length - 1; layer++) {
			a[layer] = (w[layer].dot(a[layer - 1])).sum(b[layer]).func(); // forward propagation.
		}
		a[structure.length - 1] = (w[structure.length - 1].dot(a[structure.length - 2])).sum(b[structure.length - 1]); 
		// last stage we didn't take sigmoid instead we'll use softmax
		
		double[] output = a[structure.length - 1].T().matrix[0];
		double softmax = 0;
		for(int i = 0; i < output.length; i++) {
			output[i] = Math.exp(output[i]);
		}
		for(int i = 0; i < output.length; i++) {
			softmax += output[i];
		}
		for(int i = 0; i < output.length; i++) {
			output[i] /= softmax;
		}
		
		return output;
	}
	
	
}