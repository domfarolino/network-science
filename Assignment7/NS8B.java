// NS8B.java CS5172/6072 2018 Cheng
// usage: java NS8B neighborsFile
// degree-preserving randomization

import java.io.*;
import java.util.*;

class NS8B{

	int N = 0;  // number of vertices/nodes
	int L = 0;  // number of links/edges
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	int[][] edges = null;
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
		L += hset.size();
	}
	in.close();
	N = neighbors.size();
	L /= 2;
  }

  void edgeList(){
	edges = new int[L][2];
	int n = 0;
	for (int i = 0; i < N; i++) for (int j : neighbors.get(i))
		if (j > i){ edges[n][0] = i; edges[n][1] = j; n++; }
  }

  void computeDegrees(){
    degrees = new int[N];
    for (int i = 0; i < N; i++) degrees[i] = neighbors.get(i).size();
  }

  void randomizeEdges(){
	boolean[] swapped = new boolean[L];
	for (int i = 0; i < L; i++) swapped[i] = false;
	Random random = new Random();
	int unswapped = L;
	while (unswapped > 1){
		int a = random.nextInt(unswapped);
		int b = random.nextInt(unswapped);
		if (a == b) continue;
		if (a > b){ int t = a; a = b; b = t; }
		int j = 0; int n = 0;
		for (; n <= a; n++) while (swapped[j++]);
		int c = j - 1; swapped[c] = true;
		for (; n <= b; n++) while (swapped[j++]);
		int d = j - 1; swapped[d] = true;
		unswapped -= 2;	
		if (edges[c][1] == edges[d][0]) continue;
		int temp = edges[c][1]; edges[c][1] = edges[d][1]; edges[d][1] = temp;
	}
  }

  void randomizeNeighbors(){
	for (int i = 0; i < N; i++) neighbors.get(i).clear();
	for (int i = 0; i < L; i++){
		neighbors.get(edges[i][0]).add(edges[i][1]);
		neighbors.get(edges[i][1]).add(edges[i][0]);
	}
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
     System.err.println("Usage: java NS8B neighborsFile");
     System.exit(1);
   }
   NS8B ns8 = new NS8B();
   ns8.readNet(args[0]);
   ns8.edgeList();
   ns8.randomizeEdges();
   ns8.randomizeNeighbors();
   ns8.computeDegrees();
   //ns8.forwardNeighbors();
   ns8.backwardNeighbors();
 }
}
