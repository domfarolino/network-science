// NS5A.java CS5172/6072 Cheng 2018
// This program reads a vertices/edges file and save the Laplacian
// It then uses Jama to find the eigenvalues and eigenvectors of the Laplacian 
// Usage: java -cp Jama-1.0.3.jar;. NS5A vertices/edges-file dim1 dim2

import java.io.*;
import java.util.*;
import java.awt.*;       
import java.awt.event.*; 
import javax.swing.*;    
import Jama.*;

public class NS5A extends JFrame {
   	public static final int CANVAS_WIDTH  = 600;
   	public static final int CANVAS_HEIGHT = 600;
	int N = 0;  // number of vertices/nodes
	int L = 0;  // number of edges/links
	String[] labels = null; // node labels
	int[][] edges = null;
	Matrix Laplacian = null; // the Laplacian
	double[] eigenvalues = null; // the eigenvalues
	Matrix V = null; // eigenvectors
   	double[] mins = new double[2];
   	double[] maxs = new double[2];
	double[][] X = null;
	int dim1 = 1; int dim2 = 2;

   private DrawCanvas canvas;

   public NS5A(String filename, int d1, int d2) {
      dim1 = d1; dim2 = d2;
      readNet(filename);
      computeEigen();
      canvas = new DrawCanvas();    
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH + 50, CANVAS_HEIGHT + 30));
  
      Container cp = getContentPane();
      cp.add(canvas);
  
      setDefaultCloseOperation(EXIT_ON_CLOSE);   
      pack();              
      setTitle("......");  
      setVisible(true);   
   }
  
   private class DrawCanvas extends JPanel {
      // Override paintComponent to perform your own painting
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);     // paint parent's background
         setBackground(Color.WHITE);  
	 double wrange = maxs[0] - mins[0];
	 double hrange = maxs[1] - mins[1];
	 int[][] pos = new int[N][2];
  	 for (int i = 0; i < N; i++){
            pos[i][0] = (int)((X[i][0] - mins[0]) * CANVAS_WIDTH / wrange) + 10;
	    pos[i][1] = (int)((X[i][1] - mins[1]) * CANVAS_HEIGHT / hrange) + 10;
	 }
         g.setColor(Color.BLACK);  
	 for (int i = 0; i < L; i++)
           g.drawLine(pos[edges[i][0]][0], pos[edges[i][0]][1], pos[edges[i][1]][0], pos[edges[i][1]][1]);
         for (int i = 0; i < N; i++)
           g.drawString(labels[i], pos[edges[i][0]][0], pos[edges[i][1]][1]);
     }
   }
  
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
	if (dim1 < 1 || dim1 >= N || dim2 < 1 || dim2 >=N){
		System.err.println("dimension(s) out of bound");
		System.exit(1);
	}
	labels = new String[N];
	edges = new int[L][2];
	double[][] laplacian = new double[N][N];
	for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) laplacian[i][j] = 0;
	for (int i = 0; i < N; i++){
		terms = in.nextLine().split("\t");
		labels[i] = terms[0];
	}
	for (int i = 0; i < L; i++){
		terms = in.nextLine().split("\t");
		int a = Integer.parseInt(terms[0]);
		int b = Integer.parseInt(terms[1]);
		edges[i][0] = a; edges[i][1] = b;
		laplacian[a][b] = laplacian[b][a] = -1.0;
		laplacian[a][a] += 1.0; laplacian[b][b] += 1.0;
	}
	in.close();
	Laplacian = new Matrix(laplacian);	
  }

  void computeEigen(){
	EigenvalueDecomposition ed = Laplacian.eig();
	eigenvalues = ed.getRealEigenvalues();
	V = ed.getV();
	X = new double[N][2];
	for (int i = 0; i < N; i++){
		X[i][0] = V.get(i, dim1);
		X[i][1] = V.get(i, dim2);
	}
   	mins[0] = mins[1] = 1000.0; 
	maxs[0] = maxs[1] = -1000.0;
	for (int i = 0; i < N; i++)
	  for (int j = 0; j < 2; j++){
		if (X[i][j] < mins[j]) mins[j] = X[i][j];
		if (X[i][j] > maxs[j]) maxs[j] = X[i][j];
	}
			
  }
   public static void main(String[] args) {
	if (args.length < 3){
		System.err.println("Usage: java NS5A vertices/edge-file dim1 dim2");
		return;
	}
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new NS5A(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2])); 
         }
      });
   }
}
