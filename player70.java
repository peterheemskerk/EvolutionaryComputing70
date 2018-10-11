import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Collections;
// import org.apache.commons.lang.ArrayUtils;


import java.util.Random;
import java.util.Properties;

public class player70 implements ContestSubmission
{
	public static Random rnd_;
	static ContestEvaluation evaluation_; 						//TODO Deze heb ik zelf static gemaakt om een error op te lossen; misschien is dat niet de way to go
    	private int evaluations_limit_;

	private static final int GENERATION_LIMIT = 100000;				// TODO - default = 10000
	private static final int NUMBER_OF_INDIVIDUALS = 100; 				// size of population
	private static final int CHILDREN_PER_GENERATION = 15;
	private static final double RANDOM_MUTATION_PROB = 0.01;

	private static final double tau = 1/Math.sqrt(10); 	// Learning rate, problem size (n) heb ik geinterpreteerd als dimensies van het fenotype (dat is de consensus ook op Slack op het moment)
	private static final double epsilon = 1E-6; // Machine precision

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
		final Random randomNumbers = new Random();			// random number generator

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

        	while(evals<evaluations_limit_ && generation < GENERATION_LIMIT){

            		// Select parents		
			int num_individuals = currentPop.length; 
			Individual[] newParents = selectParents(currentPop);
			int num_children = CHILDREN_PER_GENERATION;
			//DEBUG System.out.printf("nextPop...generation: %d, aantal pop: %d, aantal parents: %d, aantal children: %d", generation, NUMBER_OF_INDIVIDUALS, newParents.length, num_children);

            		// Apply crossover / mutation operators
			Individual[] newChildren = createChildren(num_children, newParents);

			// Test fitness van gecreerde children
			for (int i = 0; (i < newChildren.length && evals<evaluations_limit_); i++)
			{
    			// Check fitness of unknown fuction
    			Double fitness = (double) evaluation_.evaluate(newChildren[i].getFenotype());
				newChildren[i].setFitness(fitness);
    			evals++;
			}  
			Individual[] newPop = selectSurvivors(currentPop, newChildren);	
			currentPop = newPop;
			System.out.print("DIT WAS GENERATIE: ");
			System.out.println(generation);
			generation++;

		}	// endwhile
 		//DEBUG printPop(currentPop);

	}	// endrun()


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

	public static Individual[] createChildren( int numChildren, Individual[] parents )
		// children worden gemaakt uit parents
	{
		//   choose mutationtype: sigma-mut = True: mutation using Normal(Sigma) - False: random swap
		boolean sigma_mut = true;  // TODO parameter-tuning
		Random rand = new Random(); //TODO Is een aparte random number generator per individu okee of willen we een centrale gebruiken?

		// create empty child population with right number
		Individual[] newChildren = new Individual[numChildren];
		
		int parentcount = 0;		// TODO volgens mij werkt deze procedure alleen als aantal parents > aantal kinderen. 
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
		return newChildren;
	}

	public static double[] recombineGenotypes(double[] genotype1, double[] genotype2)
	{	// TODO deze wordt niet meer gebruik en kan dus weg
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
		double[] fitnesslist = new double[NUMBER_OF_INDIVIDUALS + CHILDREN_PER_GENERATION]; // TODO - hier stond 120
		//  int NUMBER_OF_INDIVIDUALS = 100; TODO - mag weg
		Individual[] newPop = new Individual[NUMBER_OF_INDIVIDUALS];

		for (int i = 0; i < (parentPop.length + childrenPop.length); i++) {
			if (i < childrenPop.length) {
				fitnesslist[i] = childrenPop[i].getFitness();
			}
			else {
				fitnesslist[i] = parentPop[i - childrenPop.length].getFitness();
			}
		}

		Arrays.sort(fitnesslist);

		for (int i = 0; i < NUMBER_OF_INDIVIDUALS; i++) {					// TODO - hier stond 100
			double searchedfitness = fitnesslist[i + CHILDREN_PER_GENERATION];   		// TODO - hier stond 20
			for (int j = 0; j < NUMBER_OF_INDIVIDUALS + CHILDREN_PER_GENERATION; j++) {   	// TODO - hierstond 120
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
		}

		return newPop;
	}
}
