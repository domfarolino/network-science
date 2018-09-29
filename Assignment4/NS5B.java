// NS5B.java CS5172/6072 Cheng 2018
// This program reads a vertices/edges file and save the adjacency matrix A
// It then uses Jama to find the eigenvalues and eigenvectors of A
// Usage: java -cp Jama-1.0.3.jar;. NS5B vertices/edges-file

import java.io.*;
import java.util.*;
import Jama.*;

public class NS5B{
	int N = 0;  // number of vertices/nodes
	int L = 0;  // number of edges/links
	String[] labels = null; // node labels
	int[][] edges = null;
	Matrix adjacency = null; // the adjacency
	double[] eigenvalues = null; // the eigenvalues
  
  void readNet(String filename){
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found");
		System.exit(1);
	}
	String[] terms = in.nextLine().split("\t");
	N = Integer.parseInt(terms[0]);  L = Integer.parseInt(terms[1]);
	labels = new String[N];
	edges = new int[L][2];
	double[][] A = new double[N][N];
	for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) A[i][j] = 0;
	for (int i = 0; i < N; i++){
		terms = in.nextLine().split("\t");
		labels[i] = terms[0];
	}
	for (int i = 0; i < L; i++){
		terms = in.nextLine().split("\t");
		int a = Integer.parseInt(terms[0]);
		int b = Integer.parseInt(terms[1]);
		edges[i][0] = a; edges[i][1] = b;
		A[a][b] = A[b][a] = 1.0;
	}
	in.close();
	adjacency = new Matrix(A);	
  }

  void computeEigen(){
	EigenvalueDecomposition ed = adjacency.eig();
	eigenvalues = ed.getRealEigenvalues();
  }

  void countTriangles(){
      int n = 0;
      for (int i = 0; i < N; i++)
	for (int j = i + 1; j < N; j++) if (adjacency.get(i, j) > 0)
		for (int k = j + 1; k < N; k++) 
			if (adjacency.get(j, k) > 0 && adjacency.get(i, k) > 0)
			n++;
      System.out.println("Number of triangles = " + n);
  }

  void sumsOfEigenvalues(){
	double sum = 0;
	for (int i = 0; i < N; i++) sum += eigenvalues[i];
	System.out.println(sum);
	double sum2 = 0;
	for (int i = 1; i < N; i++) sum2 += eigenvalues[i] * eigenvalues[i];
	System.out.println(sum2);
	double sum3 = 0;
	for (int i = 0; i < N; i++) sum3 += eigenvalues[i] * eigenvalues[i] * eigenvalues[i];
	System.out.println(sum3);
  }

 public static void main(String[] args) {
	NS5B ns5 = new NS5B();
	ns5.readNet(args[0]);
	ns5.computeEigen();
	ns5.countTriangles();
	ns5.sumsOfEigenvalues();
   }
}
