# Assignment 15

NS16A.java enumerates maximal frequent itemsets (bicliques) in a bipartite network.  Run it on a bipartite
network (adInverted5-100.txt as a term-document matrix) and save the output on a specific minimum support
(for the definition of "frequent", 7 for adInverted5-100 may be a choice).

NS16B.java is similar to NS13B.java and it construct a biclique graph based on a minimum overlap size and
finds all connected components and map them back to the original bipartite network (as dense submatrices).
Find a good minimum overlap size and show the resulting biclique percolation communities in the original network.
