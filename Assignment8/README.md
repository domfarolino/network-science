# Assignment 8

NS9A.java implements Barabasi-Albert with linear preferential attachment.  A snapshot of degrees
at iteration N-dt is taken and the dk for nodes are computed using the final degrees.  This dk is
used to estimate Pi(k), the degree-dependent probability for neighbor selection.  pi(k), the cumulative
preferential attachment function is computed and output.  Use Excel to plot the pi(k) vs k curve on log-log
scale and fit a power law (straight line on log-log) to the data.  (You may want to start a scatter plot using
only data points from k = m to half of kmax, in order to have the right weight and balance for the fit to get something
close to pi(k) = Ck^2 (Eq.18 of note).  Report the degree exponent for pi(k).

NS9B.java is Model A of Barabasi-Albert, without PA.  Add code so that you can also generate and plot the pi(k) function.
