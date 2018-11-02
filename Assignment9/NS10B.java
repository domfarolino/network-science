// NS10B.java CS5172/6072 2018 Cheng
// usage: java NS10B neighborsFile
// Xulvi-Brunet abd Sokolov Algorithm to maximize degree correlation

import java.io.*;
import java.util.*;

class NS10B{

	int N = 0;  // number of vertices/nodes
	int L = 0;  // number of links/edges
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	int[][] edges = null;
	int[] degrees = null;

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
	degrees = new int[N];
	int n = 0;
	for (int i = 0; i < N; i++){
		degrees[i] = neighbors.get(i).size();
		for (int j : neighbors.get(i)) if (j > i){ 
			edges[n][0] = i; edges[n][1] = j; n++; } 
	}
  }

  void rewire(){
	boolean[] swapped = new boolean[L];
	for (int i = 0; i < L; i++) swapped[i] = false;
	Random random = new Random();
	TreeMap<Double, Integer> sorted = new TreeMap<Double, Integer>();
	int unswapped = L;
	while (unswapped > 1){
		int a = random.nextInt(unswapped);
		int b = random.nextInt(unswapped);
		if (a == b) continue;
		if (a > b) {
                  // Swap a & b
                  int tmp = a;
                  a = b;
                  b = tmp;
                }

                // Assert: a <= b
		int j = 0;
                int n = 0;
		for (; n <= a; n++)
                  while (swapped[j++]);
		int c = j - 1; 
		for (; n <= b; n++)
                  while (swapped[j++]);
		int d = j - 1; 

		swapped[c] = swapped[d] = true;
                unswapped -= 2;
		if (edges[c][0] == edges[d][0] ||
                    edges[c][0] == edges[d][1] ||
                    edges[c][1] == edges[d][0] ||
                    edges[c][1] == edges[d][1]) {
                  continue;
                }
		// two edges with less than four nodes, no rewiring
		sorted.clear();  // c0, c1, d0, d1 are labeled with 0, 1, 2, 3
		sorted.put((double)(degrees[edges[c][0]]), 0);
		sorted.put(degrees[edges[c][1]] + 0.25, 1);
		sorted.put(degrees[edges[d][0]] + 0.5, 2);
		sorted.put(degrees[edges[d][1]] + 0.75, 3);

                sorted.pollFirstEntry();
		int u = sorted.pollFirstEntry().getValue() + sorted.pollFirstEntry().getValue();
                sorted.pollFirstEntry();

		// u is the sum of the labels of the two nodes with smallest degrees
		// possible u values: 1, 2, 3, 4, 5 on six combinations of 2 out of 4
		// the following tunes towards assortative
		if (u == 1 || u == 5) continue;  // already assortative
		if (u == 3) {
			int tmp = edges[c][1];
                        edges[c][1] = edges[d][1];
                        edges[d][1] = tmp;
		} else {
			int tmp = edges[c][1];
                        edges[c][1] = edges[d][0];
                        edges[d][0] = tmp;
		}
		// for rewiring for disassortativity, 
		// u should be the sum of the two nodes with less extreme degrees of the four
	}

  } 

  void backToNeighbors(){
	for (int i = 0; i < N; i++) neighbors.get(i).clear();
	for (int i = 0; i < L; i++){
		neighbors.get(edges[i][0]).add(edges[i][1]);
		neighbors.get(edges[i][1]).add(edges[i][0]);
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
   if (args.length < 1){
     System.err.println("Usage: java NS10B neighborsFile");
     System.exit(1);
   }
   NS10B ns10 = new NS10B();
   ns10.readNet(args[0]);
   ns10.edgeList();
   ns10.rewire();
   ns10.backToNeighbors();
   ns10.printNeighbors();
 }
}
