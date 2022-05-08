import java.util.ArrayList;
import java.util.Random;

public class Matrix {
	double[][] matrix;
	int col, row;
	
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
		Random rand = new Random();
		for(int r = 0; r < row; r++)
			for(int c = 0; c < col; c++)
				matrix[r][c] = rand.nextDouble();
		
		this.col = col;
		this.row = row;		
	}
	
	public Matrix createClone(Matrix B) {
		Matrix C = new Matrix(B.row, B.col);
		for(int r = 0; r < B.row; r++) {
			for(int c = 0; c < B.col; c++) {
				C.matrix[r][c] = B.matrix[r][c];
			}
		}
		return B;
	}
	
	public Matrix getVector(ArrayList<Double> a) {
		Matrix C = new Matrix(a.size(), 1);
		for(int r = 0; r < C.row; r++) {
			C.matrix[r][0] = a.get(r);
		}
		return C;
	}
	
	public Matrix getVector(int c) {
		Matrix C = new Matrix(col, 1);
		for(int r = 0; r < C.row; r++) {
			C.matrix[r][0] = matrix[c][r];
		}
		return C;
	}
	
	public Matrix getMatrix(ArrayList<ArrayList<Double>> M) {
		int ro = M.size();
		int co = M.get(0).size();
		Matrix C = new Matrix(ro, co);
		for(int r = 0; r < ro; r++) {
			for(int c = 0 ; c < co; c++) {
				C.matrix[r][c] = M.get(r).get(c);
			}
		}
		return C;
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
	
	public Matrix sub(Matrix B) {
		if(!(row == B.row && col == B.col)) {
			System.out.println("Matrix sub error. Size Mismatch.");
			return null;
		}
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] - B.matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix ewProd(Matrix B) {
		if(!(row == B.row && col == B.col)) {
			System.out.println("Matrix exProd error. Size Mismatch.");
		}
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] * B.matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix sProd(double b) {
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] * b;
			}
		}
		return C;
	}
	
	public Matrix sSum(double b) {
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] + b;
			}
		}
		return C;
	}
	
	public Matrix ewDiv(Matrix B) {
		if(!(row == B.row && col == B.col)) {
			System.out.println("Matrix sum error. Size Mismatch.");
			return null;
		}
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = matrix[r][c] / B.matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix T() {
		Matrix C = new Matrix(col, row);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[c][r] = matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix d_func() {
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = (1 - matrix[r][c]) * matrix[r][c];
			}
		}
		return C;
	}
	
	public Matrix func() {
		Matrix C = new Matrix(row, col);
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++) {
				C.matrix[r][c] = activation(matrix[r][c]); // I will use sigmoid as activation.
			}
		}
		return C;
	}
	
	public double activation(double d) {
		return 1 / (1 + Math.exp(-d));
	}
	
	public String toString() {
		String str = "";
		for(int r = 0; r < row; r++) {
			for(int c = 0; c < col; c++)
				str += matrix[r][c] + " ";
			str += "\n";
		}
		str += "\n\n";
		return str;
	}
	
}
