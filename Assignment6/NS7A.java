// NS7A.java CS5172/6072 2018 cheng
// graph6 decoding from stdin
// Usage: java NS7A 9 < graph9c.g6
// You need to come up with a scheme to generate a visual comparison
// between the Hamming distance of two graphs and the Euclidean distance
// of their laplacian eigenvalues
// Think how the eigenvalue distance can be replaced with distance between
// the Fiedler vectors but you don't have to do it.

import java.io.*;
import java.util.*;
import java.text.*;
import Jama.*;

public class NS7A{

    	int N = 0; 
	int numberOfPairs = 0;
	Matrix laplacian = null; 
	double[] eigenvalues = null; 
  	DecimalFormat decimalf = new DecimalFormat("#0.000");
	ArrayList<double[]> eigens = new ArrayList<double[]>();
	ArrayList<byte[]> edges = new ArrayList<byte[]>();

  void readNets(int n){
	N = n;
	numberOfPairs = N * (N - 1) / 2;
	Scanner in = new Scanner(System.in);
	while (in.hasNextLine()){
		String line = in.nextLine();
		if (line.charAt(0) != N + 63){
			System.err.println(line.charAt(0) - 63);
			System.exit(1);
		}
		System.out.println(line);
		handle(line);
	}
  }

  void handle(String graph6){
	double[][] A = new double[N][N];
	byte[] bits = new byte[numberOfPairs];
	boolean[] six = new boolean[6];
	int byteLen = numberOfPairs % 6 == 0 ? 
		numberOfPairs / 6 : numberOfPairs / 6 + 1;
	int pos = 0;
	for (int i = 1; i <= byteLen; i++){
		int encoding = graph6.charAt(i) - 63;
		for (int j = 5; j >= 0; j--){
			six[j] = (encoding & 1) != 0;
			encoding >>= 1;
		}
		for (int j = 0; j < 6; j++)
			if (pos < numberOfPairs)
				if (six[j]) bits[pos++] = 1;
				else bits[pos++] = 0;
			else break;
	}
	for (int i = 0; i < numberOfPairs; i++) System.out.print(bits[i]);
	System.out.println();
	int n = 0;
	for (int i = 1; i < N; i++) for (int j = 0; j < i; j++)
		A[i][j] = A[j][i] = bits[n++] == 0 ? 0: -1.0;
	for (int i = 0; i < N; i++){
		A[i][i] = 0;
		for (int j = 0; j < N; j++) if (j != i && A[i][j] != 0) A[i][i]++;
	}
	laplacian = new Matrix(A);
	EigenvalueDecomposition ed = laplacian.eig();
	eigenvalues = ed.getRealEigenvalues();
	for (int i = 0; i < N; i++) 
		System.out.print(decimalf.format(eigenvalues[i]) + " ");
	System.out.println();
	edges.add(bits); eigens.add(eigenvalues);
  }

  double EuclideanDistance(double[] a, double[] b){
	double sum = 0;
	for (int i = 0; i < N; i++){
		double d = a[i] - b[i];
		sum += d * d;
	}
	return Math.sqrt(sum);
  }
	
  int HammingDistance(byte[] a, byte[] b){
	int sum = 0;
	for (int i = 0; i < numberOfPairs; i++) if (a[i] != b[i]) sum++;
	return sum;
  }

  void distanceComparison(){
	int numberOfGraphs = edges.size();
	for (int i = 1; i < numberOfGraphs; i++)
		for (int j = 0; j < i; j++){
			int d1 = HammingDistance(edges.get(i), edges.get(j));
			double d2 = EuclideanDistance(eigens.get(i), eigens.get(j));
                        System.out.println(d1 + "	" + d2);
		}
  }

  public static void main(String[] args){
	NS7A ns7 = new NS7A();
	ns7.readNets(Integer.parseInt(args[0]));
	ns7.distanceComparison();
  }
}

    
