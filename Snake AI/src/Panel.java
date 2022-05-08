import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 30;
	static final int GAME_UNITS = SCREEN_HEIGHT * SCREEN_WIDTH / UNIT_SIZE;
	static final int DELAY = 0;
	final int[] x = new int[GAME_UNITS];
	final int[] y = new int[GAME_UNITS];
	
	int body_len = 4;
	int score = 4;
	int steps = 0;
	int AppleX;
	int AppleY;
	double[] situation;
	char direction = 'R';
	boolean running = false;
	
	Network NN;
	Timer timer;
	Random rand;
	
	Panel(){
		rand = new Random();
		NN = new Network();
		x[0] = 10 * UNIT_SIZE;
		y[0] = 10 * UNIT_SIZE;
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		Start();
	}
	
	Panel(Network parent){
		rand = new Random();
		NN = new Network(parent);
		x[0] = 10 * UNIT_SIZE;
		y[0] = 10 * UNIT_SIZE;
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		Start();
	}
	
	public void Start() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			g.setColor(Color.BLUE);
			for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.RED);
			g.fillOval(AppleX + 2, AppleY + 1, UNIT_SIZE - 2, UNIT_SIZE - 2);
			
			g.setColor(new Color(75, 150, 75));
			g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 1; i < body_len; i++) {
				g.setColor(Color.GREEN);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
	}
	
	public void move(){
		//score += 1;
		for(int i = body_len; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
			case 'U': 
				y[0] -= UNIT_SIZE;
				break;
			case 'D':
				y[0] += UNIT_SIZE;
				break;
			case 'R':
				x[0] += UNIT_SIZE;
				break;
			case 'L':
				x[0] -= UNIT_SIZE;
				break;			
		}
		
	}
	
	public void checkApple() {
		if(x[0] == AppleX && y[0] == AppleY) {
			score += 1;
			body_len++;
			newApple();
		}
	}
	
	public void newApple() {
		AppleX = rand.nextInt( (int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		AppleY = rand.nextInt( (int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
		for(int i = 0; i < body_len; i++) {
			if(AppleX == x[i] && AppleY == y[i]) {
				newApple();
			}
		}
	}
	
	public void checkCollision() {
		for(int i = 1; i < body_len; i++) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running  = false;
			}
		}
		
		if(x[0] < 0 || x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		
		if(y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		
		if(steps > 400) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(running) {
			steps++;
			decide();
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}
	
	public void decide() {
		situation = new double[4];
		
		situation[0] = Math.max(AppleX - x[0], 0);
		situation[1] = Math.max(x[0] - AppleX, 0);
		situation[2] = Math.max(AppleY - y[0], 0);
		situation[3] = Math.max(y[0] - AppleY, 0);
		
		double[] decision = NN.feedForward(situation);
		int max = 0;
		if(decision[1] > decision[max]) max = 1;
		if(decision[2] > decision[max]) max = 2;
		if(decision[3] > decision[max]) max = 3;
		switch (max) {
			case 0:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case 1:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			case 2:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case 3:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
		}
	}
	
}
