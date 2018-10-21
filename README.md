# EvolutionaryComputing70
This is the repository for the Evolutionary Computing 2018 programming assignment, group 70.

We implemented a self-adaptive mutation strategy with uncorrelated step size mutation, and investigated the effect of diversity (and hence tuning of tournament size for parent selection and number of created children per generation) on the mean computational budget it took to get a maximal fitness of 9.9 out of 10 on a Schaffers F7 benchmark function.

The shell script we used for our grid search is runInLoop.sh; this script also calls all the other components, namely it first compiles our java files (player70.java and Individual.java) and then performs 100 test runs on the Schaffers F7 function for each of 25 parameter sets.

This data is then plotted per run and saved in csvs; for each parameter set (or run set in our terminology), the runs are summarized with statistics about the number of generations it took to reach 9.9 fitness, the max eventual fitness, and some diversity measures based on the mean Euclidean distance of the individuals to the centroid of each generation. This saving of data is done in Python (saveRunsetResults.py) by reading the csv files per run generated in Java.

Ultimately, the results are plotted (plotRunsetResults.py) as a mean fraction of the computational budget, meaned over the 100 runs per parameter set/grid point.

