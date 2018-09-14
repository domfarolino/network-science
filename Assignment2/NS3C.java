// NS3C.java CS5172/6072 2018 Cheng
// find connected components in a bipartite network
// Usage: java NS3C bipartite-neighbors-file
// the bipartite-neighbors-file contains baskets (with names) of items (as integers)


import java.io.*;
import java.util.*;

public class NS3C{

  int N = 0;  // number of baskets
  int M = 0;  // number of items
  ArrayList<String> labels = new ArrayList<String>(); // basket labels
  ArrayList<HashSet<Integer>> outNeighbors = new ArrayList<HashSet<Integer>>();
  ArrayList<HashSet<Integer>> inNeighbors = new ArrayList<HashSet<Integer>>();
  int[] componentsA = null;  int[] componentsB = null;
  HashSet<Integer> forward = new HashSet<Integer>();
  HashSet<Integer> backward = new HashSet<Integer>();
  int numberOfComponents = 0;
  

  void readNet(String filename){  // fill outNeighbors, M and N
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
    for (int j = 1; j < terms.length; j++){
      int item = Integer.parseInt(terms[j]);
      if (item > M) M = item;
      hset.add(item);
    }
    outNeighbors.add(hset);
  }
  in.close();
  N = labels.size(); M++; 
  }

 void makeBackwardNet() {  // make inNeighbors from outNeighbors, need your code
  for (int i = 0; i < M; i++)
    inNeighbors.add(new HashSet<Integer>());  // all empty

  // your code to fill them
  for (int i = 0; i < M; i++) {
    for (int j: outNeighbors.get(i)) {
      inNeighbors.get(j).add(i);
    }
  }
 }

 void allComponents(){  
  componentsA = new int[N]; componentsB = new int[M];
  for (int i = 0; i < N; i++) componentsA[i] = -1; 
  for (int i = 0; i < M; i++) componentsB[i] = -1; 
  for (int i = 0; i < N; i++) if (componentsA[i] == -1){
    forward.clear(); backward.clear();
    forward.add(i);
    expandA(i);
    System.out.println("Component " + numberOfComponents +
      " " + forward.size() + " " + backward.size());
    numberOfComponents++;
  }
  for (int i = 0; i < M; i++) if (componentsB[i] == -1)
    System.out.println("Isolated item " + i);
 }


  void expandA(int basket){
  for (int j : outNeighbors.get(basket)) if (componentsB[j] == -1){
    componentsB[j] = numberOfComponents;
    backward.add(j);
    expandB(j);
  }
  }

  void expandB(int item){
  for (int j : inNeighbors.get(item)) if (componentsA[j] == -1){
    // You complete the section
    componentsA[j] = numberOfComponents;
    forward.add(j);
    expandA(j);
  }
  }


 public static void main(String[] args){
  NS3C ns3 = new NS3C();
  ns3.readNet(args[0]);
  ns3.makeBackwardNet();
  ns3.allComponents();
 }
}
