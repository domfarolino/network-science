// NS10A.java CS5172/6072 Cheng 2018
// This program reads a neighbors file and computes the degree correlation function knn
// Usage: java NS10A neighbors-file

import java.io.*;
import java.util.*;

public class NS10A{

	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	int kmax = 0;
	double[] knn = null;  // average degree of neighbors

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
		for (int j = 1; j < terms.length; j++)
                  hset.add(Integer.parseInt(terms[j]));
		neighbors.add(hset);
		L += terms.length - 1;
	}
	in.close();
	N = labels.size();  L /= 2;
  }

  void degreeCorrelation(){ // (7.7)
	int[] degrees = new int[N];
	for (int i = 0; i < N; i++){
		degrees[i] = neighbors.get(i).size();
		if (degrees[i] > kmax)
                  kmax = degrees[i];
	}

	int[][] e = new int[kmax + 1][kmax + 1];
	int[] q = new int[kmax + 1];
	knn = new double[kmax + 1];

	for (int i = 0; i <= kmax; i++) {
		for (int j = 0; j <= kmax; j++)
                  e[i][j] = 0;
        }

	for (int i = 0; i < N; i++){
		int d1 = degrees[i];
		for (int j: neighbors.get(i))
                  e[d1][degrees[j]]++;
	}

	for (int i = 0; i <= kmax; i++){
		q[i] = 0;
		for (int j = 0; j <= kmax; j++)
                  q[i] += e[i][j];
		knn[i] = 0;
		if (q[i] > 0){
			for (int j = 0; j <= kmax; j++)
                          knn[i] += j * e[i][j];
			knn[i] /= q[i];
		}
	}
  }

  void printKnn(){
	for (int i = 0; i <= kmax; i++)
          if (knn[i] > 0)
            System.out.println(i + "\t" + knn[i]);
  }
	

  void printNeighbors(){
	for (int i = 0; i < N; i++){
		System.out.print(labels.get(i));
		for (int j: neighbors.get(i))
                  System.out.print(" " + j);
		System.out.println();
	}
  }


  public static void main(String[] args){
	NS10A ns10 = new NS10A();
	ns10.readNet(args[0]);
	ns10.degreeCorrelation();
	ns10.printKnn();
  }
}
