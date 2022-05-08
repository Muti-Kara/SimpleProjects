import java.util.Random;

public class Matrix {
	static double learningrate = 3.0;
	double[][] matrix;
	int col, row;
	Random rand = new Random();
	
	public Matrix(int row, int col) {
		matrix = new double[row][col];
		for(int r = 0; r < row; r++)
			for(int c = 0; c < col; c++)
				matrix[r][c] = 0.0;
		
		this.col = col;
		this.row = row;		
	}
	
	public Matrix(int row, int col, boolean random) {
		matrix = new double[row][col];
		for(int r = 0; r < row; r++)
			for(int c = 0; c < col; c++)
				matrix[r][c] = rand.nextGaussian() - .5;
		
		this.col = col;
		this.row = row;		
	}
	
	public static void decreaseRate() {
		learningrate *= 0.8;
	}
	
	public Matrix getVector(double[] a) {
		Matrix C = new Matrix(a.length, 1);
		for(int r = 0; r < C.row; r++) {
			C.matrix[r][0] = a[r];
		}
		return C;
	}
	
	public Matrix T() {
		Matrix T = new Matrix(col, row);
		for(int r = 0; r < row; r++) 
			for(int c = 0; c < col; c++) 
				T.matrix[c][r] = matrix[r][c];
		return T;
	}
	
	public Matrix generate_from() {
		Matrix X = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				X.matrix[r][c] = matrix[r][c] + (rand.nextGaussian() - 0.5) * learningrate;
			}
		}
		return X;
	}
	
	public Matrix generate_mutation() {
		Matrix X = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				X.matrix[r][c] = matrix[r][c] + (rand.nextDouble() - 0.5) * 10;
			}
		}
		return X;
	}
	
	public Matrix generate() {
		
		double d = rand.nextDouble();
		if(d < 0.6) return generate_from();
		else return generate_mutation();
		
	}
	
	public void scaleData() {
		double[] mins = new double[col];
		double[] maxs = new double[col];
		for(int c = 0; c < col; c++) {
			for(int r = 0; r < row; r++){
				if(matrix[r][c] > maxs[c]) {
					maxs[c] = matrix[r][c];
				}
				if(matrix[r][c] < mins[c]) {
					mins[c] = matrix[r][c];
				}
			}
		}
		for(int c = 0; c < col; c++) {
			maxs[c] -= mins[c]; // to avoid doing max - min for all elements of matrix
		}
		for(int c = 0; c < col; c++) {
			for(int r = 0; r < row; r++){
				matrix[r][c] = (matrix[r][c] - mins[c]) / maxs[c];
			}
		}
	}
	
	public Matrix dot(Matrix B) {
		if(this.col != B.row) {
			System.out.println("Matrix dot error. Size Mismatch :" + col + " " + B.row);
			return null;
		}
		Matrix C = new Matrix(this.row, B.col);
		for(int crow = 0; crow < this.row; crow++) {
			for(int ccol = 0; ccol < B.col; ccol++) {
				double c = 0;
				for(int sum = 0; sum < this.col; sum++) {
					c += matrix[crow][sum] * B.matrix[sum][ccol];
				}
				C.matrix[crow][ccol] = c;
			}
		}
		return C;
	}
	
	public Matrix sum(Matrix B) {
		if(!(row == B.row && col == B.col)) {
			System.out.println("Matrix sum error. Size Mismatch.");
			return null;
		}
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] + B.matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix func() {
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = activation(matrix[r][c]); // I will use ReLU as activation.
			}
		}
		return C;
	}
	
	public double activation(double d) {
		return Math.tanh(d);
	}
	
	public String toString() {
		String str = "";
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++)
				str += matrix[r][c] + " ";
			str += "\n";
		}
		return str;
	}
	
}
