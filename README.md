# INFO6205_304
Genetic Algorithm Project:

Problem
Since weight initialization can have a profound impact on both the convergence rate and final quality of a network and Genetic Algorithm is a very useful approach on NP hard problem, we created a genetic algorithm to improve weight initialization for back propagation neural network. The problem our neural network is trying to solve is a classical recognition problem on iris flower. There are four decimal numbers as input data which represent the features of three types of iris flower, and the trained model is used to recognize the type of an iris flower. The data set is public in the UCI Machine Learning Repository: https://archive.ics.uci.edu/ml/datasets/iris.

Implementation Design
Genotype& Phenotype: The genotype and phenotype of our algorithm is basically equal and another way of implementing them in separate expression is still under experiment. The genotype includes two matrixes which is the weight of hidden layer and output layer, because the structure of our network is a 3-layer BP neural network. Given the number of input is X, the number of nodes in hidden layer is Y and the number of output is Z, then the first matrix is X×Y and the second matrix is Y×Z and each wij is a decimal number between -1 and +1.
(■(w_11&⋯&w_1y@⋮&⋱&⋮@w_x1&⋯&w_xy ))                                       (■(w_11&⋯&w_1z@⋮&⋱&⋮@w_y1&⋯&w_yz ))
Fitness function: The fitness of one generation is closely relevant to performance of each model. we represented the performance of a model using variance between the output result and standard output. Hence, if we want to find the best solution, then it means we are going to find an individual which has the smallest variance. The fitness of generation is f(generation) = 1/ s(generation), and the s(generation) is the variance of a model. The best, worst, average fitness was calculated for each generation and logged in the file.
Evolution: A single evolution includes processes of selection, crossover, and mutation. We used a Priority Queue to place the individuals and created a customized comparator class to maintain their order by the fitness. For each generation, we choose individuals and let them sexually reproduce using roulette method, and each child would experience mutation with a fixed probability 0.01, then we and kept the half of new individuals and another half is selected from the parent population. 


Contributer:
Shouting Lyu,
Chang Liu
