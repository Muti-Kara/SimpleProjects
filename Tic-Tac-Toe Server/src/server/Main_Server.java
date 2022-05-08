package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main_Server {

	public static void main(String[] args) {
		
		Socket s1, s2;
		BufferedReader[] in = new BufferedReader[2];
		BufferedWriter[] out = new BufferedWriter[2];
		Game newGame = new Game(in, out);
		
		try {
			ServerSocket ss = new ServerSocket(5111);
			s1 = ss.accept();
			in[0] = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			out[0] = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));
			newGame.send("Please wait for another player to connect...\n", 0);
			
			s2 = ss.accept();
			in[1] = new BufferedReader(new InputStreamReader(s2.getInputStream()));
			out[1] = new BufferedWriter(new OutputStreamWriter(s2.getOutputStream()));
			
			newGame.startGame();
			newGame.printScreen();
			
			while(newGame.isRunning()) {
				newGame.makeMove();
				newGame.printScreen();
			}
			
			newGame.send("q\n");
			
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
