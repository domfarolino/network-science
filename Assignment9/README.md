# Assignment 9

NS10A.java reads a network, computes the degree correlation function knn(k) and print it out.

NS10B.java reads a network, runs Xulvi-Brunet and Sokolov algorithm to maximize the degree
correlation (assortativity).

NS10C.java generates a random network with N and <k> (suggested values N = 10,000 and <k> = 3.0).

Modify NS10B.java so that you can also tune it to maximally disassortative.

Generate a random network using NS10C and use NS10A to compute its knn(k).  Then use NS10B and your
version of it to maximize assortativity and disassortativity and then compute the knn(k) for the
rewired networks.  Use excel to generate scatter plots of knn(k) vs k and find the correlation
exponent (mu) for each of them.

Do the same to stringentNet.
