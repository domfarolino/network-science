// NS10C.java CS5172/6072 2018 Cheng
// usage: java NS10C N <k>
// random graph generator with number of nodes N and average degree <k>

import java.io.*;
import java.util.*;

class NS10C{

    int N = 0;  int L = 0;
    ArrayList<HashSet<Integer>> neighbors = null;
    Random random = new Random();

    public NS10C(int n, double k){
        N = n;  L = (int)(k * N / 2);
	neighbors = new ArrayList<HashSet<Integer>>(N);
	for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
    }

    void addLinks(){
	int l = 0;
	while (l < L){
		int a = random.nextInt(N);
		int b = random.nextInt(N);
		if (a != b && !neighbors.get(a).contains(b)){
			neighbors.get(a).add(b);
			neighbors.get(b).add(a);
			l++;
		}
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
     System.err.println("Usage: java NS10C N <k>");
     System.exit(1);
   }
   NS10C ns10 = new NS10C(Integer.parseInt(args[0]), Double.parseDouble(args[1]));
   ns10.addLinks();
   ns10.printNeighbors();
 }
}
