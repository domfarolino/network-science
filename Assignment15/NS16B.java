// NS16B.java CS5172/6072 cheng 2018
// reads a set of bicliques (output from NS16A) and 
// builds a biclique graph and then finds its components
// outputs nodes from the original network in each of these components as biclique percolation
// Usage: java NS16B bicliques minOverlap

import java.io.*;
import java.util.*;

public class NS16B{

   int minOverlap = 3;
   ArrayList<HashSet<Integer>> bicliquesA = new ArrayList<HashSet<Integer>>();
   ArrayList<HashSet<Integer>> bicliquesB = new ArrayList<HashSet<Integer>>();
   int N = 0;  // number of bicliques
   ArrayList<HashSet<Integer>> neighbors = null;  // the biclique graph

   void readBicliques(String filename){
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found");
		System.exit(1);
	}
	in.nextLine();  // skip the bipartite network size
	while (in.hasNextLine()){
		String line = in.nextLine();
		int colon = line.indexOf(':');
		String rows = line.substring(0, colon - 1);
		String columns = line.substring(colon + 2);
		String[] terms = rows.split(" ");
		HashSet<Integer> hset = new HashSet<Integer>();
		for (int j = 0; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		bicliquesA.add(hset);
		terms = columns.split(" ");
		hset = new HashSet<Integer>();
		for (int j = 0; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		bicliquesB.add(hset);
	}
	in.close();
	N = bicliquesA.size();
  }

  void makeCliqueGraph(int threshold){
	minOverlap = threshold;
	neighbors = new ArrayList<HashSet<Integer>>(N);
	for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
	for (int i = 1; i < N; i++) for (int j = 0; j < i; j++)
		if (intersection(bicliquesA.get(i), bicliquesA.get(j)).size() >= minOverlap
			&& intersection(bicliquesB.get(i), bicliquesB.get(j)).size() >= minOverlap){
			neighbors.get(i).add(j); neighbors.get(j).add(i);
	}
  }

  HashSet<Integer> intersection(HashSet<Integer> A, HashSet<Integer> B){
	HashSet<Integer> C = new HashSet<Integer>(A);
	C.retainAll(B);
	return C;
  }

  HashSet<Integer> bfsReachable(int startVertex){  // with no distances from start
	HashSet<Integer> reachables = new HashSet<Integer>();
	Queue<Integer> queue = new LinkedList<Integer>();
	queue.add(startVertex); reachables.add(startVertex);
	while (!queue.isEmpty()){
		int h = queue.remove();
		neighbors.get(h).forEach(j -> { if (!reachables.contains(j)){
			reachables.add(j); queue.add(j); }});
	}
	return reachables;
  }


  void allComponents(){
	int[] components = new int[N];
     	int n = 1; int m = 0;
     	for (int i = 0; i < N; i++) components[i] = -1;
     	while (m < N){
        	int i = 0; for (; i < N; i++) if (components[i] < 0) break;
		HashSet<Integer> newComponent = bfsReachable(i);
		int size = newComponent.size();
		for (int j : newComponent) components[j] = n;
		printCommunity(newComponent);
		n++; m += size;
     	}
   }

  void printCommunity(HashSet<Integer> bicliquePercolation){
	TreeSet<Integer> nodesInCommunityA = new TreeSet<Integer>();
	TreeSet<Integer> nodesInCommunityB = new TreeSet<Integer>();
	bicliquePercolation.forEach(x -> {
		nodesInCommunityA.addAll(bicliquesA.get(x));
		nodesInCommunityB.addAll(bicliquesB.get(x));
	});
	System.out.println("Number of maximal bicliques in community: " + bicliquePercolation.size());
	nodesInCommunityA.forEach(x -> System.out.print(x + " "));
	System.out.print(":");
	nodesInCommunityB.forEach(x -> System.out.print(" " + x));
	System.out.println();
  }


  public static void main(String[] args){
	if (args.length < 2){
		System.err.println("Usage: java NS16B bicliques minOverlap");
		return;
	}
	NS16B ns16 = new NS16B();
	ns16.readBicliques(args[0]);
	ns16.makeCliqueGraph(Integer.parseInt(args[1]));
	ns16.allComponents();
  }
}

