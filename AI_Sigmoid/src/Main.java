import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		NeuralNetwork NN = new NeuralNetwork(1, 5);
		
		int epochs = 30;
		int steps = 10;
		double learningrate = 1;
		double regularization = 0.01;
		
		NN.setEpochs(epochs);
		NN.setLearningrate(learningrate);
		NN.setStochastic(steps);
		NN.setRegularization(regularization);
		
		//NN.train();
		NN.predict();
		
		
	}

}
