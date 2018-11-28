// NS14A.java CS5172/6072 Cheng 2018
// This program reads a neighbors file and simulates an epidemic
// Usage: java NS14A neighbors-file beta mu

import java.io.*;
import java.util.*;

public class NS14A{

	static final int maxIter = 20;
	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	char[] compartments = null;
	Random random = new Random();

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

 void SI(double beta){
	compartments = new char[N];
	for (int i = 0; i < N; i++) compartments[i] = 'S';
	compartments[ random.nextInt(N)] = 'I';
	HashSet<Integer> newlyInfected = new HashSet<Integer>();
	int numberOfI = 1;
	for (int iter = 0; iter < maxIter; iter++){
		newlyInfected.clear();
		for (int i = 0; i < N; i++) if (compartments[i] == 'I')
			for (int j: neighbors.get(i))
				if (compartments[j] == 'S')
					if (random.nextDouble() < beta) newlyInfected.add(j);
		numberOfI += newlyInfected.size();
		for (int j: newlyInfected) compartments[j] = 'I';
		System.out.println(iter + "\t" + (numberOfI * 100.0 / N));
	}
 }

 void SIS(double beta, double mu){
	HashSet<Integer> newlyInfected = new HashSet<Integer>();
	HashSet<Integer> newlyRecovered = new HashSet<Integer>();
	compartments = new char[N];
	for (int i = 0; i < N; i++) compartments[i] = 'S';
	compartments[ random.nextInt(N)] = 'I';
	int numberOfI = 1;
	for (int iter = 0; iter < maxIter; iter++){
		newlyInfected.clear();
		newlyRecovered.clear();
		for (int i = 0; i < N; i++) if (compartments[i] == 'I')
			for (int j: neighbors.get(i))
				if (compartments[j] == 'S')
					if (random.nextDouble() < beta) newlyInfected.add(j);
		if (iter > 5)  // it takes a while to start recover
			for (int i = 0; i < N; i++) if (compartments[i] == 'I')
				if (random.nextDouble() < mu) newlyRecovered.add(i);
		numberOfI += newlyInfected.size() - newlyRecovered.size();
		for (int j: newlyInfected) compartments[j] = 'I';
		for (int j: newlyRecovered) compartments[j] = 'S';
		System.out.println(iter + "\t" + (numberOfI * 100.0 / N));
	}
 }

 void SIR(double beta, double mu){
	compartments = new char[N];
	for (int i = 0; i < N; i++) compartments[i] = 'S';
	compartments[ random.nextInt(N)] = 'I';
	HashSet<Integer> newlyInfected = new HashSet<Integer>();
	HashSet<Integer> newlyRecovered = new HashSet<Integer>();
	int numberOfI = 1;
	for (int iter = 0; iter < maxIter; iter++){
		newlyInfected.clear();
		newlyRecovered.clear();
		for (int i = 0; i < N; i++) if (compartments[i] == 'I')
			for (int j: neighbors.get(i))
				if (compartments[j] == 'S')
					if (random.nextDouble() < beta) newlyInfected.add(j);
		if (iter > 5)  // it takes a while to start recover
			for (int i = 0; i < N; i++) if (compartments[i] == 'I')
				if (random.nextDouble() < mu) newlyRecovered.add(i);
		numberOfI += newlyInfected.size() - newlyRecovered.size();
		for (int j: newlyInfected) compartments[j] = 'I';
		for (int j: newlyRecovered) compartments[j] = 'R';
		System.out.println(iter + "\t" + (numberOfI * 100.0 / N));
	}
 }


  public static void main(String[] args){
	if (args.length < 3){
		System.err.println("Usage: java NS14A beta mu");
		return;
	}
	NS14A ns14 = new NS14A();
	ns14.readNet(args[0]);
	ns14.SI(Double.parseDouble(args[1]));
	//ns14.SIS(Double.parseDouble(args[1]), Double.parseDouble(args[2]));
	//ns14.SIR(Double.parseDouble(args[1]), Double.parseDouble(args[2]));
  }
}
