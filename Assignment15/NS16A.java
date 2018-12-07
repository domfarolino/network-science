// NS16A.java CS5172/6072 2018 Cheng
// enumerate maximal frequent itemsets
// Usage: java NS16A bipartite-neighbors-file
// the bipartite-neighbors-file contains items (with names) of baskets (as integers)


import java.io.*;
import java.util.*;

public class NS16A{

	int minSupport = 7;
	int N = 0;  // number of items
	int M = 0;  // number of baskets
	ArrayList<String> items = new ArrayList<String>(); // item labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();

  void readNet(String filename){  // fill neighbors, M and N
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found");
		System.exit(1);
	}
	while (in.hasNextLine()){
		String[] terms = in.nextLine().split(" ");
		items.add(terms[0]);
		HashSet<Integer> hset = new HashSet<Integer>();
		for (int j = 1; j < terms.length; j++){
			int basket = Integer.parseInt(terms[j]);
			if (basket > M) M = basket;
			hset.add(basket);
		}
		neighbors.add(hset);
	}
	in.close();
	N = items.size(); M++; 
	System.out.println(N + " " + M);
  }

  void SE(int k, HashSet<Integer> S, HashSet<Integer> T, HashSet<Integer> C){  
	// set enumeration on S with the support T being carried
	// C is the candidates and k prevents duplicates
  	if (C.isEmpty()){ 
		if (S.size() >= minSupport){ 
			printout(S); 
			printBaskets(T);
		}
		return; 
	}
  	for (int j = k; j < N; j++) if (C.contains(j)){
		HashSet<Integer> newT = T.size() == M ?
			neighbors.get(j) : intersection(T, neighbors.get(j));
		HashSet<Integer> newC = new HashSet<Integer>(C);
		newC.remove(j);
		newC.removeIf(x -> intersection(newT, neighbors.get(x)).size() < minSupport); 
		S.add(j); 
		SE(j + 1, S, newT, newC);
		S.remove(j);
	}
  }

  void maximalFrequentItemsets(int mins){  // starts SE
	minSupport = mins;
	HashSet<Integer> allBaskets = new HashSet<Integer>(M);
	for (int i = 0; i < M; i++) allBaskets.add(i);
	HashSet<Integer> allItems = new HashSet<Integer>(N);
	for (int i = 0; i < N; i++) allItems.add(i);
	SE(0, new HashSet<Integer>(), allBaskets, allItems);
  }

  void printout(HashSet<Integer> S){  // used by SE
	for (int j: S) System.out.print(j + " ");
	System.out.print(":");
  }

  void printBaskets(HashSet<Integer> T){  // used by SE
	for (int j: T) System.out.print(" " + j);
	System.out.println();
  }


  HashSet<Integer> intersection(HashSet<Integer> A, HashSet<Integer> B){  // used by SE
	HashSet<Integer> C = new HashSet<Integer>(A);
	C.retainAll(B);
	return C;
  }



 public static void main(String[] args){
	if (args.length < 2){
		System.err.println("Usage: java NS16A YgenotypesNeighbors.txt 10");
		return;
	}
	NS16A ns16 = new NS16A();
	ns16.readNet(args[0]);
	ns16.maximalFrequentItemsets(Integer.parseInt(args[1]));
 }
}
