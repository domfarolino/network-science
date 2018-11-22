// NS11A.java CS5172/6072 Cheng 2018
// This program reads a neighbors file
// It finds the size of the largest component P_infty(0)
// It removes a fraction f of nodes in one of three ways
// random, high-degree first, or high collective influence first
// and reports the size of the largest component P_infty(f)
// Usage: java NS11A neighbors-file

import java.io.*;
import java.util.*;

public class NS11A{

	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
	ArrayList<HashSet<Integer>> neighborsOfNeighbors = new ArrayList<HashSet<Integer>>();
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
		labels.add(terms[0]);
		HashSet<Integer> hset = new HashSet<Integer>();
		for (int j = 1; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
		neighbors.add(hset);
		L += terms.length - 1;
	}
	in.close();
	N = labels.size();  L /= 2;
  }

  void computeDegrees(){
	degrees = new int[N];
	for (int i = 0; i < N; i++) degrees[i] = neighbors.get(i).size(); 
  }

  void computeNeighborsOfNeighbors(){
	neighborsOfNeighbors.clear();
	for (int i = 0; i < N; i++){
		HashSet<Integer> nn = new HashSet<Integer>();
		for (int j: neighbors.get(i)) nn.addAll(neighbors.get(j));
		nn.remove(i);
		neighborsOfNeighbors.add(nn);
	}
  }

  int topInfluencer(){
	int topInfluence = 0;  int topNode = -1;
	for (int i = 0; i < N; i++){
		int totalReducedDegree = 0;
		for (int j: neighborsOfNeighbors.get(i)) totalReducedDegree += degrees[j] - 1;
		totalReducedDegree *= (degrees[i] - 1);
		if (totalReducedDegree > topInfluence){
			topInfluence = totalReducedDegree;
			topNode = i;
		}
	}
	return topNode;
  }

  int sizeOfLargestComponent(){  // P_infty is Ng / N
     Queue<Integer> queue = new LinkedList<Integer>();
     int[] components = new int[N];
     int n = 1; int m = 0;  int Ng = 0; 
     for (int i = 0; i < N; i++) components[i] = -1;
     while (m < N){
        int i = 0; for (; i < N; i++) if (components[i] < 0) break;
	components[i] = n;
        queue.add(i); int size = 1;
        while (queue.peek() != null){
           int h = queue.remove();  
           for (int j: neighbors.get(h)) if (components[j] < 0){
              components[j] = n; queue.add(j); size++;
           }
        }
	if (size > Ng) Ng = size; 
	n++; m += size;
     }
     return Ng;
   }

  void randomFailures(){  
	ArrayList<Integer> nodeList = new ArrayList<Integer>(N);
	int interval = N / 100;
	for (int i = 0; i < N; i++) nodeList.add(i);
	Collections.shuffle(nodeList);
	for (int i = 0; i < N; i++){  // f is i / N
		removeNode(nodeList.get(i));
		if (i % interval == 2) System.out.println(i + "\t" + sizeOfLargestComponent());
	}
  }

  void removeNode(int node){
	for (int j: neighbors.get(node))
		if (neighbors.get(j).remove(node)) degrees[j]--;
	neighbors.get(node).clear(); degrees[node] = 0;
  }

  void attackHubsFirst(){
	ArrayList<Integer> nodeList = new ArrayList<Integer>(N);
	int interval = N / 100;
	for (int i = 0; i < N; i++) nodeList.add(i); 
	nodeList.sort((x, y) -> degrees[y] - degrees[x]);
	for (int i = 0; i < N; i++){
		removeNode(nodeList.get(0));
		nodeList.sort((x, y) -> degrees[y] - degrees[x]);
		if (i % interval == 2) System.out.println(i + "\t" + sizeOfLargestComponent());
	}
  }

  void attackInfluencersFirst(){
	int interval = N / 100;
	for (int i = 0; i < N; i++){
//		computeNeighborsOfNeighbors();
		removeNode(topInfluencer());
		if (i % interval == 2) System.out.println(i + "\t" + sizeOfLargestComponent());
	}
  }

  public static void main(String[] args){
	NS11A ns11 = new NS11A();
	ns11.readNet(args[0]);
	ns11.computeDegrees();
	ns11.computeNeighborsOfNeighbors();
	//ns11.attackInfluencersFirst();
	//ns11.attackHubsFirst();
        ns11.randomFailures();
  }
}
