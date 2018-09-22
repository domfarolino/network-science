// NS4A.java CS5172/6072 2018 Cheng
// usage: java NS4A N
// random graph generator with number of nodes N
// randomly adds a link a time and reports <k> and N_G (size of the largest component)
// for results close to the theoretical phase transition points, use N >= 10000.

import java.io.*;
import java.util.*;

class NS4A{

    static final int numberOfSamples = 40;
    int N = 0;
    ArrayList<HashSet<Integer>> neighbors = null;
    Random random = new Random();

    public NS4A(int n){
        N = n;  
  neighbors = new ArrayList<HashSet<Integer>>(N);
  for (int i = 0; i < N; i++) neighbors.add(new HashSet<Integer>());
    }

    void addLinks(){
  int reportingInterval = N / numberOfSamples;
  double maxL = Math.log((double)N) * N / 2;  // reaching N_G = N
  int L = 0;
  while (L < maxL){
    int a = random.nextInt(N);
    int b = random.nextInt(N);
    if (a != b && !neighbors.get(a).contains(b)){
      neighbors.get(a).add(b);
      neighbors.get(b).add(a);
      if (L++ % reportingInterval == 0) reportNG(L);
    }
  }
    }

    void reportNG(int L){
     System.out.print(((2.0 * L) / N)); // mean degree <k>
     allComponents();
    }

  HashSet<Integer> bfsReachable(int startVertex){  // with no distances from start
  HashSet<Integer> reachables = new HashSet<Integer>();
  Queue<Integer> queue = new LinkedList<Integer>();
  queue.add(startVertex); reachables.add(startVertex);
  while (!queue.isEmpty()){
    int h = queue.remove();
    neighbors.get(h).forEach(j -> { if (!reachables.contains(j)){
      reachables.add(j); queue.add(j); }});
  }
  return reachables;
  }

  void allComponents(){
     int[] components = new int[N];
     int n = 1; int m = 0;  int Ng = 0; int next = 0;
     for (int i = 0; i < N; i++) components[i] = -1;
     while (m < N){
        int i = 0; for (; i < N; i++) if (components[i] < 0) break;
  HashSet<Integer> newComponent = bfsReachable(i);
  int size = newComponent.size();
  for (int j : newComponent) components[j] = n;
  if (size > Ng){ next = Ng; Ng = size; }
  else if (size > next) next = size;
  n++; m += size;
     }
     System.out.println("\t" + Ng + "\t" + next);
   }

 
 public static void main(String[] args){
   if (args.length < 1){
     System.err.println("Usage: java NS4A N");
     System.exit(1);
   }
   NS4A ns4 = new NS4A(Integer.parseInt(args[0]));
   System.out.println("<k>\tNg\tSecond Largest");
   ns4.addLinks();
 }
}
