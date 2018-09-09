// NS3A.java CS5172/6072 2018 Cheng
// usage: java NS3A neighborsFile
// uses BFS to find pairwise shortest paths
// uses BFS to find connected components

import java.io.*;
import java.util.*;

class NS3A{

  int N = 0;  // number of vertices/nodes
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
  }
  in.close();
  N = labels.size();
  }

  int bfsDistances(int startVertex){  // return longest distances from start
  HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
  Queue<Integer> queue = new LinkedList<Integer>();
  queue.add(startVertex);
  int maxDistance = 0;
  distances.put(startVertex, 0);
  while (queue.peek() != null){
    int h = queue.remove();
    int d = distances.get(h);
    if (d > maxDistance) maxDistance = d;
    neighbors.get(h).forEach(j -> { if (!distances.containsKey(j)){
      distances.put(j, d + 1); queue.add(j); }});
  }
  return maxDistance;
  }

  int findDiameter(){
  int maxDistance = 0;
  for (int i = 0; i < N; i++){
    int newDist = bfsDistances(i);
    //System.out.println(i + " " + newDist);
    if (newDist > maxDistance) maxDistance = newDist;
  }
  return maxDistance;
  }

  HashSet<Integer> bfsReachable(int startVertex) {  // with no distances from start
    HashSet<Integer> reachables = new HashSet<Integer>();
    Queue<Integer> queue = new LinkedList<Integer>();

    queue.add(startVertex);
    reachables.add(startVertex);

    while (!queue.isEmpty()) {
      int h = queue.remove();

      neighbors.get(h).forEach(j -> {
        if (!reachables.contains(j)) {
          reachables.add(j);
          queue.add(j);
        }
      });
    }

    return reachables;
  }

  HashSet<Integer> dfsReachable(int startVertex){ // replace queue with stack
  HashSet<Integer> reachables = new HashSet<Integer>();
  Stack<Integer> stack = new Stack<Integer>();
  stack.push(startVertex); reachables.add(startVertex);
  while (!stack.isEmpty()){
    int h = stack.pop();
    neighbors.get(h).forEach(j -> { if (!reachables.contains(j)){
      reachables.add(j); stack.push(j); }});
  }
  return reachables;
  }


  void allComponents(){
    int[] components = new int[N];
    int n = 1; int m = 0;
    for (int i = 0; i < N; i++)
      components[i] = -1;

    while (m < N) {
      int i = 0;
      for (; i < N; i++)
        if (components[i] < 0) break;

      HashSet<Integer> newComponent = bfsReachable(i);
      int size = newComponent.size();

      for (int j : newComponent)
        components[j] = n;

      System.out.println("Component " + n + " has " + size + " nodes.");
      n++;
      m += size;
    }
  }

 public static void main(String[] args){
   if (args.length < 1){
     System.err.println("Usage: java NS3A neighborsFile");
     System.exit(1);
   }
   NS3A ns3 = new NS3A();
   ns3.readNet(args[0]);
   ns3.allComponents();
   int maxDistance = ns3.findDiameter();
   System.out.println("Diameter of the graph: " + maxDistance);
 }
}
