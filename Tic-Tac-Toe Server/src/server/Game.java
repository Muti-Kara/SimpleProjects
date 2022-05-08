package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Game {
	BufferedReader[] in;
	BufferedWriter[] out;
	char[][] game = new char[3][3];
	char turn = 'O';
	
	Game(BufferedReader[] in, BufferedWriter[] out){
		this.in = in;
		this.out = out;
	}
	
	public void send(String str, int who) {
		try {
			out[who].write(str);
			out[who].flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String str) {
		send(str, 0);
		send(str, 1);
	}
	
	public boolean isRunning() {
		for(int i = 0; i < 3; i++) {
			if(game[i][0] == game[i][1] && game[i][1] == game[i][2]) {
				if(game[i][0] == 'O') {
					send("\nYou've won!!\n", 0);
					send("\nYou've lost.\n", 1);
					return false;
				}
				if(game[i][1] == 'X') {
					send("\nYou've won!!\n", 1);
					send("\nYou've lost.\n", 0);
					return false;
				}
			}
		}
		for(int i = 0; i < 3; i++) {
			if(game[0][i] == game[1][i] && game[1][i] == game[2][i]) {
				if(game[0][i] == 'O') {
					send("\nYou've won!!\n", 0);
					send("\nYou've lost.\n", 1);
					return false;
				}
				if(game[1][i] == 'X') {
					send("\nYou've won!!\n", 1);
					send("\nYou've lost.\n", 0);
					return false;
				}
			}
		}
		
		if(game[0][0] == game[1][1] && game[1][1] == game[2][2]) {
			if(game[1][1] == 'O') {
				send("\nYou've won!!\n", 0);
				send("\nYou've lost.\n", 1);
				return false;
			}
			if(game[1][1] == 'X') {
				send("\nYou've won!!\n", 1);
				send("\nYou've lost.\n", 0);
				return false;
			}
		}
		
		if(game[0][2] == game[1][1] && game[1][1] == game[2][0]) {
			if(game[1][1] == 'O') {
				send("\nYou've won!!\n", 0);
				send("\nYou've lost.\n", 1);
				return false;
			}
			if(game[1][1] == 'X') {
				send("\nYou've won!!\n", 1);
				send("\nYou've lost.\n", 0);
				return false;
			}
		}
		boolean isFinish = false;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++)
				isFinish = isFinish || game[i][j] == '.';
		}
		if(!isFinish)
			send("\nGame has been finished.\n");
		else
			send("\nGame continues.\n");
		return isFinish;
	}
	
	public void startGame() {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				game[i][j] = '.';
	}
	
	public void printScreen() {
		send("Current game:\n");
		send("\t1\t2\t3\n");
		for(int i = 0; i < 3; i++) {
			send((i + 1) + "\t");
			for(int j = 0; j < 3; j++) {
				send(game[i][j] + "\t");
			}
			send("\n");
		}
	}
	
	public int[] makeMove(int player) {
		int[] xy = new int[2];
		send((player + 1) + ". player's turn.\n", 1-player);
		send("Your turn.\n", player);
		send("xy\n", player);
		
		try {
			xy[0] = Integer.parseInt(in[player].readLine()) - 1;
			if(xy[0] < 0 || xy[0] > 2)
				xy[0] = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			xy[1] = Integer.parseInt(in[player].readLine()) - 1;
			if(xy[1] < 0 || xy[1] > 2)
				xy[1] = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		send("\nYou've entered this coordinates: (" + (xy[0] + 1) + ", " + (xy[1] + 1) + ")\n", player);
		return xy;
	}
	
	public void makeMove() {
		int[] xy;
		if(turn == 'O') {
			xy = makeMove(0);
			turn = 'X';
			if(game[xy[0]][xy[1]] == '.')
				game[xy[0]][xy[1]] = 'O';
		}else {
			xy = makeMove(1);
			turn = 'O';
			if(game[xy[0]][xy[1]] == '.')
				game[xy[0]][xy[1]] = 'X';
		}
	}

	
}
