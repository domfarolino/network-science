// NS12A.java CS5172/6072 Cheng 2018
// the Louvain algorithm (step 1 only)
// Usage: java NS12A neighbors-file
import java.io.*;
import java.util.*;

public class NS12A{

	int N = 0;  // number of vertices/nodes
	int L = 0; // number of edges/links
	ArrayList<String> labels = new ArrayList<String>(); // node labels
	ArrayList<HashSet<Integer>> neighbors = new ArrayList<HashSet<Integer>>();
    	int[] degrees = null;
    	int[] membership = null;
    	int[] kc = null;
    	int[] Lc = null;

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

 
  void initializeCommunities(){
	Lc = new int[N]; kc = new int[N]; degrees = new int[N]; membership = new int[N];
        for (int i = 0; i < N; i++){
		Lc[i] = 0;
		kc[i] = degrees[i] = neighbors.get(i).size();
		membership[i] = i;
	}
   }

 void modularity(){  // (9.12)
	double M1 = 0;
	double M2 = 0;
	int numberOfCommunities = 0;
	for (int i = 0; i < N; i++) if (kc[i] > 0){ // for all communities
		numberOfCommunities++;
		M1 += Lc[i];   // sum of Lc 
		M2 += kc[i] * kc[i];   // sum of kc squared
	}
	M1 /= L;  // sum of Lc divided by L
	M2 = M2 / 4 / L / L;  // sum of kc squared divided by 2L squared
	System.out.println(numberOfCommunities + " " + (M1 - M2));
 }

 boolean louvainRound(){
	HashSet<Integer> communitiesVisited = new HashSet<Integer>();
	boolean changed = false;
	for (int i = 0; i < N; i++){
		double maxGain = 0;
		int currentCommunity = membership[i];  // i is in community A
		int bestCommunityToMoveIn = currentCommunity;
		communitiesVisited.clear();
		communitiesVisited.add(currentCommunity);
		int kiToBestC = 0;
		int kiA = 0;  
		// links from i to nodes in  current C or k_i,in in (9.58) for current C
		for (int j: neighbors.get(i)) if (membership[j] == currentCommunity) kiA++;
		ArrayList<Integer> narray = new ArrayList<Integer>();
		for (int j: neighbors.get(i)) narray.add(j);
		for (int k = 0; k < narray.size(); k++){
			int community = membership[narray.get(k)];
			if (communitiesVisited.contains(community)) continue;
			int kiB = 0; // k_i,in in (9.58) for a different C
			for (int l = k + 1; l < narray.size(); l++)
				if (membership[narray.get(l)] == community) kiB++;
			double gain = kiB - kiA +  // (dM(different C) - dM(current C)) / L (9.58)
		(kc[currentCommunity] - kc[community]) * degrees[i] * 1.0 / L / 2.0;
			if (gain > maxGain){ 
				maxGain = gain; 
				bestCommunityToMoveIn = community;
				kiToBestC = kiB;
			}
			communitiesVisited.add(community);
		}
		if (bestCommunityToMoveIn != currentCommunity){
			changed = true;
			membership[i] = bestCommunityToMoveIn;
			kc[currentCommunity] -= degrees[i];
			kc[membership[i]] += degrees[i];
			Lc[currentCommunity] -= kiA;
			Lc[membership[i]] += kiToBestC;
		}
	}
	return changed;
 }

 void louvainStep1(){
	modularity();  // initial modularity is negative
	while(louvainRound()){ 
           modularity();  // modularity increases
	   communities();
        }
 }

 void communities(){
	int[] sizes = new int[N];
	for (int i = 0; i < N; i++) sizes[i] = 0;
	for (int i = 0; i < N; i++) sizes[membership[i]]++;
	// when sizes[i] > 0, there is a community
	// for large networks, you may only want to present those with size > 10
}

 
 public static void main(String[] args){
   if (args.length < 1){ System.err.println("Usage: java NS12A neighbors-file");
	return; } 
   NS12A ns12 = new NS12A();
   ns12.readNet(args[0]);  
   ns12.initializeCommunities();
   ns12.louvainStep1();
   for (int i = 0; i < ns12.N; i++)
     System.out.println(ns12.labels.get(i) + " " + ns12.membership[i]);
 }   
} 
