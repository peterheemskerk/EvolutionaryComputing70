import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.*;
// import java.io.PrintWriter;
// import java.io.File;

// import org.apache.commons.lang.ArrayUtils;


import java.util.Random;
import java.util.Properties;

public class player70 implements ContestSubmission
{
	public static Random rnd_;
	static ContestEvaluation evaluation_; 				//TODO Deze heb ik zelf static gemaakt om een error op te lossen; misschien is dat niet de way to go
    	private int evaluations_limit_;

	private static final int GENERATION_LIMIT = 1000000;		// TODO - default = 10000
	private static final int NUMBER_OF_INDIVIDUALS = 100; 		// size of population
	private static final int CHILDREN_PER_GENERATION = 15;
	private static final boolean SIGMA_MUT = true;			// SIGMA_MUT = true: mutation using Normal(Sigma) - false: random swap
	private static final boolean ONE_CHILD = false;			// ONE_CHILD = false: 2 childres produced - true: one child produced from 2 parents
	private static final double RANDOM_MUTATION_PROB = 0;		// between 0 and 1. kans dat een gecreerd kind nog volledige random mutatie van 1 van zijn genen (incl. sigma) kri
	private static final double FITNESS_TRESHOLD = 9.9;		// use to determine number of iterations needed to reach this fitnes treshold. 


	private static final double tau = 1/Math.sqrt(10); 		// Learning rate, problem size (n) heb ik geinterpreteerd als dimensies van het fenotype (dat is de consensus ook op Slack op het moment)
	private static final double epsilon = 1E-6; 			// Machine precision

	public static int evals = 0;

	public player70()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        	// Get evaluation limit
        	evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        	boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        	boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        	boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        	if(isMultimodal){
   	  		// Do sth
   		}else{
   	         	// Do sth else
   	     	}
 	}
   
	public void run()
	{
		// Run your algorithm here
		int generation = 0;
		int generation_treshhold = 1000000;	
		boolean treshhold_reached = false;
		final Random randomNumbers = new Random();			// random number generator

		// print to csv
		System.out.println("Generation; Max; Min; Mean");

        	// init population
		Individual[] currentPop = initPopulation(NUMBER_OF_INDIVIDUALS);
        	// calculate fitness - v1.1 fitness initiele populatie wordt nu berekend door externe evaluatie. 
		for (int i = 0; i < currentPop.length; i++)
		{
    			// Check fitness of unknown fuction
    			Double fitness = (double) evaluation_.evaluate(currentPop[i].getFenotype());
			currentPop[i].setFitness(fitness);
            		evals++;
		}		

        	while(evals<evaluations_limit_ && generation < GENERATION_LIMIT)
		{
            		// Select parents	
			int num_individuals = currentPop.length; 
			Individual[] newParents = selectParents(currentPop);
			int num_children = CHILDREN_PER_GENERATION;

            		// Apply crossover / mutation operators
			Individual[] newChildren = createChildren(num_children, newParents, SIGMA_MUT, ONE_CHILD);

			// Test fitness van gecreerde children
			for (int i = 0; (i < newChildren.length && evals<evaluations_limit_); i++)
			{
    				// Check fitness of unknown fuction
    				Double fitness = (double) evaluation_.evaluate(newChildren[i].getFenotype());
				newChildren[i].setFitness(fitness);
				// Check if fitness treshold is reached
				// if (treshhold_reached == false && fitness > FITNESS_TRESHOLD) 
				// {
				// 	generation_treshhold = generation;
				//	treshhold_reached = true;
				// }	
				// Next Evaluation
    				evals++;
			}  
			// Select survivors
			Individual[] newPop = selectSurvivors(currentPop, newChildren);	

			// Calculate parameters of new population and generate output
			double[] divresults = divPop(newPop);
			double maxofgen = divresults[0];
			double minofgen = divresults[1];
			double mean_fitness = divresults[2];
			double diversityX = divresults[3];
			double diversitySigma = divresults[4];
			double totaldistSigma = divresults[5];

			System.out.println(generation + "; " + maxofgen + "; " + minofgen + "; " + mean_fitness + "; " + diversityX + "; " + diversitySigma + "; " + totaldistSigma );
			// System.out.print("DIT WAS GENERATIE: ");
			// System.out.println(generation);

			if (treshhold_reached == false && maxofgen > FITNESS_TRESHOLD) 
			{
				generation_treshhold = generation;
				treshhold_reached = true;
			}	

			// Determine next generation
			currentPop = newPop;
			generation++;

		}	// Endwhile
		System.out.println("Generation; Max Fitness; Min Fitness - Mean Fitness - Diversity X - Diversity Sigma - Total Sigma");
		System.out.print("GENERATION AT WHICH FITNESS TRESHOLD ");
		System.out.print(FITNESS_TRESHOLD);
		System.out.print(" IS REACHED: ");
		System.out.println(generation_treshhold +1);

	}	// Endrun()


	// below group70 - procedures

	public static Individual[] initPopulation(int num_individuals)
	{
		Individual[] population = new Individual[ num_individuals ];
		for (int count = 0; count < population.length; count++ )
		{
			double[] randomGenotype = new double[20];
			for (int i = 0; i < 10; i++) // First fill in the sigmas of the random genotype
			{
				randomGenotype[i] = rnd_.nextDouble(); // Get a double between 0.0 and 1.0 from the random number generator
				if (randomGenotype[i]<epsilon)
				{
					randomGenotype[i]=epsilon; // If sigma is smaller than machine precision, make it machine precision
				}
			}

			for (int i = 10; i < 20; i++) // Now fill in the "x-es" of the random genotype
			{
				double min = -5;
				double max = 5;
				randomGenotype[i] = min + Math.random() * (max - min);
				// randomGenotype[i] = rnd_.nextDouble()-0.5; // Get a double between -0.5 and 0.5 from the random number generator
			}
			population[ count ] = new Individual(randomGenotype);
		}

		return population; //initpop;
	}

	public static void detFitnessPop(Individual[] population) // Waarom willen we de fitness van een populatie weten?
	{
		for ( int count = 0; count < population.length; count++ )
		{
			Double fitness = (double) evaluation_.evaluate(population[count].getFenotype());
			population[count].setFitness(fitness);
			evals++; //TODO Hier wordt dus ook een evaluatie gemaakt, let op.

		}
	}

	public static double[] divPop(Individual[] population) 
		// calculate diversity of population
	{
		// step 1 - calculate center for each dimension
		double[] centerX = new double[10];
		double[] centerSigma = new double[10];
		double[] totalX = {0,0,0,0,0,0,0,0,0,0};
		double[] totalSigma = {0,0,0,0,0,0,0,0,0,0};
		double[] currentGenotype;
		for ( int count = 0; count < population.length; count++ )

		{
			currentGenotype = population[count].getGenotype(); // Dit bespaart ongeveer 900 keer getGenotype aanroepen
			for ( int i = 0; i < 10; i++ )
			{
				totalX[i] += currentGenotype[i+10];
				totalSigma[i] += currentGenotype[i];
			}				
		}
		for ( int i = 0; i < 10; i++ )
		{
			centerX[i] = totalX[i] / population.length;
			centerSigma[i] = totalSigma[i] / population.length;
		}

		// step 2 - calculate average distances for each dimension
		double[] totaldistX = {0,0,0,0,0,0,0,0,0,0};
		double[] totaldistSigma = {0,0,0,0,0,0,0,0,0,0};
		for ( int count = 0; count < population.length; count++ )
		{
			for ( int i = 0; i < 10; i++ )
			{
				totaldistX[i] += (population[count].getGenotype()[i+10] - centerX[i]);
				totaldistSigma[i] += (population[count].getGenotype()[i] - centerSigma[i]);
			}				
		}
		double[] averageX = new double[10];
		double[] averageSigma = new double[10];
		for ( int i = 0; i < 10; i++ )
		{
			averageX[i] = totaldistX[i] / population.length;
			averageSigma[i] = totaldistSigma[i] / population.length;
		}
		// bereken Manhattan distance voor X en voor sigma
		double diversityX = 0;
		double diversitySigma = 0;
		double quasumdiversityX = 0;
		double quasumdiversitySigma = 0;
		double totalcenterSigma = 0;

		for ( int i = 0; i < 10; i++ )
		{
			quasumdiversityX += Math.pow(averageX[i],2);
			quasumdiversitySigma += Math.pow(averageSigma[i],2);

			totalcenterSigma += centerSigma[i];
		}

		diversityX = Math.sqrt(quasumdiversityX);
		diversitySigma = Math.sqrt(quasumdiversitySigma);
		// calculate mean, max of genes, min of genes (Tessa's procedure)
		double maxofgen = 0.0;
		double minofgen = 10.0;
		double mean = 0.0;
		for (int i = 0; i < population.length; i++){
			if (population[i].getFitness() > maxofgen) {
				maxofgen = population[i].getFitness();
				}
			if (population[i].getFitness() < minofgen) {
				minofgen = population[i].getFitness();
				}
			mean += population[i].getFitness();
			}
			mean = mean / population.length;
		// return values in an array
		return new double[] {maxofgen, minofgen, mean, diversityX, diversitySigma, totalcenterSigma } ;
		// return diversitysigma;
		// return diversityX;
	}

	public static void printPop(Individual[] population)
	{
		System.out.println("printpop....:");
		for ( int count = 0; count < population.length; count++)
		{
			//DEBUG System.out.printf("Individu %d: ", count);
			population[count].displayFenotype();
		}
	}

	public static Individual[] selectParents( Individual[] population )
	{	// select parents with tournament selection

		int num_of_parents = 2 * CHILDREN_PER_GENERATION;
		Individual[] parents = new Individual[num_of_parents];
		int parentInd = 0;
		int tournamentSize = 5;
		Individual bestIndividual = new Individual();
		Individual secondBestIndividual = new Individual();

		while (parentInd < (num_of_parents-1)) {

			double max_fitness = 0.0;
			double second_max_fitness = 0.0;

			for (int i = 0; i < tournamentSize; i++) { //Deze pool hoeft toch niet per se gelijk te zijn aan number of parents? Hadden we niet 20% gezegd?
				int random_int = ThreadLocalRandom.current().nextInt(0, NUMBER_OF_INDIVIDUALS);   // TODO - hier stond 100, dit klopt waarschijnlijk ?
				double currentFitness = population[random_int].getFitness(); // Dit bespaart wat tijd

				if (currentFitness > max_fitness) {
					max_fitness = currentFitness;
					bestIndividual = population[random_int]; // Individu opslaan ipv genen, dan behoud je ook de fitness evaluation

				}
				if (currentFitness < max_fitness && currentFitness > second_max_fitness) {
					second_max_fitness = currentFitness;
					secondBestIndividual = population[random_int];
				}
			}

			parents[parentInd] = bestIndividual;
			parentInd++;
			parents[parentInd] = secondBestIndividual;
			parentInd++;
		}
		return parents;
	} 

	public static Individual[] createChildren( int numChildren, Individual[] parents, boolean sigma_mut, boolean oneChild)
		// children worden gemaakt uit parents
		// sigma_mut is switch die aangeeft of random swap (false) dan wel normal selfadaptive sigma mutation (true) wordt gebruikt
		// oneChild is switch die aangeeft of uit twee parents 1 kind (true) dan wel 2 kinderen worden gecreeerd (false)

	{
		Random rand = new Random(); //TODO Is een aparte random number generator per individu okee of willen we een centrale gebruiken?

		// create empty child population with right number
		Individual[] newChildren = new Individual[numChildren];
		
		int parentcount = 0;		// TODO volgens mij werkt deze procedure alleen als aantal parents > aantal kinderen. 

		if (oneChild)  // als slechts 1 kind uit 2 ouders wordt geproduceerd   TODO naar paramters brengen
		{
			for ( int count = 0; count < numChildren; count++ )
			{
				Individual parent1 = parents[parentcount];
				parentcount++;
				Individual parent2 = parents[parentcount];
				parentcount++;

				double[] childGenome = recombineGenotypes(parent1.getGenotype(), parent2.getGenotype());
				Individual newChild = new Individual(childGenome);

				// random mutation
				newChild.mutGenotype(newChild.getGenotype(), tau, sigma_mut);

				// total random mutation for certain cases - not implemented her
				if (rand.nextDouble() < RANDOM_MUTATION_PROB)	
				{	
					newChild.mutGenotypeRandom(newChild.getGenotype());
				}

				// voeg nieuw kind toe aan de lijst
				newChildren[count] = newChild;
			}
		}
		else		// als 2 kinderen uit 2 ouders wordt geproduceerd. 
		{
			for ( int count = 0; count < numChildren; count=count+2 )
			{			
				Individual parent1 = parents[parentcount];
				parentcount++;
				Individual parent2 = parents[parentcount];
				parentcount++;

				double[] child1Genome = recombineGenotypes_twee(parent1.getGenotype(), parent2.getGenotype())[0];
				double[] child2Genome = recombineGenotypes_twee(parent1.getGenotype(), parent2.getGenotype())[1];

				Individual newChild1 = new Individual(child1Genome);
				Individual newChild2 = new Individual(child2Genome);

				// Perform the mutation on the childs after recombination (Normal(sigma) or random swap)
				newChild1.mutGenotype(newChild1.getGenotype(), tau, sigma_mut);
				newChild2.mutGenotype(newChild2.getGenotype(), tau, sigma_mut);

				// Perform total random mutation for certain cases
				if (rand.nextDouble() < RANDOM_MUTATION_PROB)	
				{	
					newChild1.mutGenotypeRandom(newChild1.getGenotype());
					newChild2.mutGenotypeRandom(newChild2.getGenotype());
				}
				newChildren[count] = newChild1;
				if (count < numChildren-1) {newChildren[count+1] = newChild2;}		
			}
		}
		return newChildren;
	}

	public static double[] recombineGenotypes(double[] genotype1, double[] genotype2)
	{	
		double[] childGenotype = new double[20];
		for (int i = 0; i < genotype1.length; i++)
		{
			// commented: als we gemiddelde nemen van allebei de ouders
			// childGenotype[i] = (genotype1[i] + genotype2[i])/2;
			if (rnd_.nextBoolean()) // Pak een random true of false, aan de hand daarvan het ene of andere genoom samplen. Wordt nu geen onderscheid tussen sigma en x gemaakt
			{
				childGenotype[i] = genotype1[i];	
			}
			else
			{
				childGenotype[i] = genotype2[i];
			}
		}
		return childGenotype;
	}


	public static double[][] recombineGenotypes_twee(double[] genotype1, double[] genotype2)
	{
		double[] childGenotype1 = new double[20];
		double[] childGenotype2 = new double[20];
		for (int i = 0; i < genotype1.length; i++)
		{
			if (rnd_.nextBoolean()) // Pak een random true of false, aan de hand daarvan het ene of andere genoom samplen. Wordt nu geen onderscheid tussen sigma en x gemaakt
			{
				childGenotype1[i] = genotype1[i];
				childGenotype2[i] = genotype2[i];
			}
			else
			{
				childGenotype1[i] = genotype2[i];
				childGenotype2[i] = genotype1[i];
			}
		}
		return new double[][] {childGenotype1, childGenotype2};
	}

	public static Individual[] selectSurvivors(Individual[] parentPop, Individual[] childrenPop) 
	{
		double[] fitnesslist = new double[NUMBER_OF_INDIVIDUALS + CHILDREN_PER_GENERATION];
		Individual[] newPop = new Individual[NUMBER_OF_INDIVIDUALS];
		// int number_in_choicelist = 0;

		// add all fitness scores to a list
		for (int i = 0; i < (parentPop.length + childrenPop.length); i++) {
			if (i < childrenPop.length) {
				fitnesslist[i] = childrenPop[i].getFitness();
			}
			else {
				fitnesslist[i] = parentPop[i - childrenPop.length].getFitness();
			}
		}

		// calculate length of roulettewheel list
		int sumchoices = 0;
		for (int i = 0; i < fitnesslist.length; i++) {
			sumchoices += i;
		}

		// create roulettewheel list
		double[] choicelist = new double[sumchoices];
		int index = 0;

		// fill roulettewheel list
		Arrays.sort(fitnesslist);
		for (int i = 0; i < fitnesslist.length; i++) {
			for (int j = 0; j < i; j++) {
				choicelist[index] = fitnesslist[i];
				index += 1;
			}
		}

		for (int i = 0; i < NUMBER_OF_INDIVIDUALS; i++) {
		
			int random_ind = ThreadLocalRandom.current().nextInt(0, choicelist.length);
			double searchedfitness = choicelist[random_ind];
			while (searchedfitness == 0.0) {
				random_ind = ThreadLocalRandom.current().nextInt(0, choicelist.length);
				searchedfitness = choicelist[random_ind];
			}
			for (int j = 0; j < NUMBER_OF_INDIVIDUALS + CHILDREN_PER_GENERATION; j++) {
				if (j < childrenPop.length) {
					if (childrenPop[j].getFitness() == searchedfitness) {
						newPop[i] = new Individual(childrenPop[j].getGenotype());
						newPop[i].setFitness(childrenPop[j].getFitness());
					}
				}
				else {
					if (parentPop[j - CHILDREN_PER_GENERATION].getFitness() == searchedfitness) {
						newPop[i] = new Individual(parentPop[j - CHILDREN_PER_GENERATION].getGenotype());
						newPop[i].setFitness(parentPop[j - CHILDREN_PER_GENERATION].getFitness());
					}
				}
			}
			
			for (int k = 0; k < choicelist.length; k++) {
				if (choicelist[k] == searchedfitness) {
					choicelist[k] = 0.0;
				}
			}
		}

		return newPop;
	}
}
