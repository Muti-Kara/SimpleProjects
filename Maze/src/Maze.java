import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
	ArrayList<ArrayList<Integer>> maze = new ArrayList<ArrayList<Integer>>();
	Stack<int[]> turnout = new Stack<int[]>();
	int x, y, ex, ey;
	Maze(int ex, int ey) throws FileNotFoundException{
		this.ex = ex; this.ey = ey; 
		Scanner inp = new Scanner(new File("Maze.txt"));
		while(inp.hasNextLine()) {
			String str = inp.nextLine();
			Scanner in = new Scanner(str);
			ArrayList<Integer> line = new ArrayList<Integer>();
			while(in.hasNextInt()) {
				line.add(in.nextInt());
			}
			maze.add(line);
			in.close();
		}
	}
	
	void Out() throws FileNotFoundException {
		PrintWriter out = new PrintWriter("Solution.txt");
		for(ArrayList<Integer> line : maze) {
			for(int j : line) {
				out.print(j+" ");
			}
			out.println();
		}
		out.close();
	}
	
	void solve(int x, int y) {
		this.x = x;
		this.y = y;
		solve();
	}
	
	void solve(){
		if(x == ex && y == ey) { 
			maze.get(x).set(y, 3);
			return;
		}
		int cases = 0, wturn = 0;
		if(maze.get(x+1).get(y) == 0) {
			cases = 1;
			wturn = 1;
		}
		if(maze.get(x-1).get(y) == 0) {
			wturn = 2;
			if(cases == 0) {
				cases = 2;
			}else {
				cases = 5;
			}
		}
		if(maze.get(x).get(y+1) == 0) {
			wturn = 3;
			if(cases == 0) {
				cases = 3;
			}else {
				cases = 5;
			}
		}
		if(maze.get(x).get(y-1) == 0) {
			wturn = 4;
			if(cases == 0) {
				cases = 4;
			}else {
				cases = 5;
			}
		}
		if(cases == 0) {
			maze.get(x).set(y, 4);
			if(turnout.isEmpty()) {
				maze.get(x).set(y, 5);
				return;
			}
			int[] turn = turnout.pop();
			x = turn[0];
			y = turn[1];
			solve();
		}
		if(cases == 5) {
			maze.get(x).set(y, 2);
			int[] turn = new int[2];
			turn[0] = x;
			turn[1] = y;
			turnout.push(turn);
			switch(wturn) {
				case 1:
					x++;
					break;
				case 2:
					x--;
					break;
				case 3:
					y++;
					break;
				case 4:
					y--;
					break;
			}
			solve();
		}
		maze.get(x).set(y, 3);
		switch(cases) {
			case 1:
				x++;
				break;
			case 2:
				x--;
				break;
			case 3:
				y++;
				break;
			case 4:
				y--;
				break;
		}
		solve();
	}
}
	
	

