// NS2A.java CS5172/6072 Cheng 2017
// This program reads a neighbors file and save the graph in an array of sets
// Usage: java NS2A stringentNet.txt

import java.io.*;
import java.util.*;

public class NS2A{

	int N = 0;  // number of vertices/nodes
	int dmax = 0; // maximum degree
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
		int degree = hset.size();
		if (degree > dmax) dmax = degree;
		neighbors.add(hset);
	}
	in.close();
	N = labels.size();
  }

  void degreeDistribution(){
	int[] counts = new int[dmax + 1];
	for (int i = 0; i <= dmax; i++) counts[i] = 0;
	for (int i = 0; i < N; i++) counts[neighbors.get(i).size()]++;
        System.out.println("Max count: " + dmax);
	for (int i = 0; i <= dmax; i++) if (counts[i] > 0) System.out.println(i + "\t" + counts[i]);
  }


  public static void main(String[] args){
	NS2A ns2 = new NS2A();
	ns2.readNet(args[0]);
	ns2.degreeDistribution();
  }
}
