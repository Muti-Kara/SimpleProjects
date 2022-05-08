import java.io.FileNotFoundException;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException{
		Maze m = new Maze(17, 22);
		m.solve(1,1);
		m.Out();
	}
	
}

