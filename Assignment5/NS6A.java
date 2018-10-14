// NS6A.java CS5172/6072 Cheng 2018
// for Homework 4.3 (or 4.10.3) in Barabasi
// power-law degree distribution with degree exponent gamma (2.2 and 3)
// Usage: java NS6A N gamma
// implements the configuration model (4.8.1) and reports self-loops and multi-links

import java.io.*;
import java.util.*;

public class NS6A{

    int N = 0;
    double gamma = 0;
    ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
    int kmax = 0;
    double[] D = null;
    int[] degrees = null;
    int numberOfStubs = 0;
    Random random = new Random();
    int selfLoops = 0;
    int multiLinks = 0;

  public NS6A(int n, double g){ N = n; gamma = g; }

  void generateD(){
	kmax = (int)Math.pow((double)N, 1.0 / (gamma - 1)) + 3; // (4.18) + 3
	double[] p = new double[kmax];
	D = new double[kmax];
	double zeta = 0;
	for (int k = 1; k < kmax; k++){
		p[k] = Math.pow((double)k, -gamma);
		zeta += p[k];
	}
	for (int k = 1; k < kmax; k++) p[k] /= zeta;  // (4.8)
	D[kmax - 1] = p[kmax - 1];
	for (int k = kmax - 2; k > 0; k--) D[k] = D[k + 1] + p[k]; // (4.25)
  } 

  void generateDegreeSequence(){
	degrees = new int[N];
	for (int i = 0; i < N; i++){
		double r = random.nextDouble();
		int k = kmax - 1; for (; D[k] < r; k--);
		degrees[i] = k;
		numberOfStubs += k;
	}
  }		

  void generateLinks(){
	for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
	int[] stubs = new int[numberOfStubs];
	int n = 0;
	for (int i = 0; i < N; i++) for (int j = 0; j < degrees[i]; j++) stubs[n++] = i;
	int unused = numberOfStubs;
	while (unused > 1){
		int a = random.nextInt(unused);
		int b = random.nextInt(unused);
		if (a == b) continue;
		if (a > b){ int t = a; a = b; b = t; }
		int j = 0; n = 0;
		for (; n <= a; n++) while (stubs[j++] < 0); j--;
		int c = stubs[j]; stubs[j] = -1;
		for (; n <= b; n++) while (stubs[j++] < 0); j--;
		int d = stubs[j]; stubs[j] = -1;
		unused -= 2;	
		if (c == d) selfLoops++;
		else if (neighbors.get(c).contains(d)) multiLinks++;
		else{ neighbors.get(c).add(d); neighbors.get(d).add(c); }
	}
  }

 public static void main(String[] args){
	if (args.length < 2){
		System.err.println("Usage: java NS6A N gamma");
		return;
	}
	NS6A ns6 = new NS6A(Integer.parseInt(args[0]), Double.parseDouble(args[1]));
	ns6.generateD();
	ns6.generateDegreeSequence();
	ns6.generateLinks();
        System.out.println("Number of self-loops: " + ns6.selfLoops);
        System.out.println("Number of multi-links: " + ns6.multiLinks);
 }
}
