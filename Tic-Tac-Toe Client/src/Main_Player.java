import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Main_Player {

	public static void main(String[] args) {
		
		try {
			Socket s = new Socket("127.0.0.1", 5111);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			String str = "", coor = "";
			
			while(!(str = in.readLine()).equals("q")) {
				if(str.equals("xy")) {
					System.out.println("Please enter x coordinates: ");
					coor = stdin.readLine();
					out.write(coor + "\n");
					out.flush();
					System.out.println("Please enter y coordinates: ");
					coor = stdin.readLine();
					out.write(coor + "\n");
					out.flush();
					continue;
				}
				System.out.println(str);
			}
			
			stdin.close();
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
