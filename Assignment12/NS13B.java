// NS13B.java CS5172/6072 cheng 2018
// reads a set of cliques (output from NS13A) and builds a clique graph and then finds its components
// outputs nodes from the original network in each of these components as clique percolation
// Usage: java NS13B cliques

import java.io.*;
import java.util.*;

public class NS13B{

   static final int minOverlap = 3;
   ArrayList<HashSet<Integer>> cliques = new ArrayList<HashSet<Integer>>();
   int N = 0;  // number of cliques
   ArrayList<HashSet<Integer>> neighbors = null;  // the clique graph

   void readCliques(String filename){
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
		for (int j = 0; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		cliques.add(hset);
	}
	in.close();
	N = cliques.size();
  }

  void makeCliqueGraph(){
	neighbors = new ArrayList<HashSet<Integer>>(N);
	for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
	for (int i = 1; i < N; i++) for (int j = 0; j < i; j++)
		if (intersection(cliques.get(i), cliques.get(j)).size() >= minOverlap){
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

  void printCommunity(HashSet<Integer> cliquePercolation){
	TreeSet<Integer> nodesInCommunity = new TreeSet<Integer>();
	cliquePercolation.forEach(x -> nodesInCommunity.addAll(cliques.get(x)));
	nodesInCommunity.forEach(x -> System.out.print(x + " "));
	System.out.println();
  }


  public static void main(String[] args){
	if (args.length < 1){
		System.err.println("Usage: java NS13B cliques");
		return;
	}
	NS13B ns13 = new NS13B();
	ns13.readCliques(args[0]);
	ns13.makeCliqueGraph();
	ns13.allComponents();
  }
}

