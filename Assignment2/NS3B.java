// NS3B.java CS5172/6072 Cheng 2018
// decomposes a directed network into strongly connected components
// Usage: java NS3B outNeighbors-file
import java.io.*;
import java.util.*;

public class NS3B{

  int N = 0;  // number of vertices/nodes
  ArrayList<String> labels = new ArrayList<String>(); // node labels
  ArrayList<HashSet<Integer>> outNeighbors = new ArrayList<HashSet<Integer>>();
  ArrayList<HashSet<Integer>> inNeighbors = new ArrayList<HashSet<Integer>>();
  int[] components = null;  
  HashSet<Integer> forward = new HashSet<Integer>();
  HashSet<Integer> backward = new HashSet<Integer>();
  int numberOfSCCs = 0;

  void readNet(String filename){  // fill outNeighbors and N
  Scanner in = null;
  try {
    in = new Scanner(new File(filename));
  } catch (FileNotFoundException e){
    System.err.println(filename + " not found");
    System.exit(1);
  }
  while (in.hasNextLine()) {
    String[] terms = in.nextLine().split(" ");
    labels.add(terms[0]);
    HashSet<Integer> hset = new HashSet<Integer>();
    for (int j = 1; j < terms.length; j++) hset.add(Integer.parseInt(terms[j]));
    outNeighbors.add(hset);
  }
  in.close();
  N = labels.size(); 
  }

 // Fills up/initializes |inNeighbors|
 void makeBackwardNet() {  // make inNeighbors from outNeighbors, need your code
  for (int i = 0; i < N; i++)
    inNeighbors.add(new HashSet<Integer>());  // all empty

  // your code to fill them
  for (int i = 0; i < N; i++) { // snek
    for (int j: outNeighbors.get(i)) {
      inNeighbors.get(j).add(i);
    }
  }
 }

 // Modifies the global |forward| to contain all reachable nodes from `v`
 // Note this is not limited to direct neighbors. Requires initialization of |outNeighbors|
 void forwardReach(int v){  // all nodes reachable from v
  forward.add(v);  // the result is in forward
  for (int j: outNeighbors.get(v)) {
    if (components[j] < 0 && !forward.contains(j))
      forwardReach(j);
  }
 }

 // Modifies the global |backward| to contain all nodes that can reach `v`
 // Note this is not limited to direct neighbors. Requires initializatino of |inNeighbors|
 void backwardReach(int v){  // all nodes that can reach v
  backward.add(v);  // the result is in backward
  for (int j: inNeighbors.get(v)) {
    if (components[j] < 0 && !backward.contains(j))
      backwardReach(j);
  }
 }

 void findSCCs(){  // only report those with more than one node
  components = new int[N];  // this keeps track of which nodes have already been counted in a component
  for (int i = 0; i < N; i++)
    components[i] = -1; 

  for (int i = 0; i < N; i++) {
    if (components[i] == -1) { // this is written poorly. /sigh
      forward.clear();
      backward.clear();
      forwardReach(i);
      backwardReach(i);

      //System.out.println(i + " has " + forward.size() + " outgoing connections, and " + backward.size() + " incoming connections");
      forward.retainAll(backward);  // Forward ∩ Backward
      int size = forward.size();  // size of the latest SCC

      // Only report the connected component # and size if the size > 1
      if (size > 1)
        System.out.println(numberOfSCCs + " " + forward.size());

      // We're dealing with a connected component at least of size 1, so mark
      // everything in the forward and backward lists as belonging to a component
      for (int j: forward)
        components[j] = numberOfSCCs;
      for (int j: backward)
        components[j] = numberOfSCCs;

      numberOfSCCs++;
    }
  }
 }

 public static void main(String[] args) {
   if (args.length < 1) {
     System.err.println("Usage: java NS3B neighbors-file");
     return;
   }

   NS3B ns3 = new NS3B();
   ns3.readNet(args[0]);
   ns3.makeBackwardNet();
   ns3.findSCCs();
   System.out.println("Number of strongly connected components " + ns3.numberOfSCCs);
 }
}
