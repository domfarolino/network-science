// NS8A.java CS5172/6072 2018 Cheng
// usage: java NS8A neighborsFile
// checking for Barabasi-Albert
// prints degree and the number of neighbors with higher degrees

import java.io.*;
import java.util.*;

class NS8A{

	int N = 0;  // number of vertices/nodes
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
		for (int j = 1; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		neighbors.add(hset);
	}
	in.close();
	N = neighbors.size();
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
     System.err.println("Usage: java NS8A neighborsFile");
     System.exit(1);
   }
   NS8A ns8 = new NS8A();
   ns8.readNet(args[0]);
   ns8.computeDegrees();
   //ns8.forwardNeighbors();
   ns8.backwardNeighbors();
 }
}
