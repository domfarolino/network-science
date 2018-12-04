// NS15A.java CS5172/6072 Cheng 2018
// This program reads a neighbors file and 
// randomly selects a node as "patient zero" and run SIR
// It then wait till "day k" to identify the frontier of the epidemic
// and use that information to guess the "patient zero" as the distance center
// Usage: java NS15A neighbors-file

import java.io.*;
import java.util.*;

public class NS15A{

	static final int dayK = 4;
	static final double beta = 0.8;
	static final double mu = 0.9;
	static final int tops = 10;
	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	char[] compartments = null;
	Random random = new Random();
	int patientZero = -1;
	int[] dist = null;
	int[] sumOfDist = null;  // distance centrality

  void readNet(String filename){
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found");
		System.exit(1);
	}
	while (in.hasNextLine()){
		String[] terms = in.nextLine().split(" ");
		labels.add(terms[0]);
		HashSet<Integer> hset = new HashSet<Integer>();
		for (int j = 1; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		neighbors.add(hset);
		L += terms.length - 1;
	}
	in.close();
	N = labels.size();  L /= 2;
  }

 void SIR(){
	compartments = new char[N];
	dist = new int[N];
	HashSet<Integer> newlyInfected = new HashSet<Integer>();
	HashSet<Integer> newlyRecovered = new HashSet<Integer>();
	for (int i = 0; i < N; i++) compartments[i] = 'S';
	patientZero = random.nextInt(N);
	System.out.println("patient zero: " + labels.get(patientZero));
	compartments[patientZero] = 'I';
	bfs(patientZero);
	int numberOfI = 1;
	for (int iter = 0; iter < dayK; iter++){
		newlyInfected.clear();
		newlyRecovered.clear();
		for (int i = 0; i < N; i++) if (compartments[i] == 'I')
			for (int j: neighbors.get(i))
				if (compartments[j] == 'S')
					if (random.nextDouble() < beta) newlyInfected.add(j);
		if (iter > 2)  // it takes a while to start recover
			for (int i = 0; i < N; i++) if (compartments[i] == 'I')
				if (random.nextDouble() < mu) newlyRecovered.add(i);
		numberOfI += newlyInfected.size() - newlyRecovered.size();
		for (int j: newlyInfected) compartments[j] = 'I';
		for (int j: newlyRecovered) compartments[j] = 'R';
		System.out.println(iter + "\t" + (numberOfI * 100.0 / N));
	}
	for (int i = 0; i < N; i++) if (compartments[i] == 'I')
		System.out.println("ring " + labels.get(i) + " " + dist[i]);
 }

  void bfs(int source){  // breadth-first search for distances from source
	Queue<Integer> queue = new LinkedList<Integer>();
        for (int j = 0; j < N; j++) dist[j] = -1;  // distance may be -1 if not reachable
        dist[source] = 0;  queue.add(source);
        while (queue.peek() != null){
           int h = queue.remove();  
           for (int j: neighbors.get(h)) if (dist[j] < 0){
              dist[j] = dist[h] + 1; queue.add(j); 
           }
        }
  }

  void computeSumOfDist(){  // computes distance centrality for nodes
	sumOfDist = new int[N];  
	for (int i = 0; i < N; i++) sumOfDist[i] = 0;
	for (int i = 0; i < N; i++) if (compartments[i] == 'I'){
		bfs(i); 
		for (int j = 0; j < N; j++) 
			if (sumOfDist[j] >= 0 && dist[j] >= 0) sumOfDist[j] += dist[j];
			else sumOfDist[j] = -1;
	}
  }

  void minSumOfDist(){  // distance centers have smallest distance centrality
	ArrayList<Integer> toBeSorted = new ArrayList<Integer>(N);
	for (int i = 0; i < N; i++) if (sumOfDist[i] > 0) toBeSorted.add(i);
	int allSorted = toBeSorted.size();
	if (allSorted == 0) return;
	toBeSorted.sort((x, y) -> sumOfDist[x] - sumOfDist[y]);
	if (allSorted > tops) allSorted = tops;
        int patientZeroRank = 0;
        int patientZeroDistance = 0;
        int avg = 0;
	for (int i = 0; i < toBeSorted.size(); i++){
	  int patientID = toBeSorted.get(i);
          int patientSum = sumOfDist[patientID];
          avg += sumOfDist[patientID];
	  if (i < allSorted) System.out.println(labels.get(patientID) + " " + patientSum);
          if (patientID == patientZero) {
            patientZeroRank = i;
            patientZeroDistance = patientSum;
          }
            
	}

        avg /= toBeSorted.size();
        System.out.println("Patient Zero Rank\t" + patientZeroRank);
        System.out.println("Patient Zero Inf Dist Sum\t" + patientZeroDistance);
        System.out.println("Average distance rank\t" + avg);
  }

  public static void main(String[] args){
	if (args.length < 1){
		System.err.println("Usage: java NS15A neighbors-file");
		return;
	}
	NS15A ns15 = new NS15A();
	ns15.readNet(args[0]);
	ns15.SIR();
	ns15.computeSumOfDist();
	ns15.minSumOfDist();
  }
}
