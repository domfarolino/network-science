// NS8C.java CS5172/6072 2018 Cheng
// usage: java NS8C neighborsFile
// full randomization, only preserving N and L

import java.io.*;
import java.util.*;

class NS8C{

	int N = 0;  // number of vertices/nodes
	int L = 0;  // number of links/edges
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	int[] degrees = null;
	int numberOfForwardNeighbors = 0;
	int numberOfBackwardNeighbors = 0;
	int myDegree = 0;

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
		HashSet<Integer> hset = new HashSet<Integer>();
		neighbors.add(hset);
		L += terms.length - 1;
	}
	in.close();
	N = neighbors.size();
	L /= 2;
  }

  void fullRandomization(){
	Random random = new Random();
	for (int i = 0; i < L; i++){
		int a = random.nextInt(N);
		int b = random.nextInt(N);
		if (a == b) b = random.nextInt(N);
		if (a == b) continue;
		neighbors.get(a).add(b); neighbors.get(b).add(a);
	}
  }

  void computeDegrees(){
        degrees = new int[N];
        for (int i = 0; i < N; i++) degrees[i] = neighbors.get(i).size();
  }

  void forwardNeighbors(){
        for (int i = 0; i < N; i++) {
                numberOfForwardNeighbors = 0;
                myDegree = degrees[i];
                neighbors.get(i).forEach(j -> {
                        if (degrees[j] > myDegree) numberOfForwardNeighbors++;
                });
                System.out.println(myDegree + "\t" + numberOfForwardNeighbors);
        }
  }

  void backwardNeighbors() {
        for (int i = 0; i < N; i++) {
                numberOfBackwardNeighbors = 0;
                myDegree = degrees[i];
                neighbors.get(i).forEach(j -> {
                        if (degrees[j] < myDegree) numberOfBackwardNeighbors++;
                });
                System.out.println(myDegree + "\t" + numberOfBackwardNeighbors++);
        }
  }

 public static void main(String[] args){
   if (args.length < 1){
     System.err.println("Usage: java NS8C neighborsFile");
     System.exit(1);
   }
   NS8C ns8 = new NS8C();
   ns8.readNet(args[0]);
   ns8.fullRandomization();
   ns8.computeDegrees();
   //ns8.forwardNeighbors();
   ns8.backwardNeighbors();
 }
}
