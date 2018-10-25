// NS9B.java CS5172/6072 2018 Cheng
// usage: java NS9B N m
// Barabasi-Albert Model A with number of nodes N and number of links m per new node
// m0 is set to be m + 2
// no preferential attachment
 
import java.io.*;
import java.util.*;

class NS9B{

    static final int dt = 50;
    int m0 = 0;
    int N = 0;
    int m = 0;
    ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
    int[] degreeSnapshot = null;

    public NS9B(int n, int mm){
        N = n; m0 = m + 2; m = mm;
    }

    void evolve(){
	for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
	neighbors.get(0).add(1); neighbors.get(1).add(0);
        Random random = new Random();
        for (int i = 2; i < m0; i++){  // initial links
        	int j = random.nextInt(i);
		neighbors.get(i).add(j); neighbors.get(j).add(i);
        }
	int j = 0;
        degreeSnapshot = new int[N - dt];
        for (int i = m0; i < N; i++){  // each node after m0 has m links
        	for (int l = 0; l < m; l++){
			j = random.nextInt(i); // no PA
			neighbors.get(i).add(j); neighbors.get(j).add(i);
        	}
                if (i + dt == N)  // take a snapshot of the degrees
                  for (int k = 0; k < i; k++) degreeSnapshot[k] = neighbors.get(k).size();
        }
    }

  void cumulative(){
        int[] changes = new int[N - dt];
        int kmax = 0;
        for (int i = 0; i < N - dt; i++){
                changes[i] = neighbors.get(i).size() - degreeSnapshot[i];
                if (degreeSnapshot[i] > kmax) kmax = degreeSnapshot[i];
        }
        int[] pk = new int[kmax + 1];
        double[] Pi = new double[kmax + 1];
        for (int k = 0; k <= kmax; k++){ pk[k] = 0; Pi[k] = 0; }
        for (int i = 0; i < N - dt; i++){  // take average change / (dt m) for Pi
                pk[degreeSnapshot[i]]++;
                Pi[degreeSnapshot[i]] += (double)(changes[i]) / m / dt;
        }
        double sum = 0;
        for (int k = 0; k <= kmax; k++) if (pk[k] > 0){  // 
                Pi[k] /= pk[k];
                sum += Pi[k];
        }
        double cumul = 0;
        for (int k = 0; k <= kmax; k++){ // pi is cumulative Pi
                Pi[k] /= sum;
                cumul += Pi[k];
                if (k >= m && k <= kmax/2)
                  System.out.println(k + "\t" + cumul);
        }
  }

  void printNeighbors(){
	for (int i = 0; i < N; i++){
		System.out.print(i);
		neighbors.get(i).forEach(j -> System.out.print(" " + j));
		System.out.println();
	}
  }


  public static void main(String[] args){
   if (args.length < 2){
     System.err.println("Usage: java NS9B N m");
     System.exit(1);
   }
   NS9B ns9 = new NS9B(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
   ns9.evolve();
   ns9.cumulative();
 }
}
