import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class NeuralNetwork {
	ArrayList<ArrayList<Double>> inputs = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> answers = new ArrayList<ArrayList<Double>>();
	
	int[] structure; // structure of neural network
	
	Matrix[] w, b; // matrixes of weights, and vectors of biases
	Matrix inp, ans;
	
	Matrix init = new Matrix(0, 0); // used for generating matrixes from array lists
	
	int sizeOfData;
	int stochastic;
	int numOfLayers;
	double learningrate;
	double regularization;
	int epochs;

	public void setLearningrate(double learningrate) {
		this.learningrate = learningrate;
	}

	public void setStochastic(int stochastic) {
		this.stochastic = stochastic;
	}
	
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}
	
	public void setRegularization(double regularization) {
		this.regularization = regularization;
	}
	
	public NeuralNetwork(int numOfHiddenLayers, int neuronsPerLayer) throws FileNotFoundException {
		Scanner in = new Scanner(new File("Data.txt"));
		while(in.hasNextLine()) {
			ArrayList<Double> data = new ArrayList<Double>();
			ArrayList<Double> answer = new ArrayList<Double>();
			String str;
			Scanner inLine = new Scanner(in.nextLine());
			while(! (str = inLine.next()).equals(";")) {
				data.add((Double.parseDouble(str)));
			}
			while(inLine.hasNextDouble()) {
				answer.add(inLine.nextDouble());
			}
			inLine.close();
			inputs.add(data);
			answers.add(answer);
		}
		in.close();
		
		inp = init.getMatrix(inputs);
		ans = init.getMatrix(answers);
		
		inp.scaleData();
		
		sizeOfData = inp.row;
		
		numOfLayers = numOfHiddenLayers + 2;
		structure = new int[numOfLayers];
		
		structure[0] = inp.col;
		for(int layer = 1; layer <= numOfHiddenLayers; layer++) {
			structure[layer] = neuronsPerLayer;
		}
		structure[numOfLayers-1] = ans.col;		
		
	}
	
	public void train() throws FileNotFoundException {
		w = new Matrix[numOfLayers]; // weight between two layer. w[i].matrix[first][second] means weight between ith layer first node and i+1 th layer second
		for(int layer = 1; layer < numOfLayers; layer++) {
			w[layer] = new Matrix(structure[layer], structure[layer - 1], true);
		}
		
		b = new Matrix[numOfLayers]; // biases
		for(int layer = 1; layer < numOfLayers; layer++) {
			b[layer] = new Matrix(structure[layer], 1, true);
		}
		
		for(int trainer = 0; trainer < epochs; trainer++) {
			int step = sizeOfData / stochastic;
			int beginning = 0, end = 0;
			while(end < sizeOfData) {
				beginning = end;
				end += step;
				end = Math.min(end, sizeOfData);
				Matrix[] dw = new Matrix[numOfLayers]; // changes of each weight
				for(int layer = 1; layer < numOfLayers; layer++) {
					dw[layer] = new Matrix(structure[layer], structure[layer - 1]);
				}
				Matrix[] db = new Matrix[numOfLayers]; // changes of biases
				for(int layer = 1; layer < numOfLayers; layer++) {
					db[layer] = new Matrix(structure[layer], 1);
				}
				for(int atData = beginning; atData < end; atData++) {
					Matrix[] a = new Matrix[numOfLayers]; // node's activation after values.
				
					a[0] = inp.getVector(atData);
					for(int layer = 1; layer < numOfLayers; layer++) {
						a[layer] = (w[layer].dot(a[layer - 1])).sum(b[layer]).func(); // forward propagation.
					}
					
					Matrix[] e = new Matrix[numOfLayers]; // errors/cost function
					Matrix T = ans.getVector(atData);
					
					e[numOfLayers-1] = a[numOfLayers-1].sub(T); // derivative of cross entropy cost			
					for(int layer = numOfLayers - 2; layer > 0; layer--) {
						e[layer] = ((w[layer + 1].T()).dot(e[layer + 1])).ewProd(a[layer].d_func()); // back propagation.
					}
					
					for(int layer = 1; layer < numOfLayers; layer++) {
						db[layer] = db[layer].sum(e[layer]);
						dw[layer] = dw[layer].sum(e[layer].dot((a[layer - 1].T()))); //calculating total changes
					}
				}
				for(int layer = 1; layer < numOfLayers; layer++) {
					b[layer] = b[layer].sub((db[layer].sProd(learningrate))); //updating weights and biases
					w[layer] = (w[layer].sub((dw[layer].sProd(learningrate))));
				}
			}
		}
		
		Memorize();
		
	}
	
	public void Memorize() throws FileNotFoundException {
		PrintWriter memorize = new PrintWriter("Trained.txt");
		
		memorize.println(numOfLayers);
		
		for(int layer = 1; layer < numOfLayers; layer++) {
			memorize.append(((Integer) w[layer].row).toString() + "\n");
			memorize.append(((Integer) w[layer].col).toString() + "\n");
			memorize.append(w[layer].toString());
			memorize.append(b[layer].toString());
		}
		
		memorize.close();
	}
	
	public void Remember() throws FileNotFoundException {
		Scanner remember = new Scanner(new File("Trained.txt"));
		
		int size = remember.nextInt();
		
		w = new Matrix[size];
		b = new Matrix[size];
		
		for(int i = 1; i < size; i++) {
			int row = remember.nextInt();
			int col = remember.nextInt();
			w[i] = new Matrix(row, col);
			for(int r = 0; r < row; r++) {
				for(int c = 0; c < col; c++) {
					w[i].matrix[r][c] = Double.parseDouble(remember.next());
				}
			}
			b[i] = new Matrix(row, 1);
			for(int r = 0; r < row; r++) {
				b[i].matrix[r][0] = Double.parseDouble(remember.next());
			}
		}
		remember.close();
	}
	
	public void predict() throws FileNotFoundException {
		Remember();
		Scanner scan = new Scanner(new File("Target.txt"));
		int length = scan.nextInt();
		ArrayList<ArrayList<Double>> target = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < length; i++) {
			ArrayList<Double> STarget = new ArrayList<Double>();
			for(int j = 0; j < structure[0]; j++) {
				STarget.add(Double.parseDouble(scan.next()));
			}
			target.add(STarget);
		}
		for(int data = 0; data < length; data++) {
			Matrix[] a = new Matrix[numOfLayers]; // node's activation after values.
			a[0] = inp.getVector(target.get(data));
			for(int layer = 1; layer < numOfLayers; layer++) {
				a[layer] = (w[layer].dot(a[layer - 1])).sum(b[layer]).func(); // forward propagation.
			}
			System.out.println(a[numOfLayers-1]);
		}
	}
}