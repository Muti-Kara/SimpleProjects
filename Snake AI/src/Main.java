
public class Main {

	public static void main(String[] args){
		int bestScore = 0;
		
		Network currentNet = new Network();
		Network previousNet = new Network();
		
		for(int i = 0; i < 100; i++) {
			Frame f = new Frame();
			while(f.p.running) {}

			if(f.p.score >= bestScore) {
				bestScore = f.p.score;
				currentNet.w = f.p.NN.w;
			    currentNet.b = f.p.NN.b;
			}
			
			f.dispose();
		}
		previousNet.w = currentNet.w;
		previousNet.b = currentNet.b;
		System.out.println("Gen 0 has finished with best score of "+ bestScore + "!");
		
		for(int gen = 1; gen < 6; gen++) {
			currentNet = new Network();
			int previousBest = bestScore;
			bestScore = 0;
			for(int i = 0; i < 100; i++) {
				Frame f = new Frame(previousNet);
				while(f.p.running) {}
				
				if(f.p.score > bestScore) {
					bestScore = f.p.score;
					currentNet.w = f.p.NN.w;
				    currentNet.b = f.p.NN.b;
				    Matrix.decreaseRate();
				}
				
				f.dispose();
			}
			if(bestScore >= previousBest) {
				previousNet.w = currentNet.w;
				previousNet.b = currentNet.b;
			}
			System.out.println("Gen " + gen + " has finished with best score of "+ bestScore + "!");
		}
		
	}

}