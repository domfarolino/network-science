// NS13A.java CS5172/6072 Cheng 2018
// enumerates maximal cliques of size at least 3
// prints cliques as sets of node indexes, one line a time
// Usage: java NS13A neighbors-file
import java.io.*;
import java.util.*;

public class NS13A{

	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();

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

  void SE(int k, HashSet<Integer> S, HashSet<Integer> C){  // recursive set enumeration
// S is the current subset, C is nodes adjacent to all nodes in S
// initially S is the empty set and C is all nodes
// k prevents duplicated cliques
  	if (C.isEmpty()){ if (S.size() >= 2) printout(S); return; }
  	for (int j = k; j <N; j++) if (C.contains(j)){
		S.add(j); 
		SE(j + 1, S, intersection(C, neighbors.get(j)));
		S.remove(j);
	}
  }

  void maximalCliques(){
	HashSet<Integer> allNodes = new HashSet<Integer>(N);
	for (int i = 0; i < N; i++) allNodes.add(i);
	SE(0, new HashSet<Integer>(), allNodes);
  }

  void printout(HashSet<Integer> S){
	for (int j: S) System.out.print(j + " ");
	System.out.println();
  }

  HashSet<Integer> intersection(HashSet<Integer> A, HashSet<Integer> B){
	HashSet<Integer> C = new HashSet<Integer>(A);
	C.retainAll(B);
	return C;
  }

 
 public static void main(String[] args){
   if (args.length < 1){ System.err.println("Usage: java NS13A neighbors-file");
	return; } 
   NS13A ns13 = new NS13A();
   ns13.readNet(args[0]);  
   ns13.maximalCliques();
 }   
} 

		

