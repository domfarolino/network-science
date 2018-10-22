# Assignment 7

IR8A.java finds the size of the "forward neighborhood" for each node in a network and prints
it along with the degree of the node, where "forward neighborhood" is the number of neighbors
with degree higher than the node.  Run it on stringentNet and port the output to Excel to plot
a forward neighborhood size vs degree scatter chart.

Modify IR8A so a backward neighborhood size (number of neighbors with smaller degrees) vs degree
scatter chart is generated.

IR8B.java does degree preserving randomization using random swapping using a technique not unlike
the random linking in configuration model.  Copy code from IR8A so you can generate forward neighborhood
size and backward neighborhood size vs degree charts.

IR8C.java performs full randomization which does not even preserve degree distribution.  Do the same and
plot the forward and backward neighborhood size vs degree charts.
