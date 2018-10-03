import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class player70 implements ContestSubmission
{
	public static Random rnd_;
	static ContestEvaluation evaluation_; //TODO Deze heb ik zelf static gemaakt om een error op te lossen; misschien is dat niet de way to go
    	private int evaluations_limit_;
	private static final double tau = 1/Math.sqrt(10); // Learning rate, problem size (n) heb ik geinterpreteerd als dimensies van het fenotype (dat is de consensus ook op Slack op het moment)
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
		final int NUMBER_OF_INDIVIDUALS = 20; 				// size of population
		final Random randomNumbers = new Random();			// random number generator

        	// init population
		Individual[] currentPop = initPopulation(NUMBER_OF_INDIVIDUALS);
        	// calculate fitness - v1.1 fitness initiele populatie wordt nu berekend door externe evaluatie. 
		for (int i = 0; (i < currentPop.length); i++)
		{
            		// Check fitness of unknown fuction
            		Double fitness = (double) evaluation_.evaluate(currentPop[i].getFenotype());
			currentPop[i].setFitness(fitness);
            		evals++;
		}		

        	while(evals<evaluations_limit_){

            		// Select parents		
			int num_individuals = currentPop.length; 
			Individual[] newParents = selectParents(currentPop);
			int num_children = NUMBER_OF_INDIVIDUALS - newParents.length;
			System.out.printf("nextPop...generation: %d, aantal pop: %d, aantal parents: %d, aantal children: %d", generation, NUMBER_OF_INDIVIDUALS, newParents.length, num_children);
			System.out.println(); 

            		// Apply crossover / mutation operators
			Individual[] newChildren = createChildren( num_children, newParents );

			// Test fitness van gecreerde children
			for (int i = 0; (i < newChildren.length && evals<evaluations_limit_); i++)
			{
            			// Check fitness of unknown fuction
            			Double fitness = (double) evaluation_.evaluate(newChildren[i].getFenotype());
				newChildren[i].setFitness(fitness);
            			evals++;
			}  

            		// Select survivors - no selection (only parent selection)
			Individual[] newPop = new Individual[NUMBER_OF_INDIVIDUALS];
			for (int count = 0; count < newPop.length; count++)
			{
				if (count < newParents.length)
					newPop[count] = newParents[count];
				else
					newPop[count] = newChildren[count - newParents.length];
			} 	
			currentPop = newPop;
			generation++;

		}	// endwhile
 		printPop(currentPop);

	}	// endrun()


	// below group70 - procedures

	public static Individual[] initPopulation(int num_individuals)
	{
		Individual[] population = new Individual[ num_individuals ];
		for (int count = 0; count < population.length; count++ )
		{
			double[] randomGenotype = new double[20];
			for (int i=0;i<10;i++) // First fill in the sigmas of the random genotype
			{
				randomGenotype[i] = rnd_.nextDouble(); // Get a double between 0.0 and 1.0 from the random number generator
				if (randomGenotype[i]<epsilon)
				{
					randomGenotype[i]=epsilon; // If sigma is smaller than machine precision, make it machine precision
				}
				
			}

			for (int i=10;i<20;i++) // Now fill in the "x-es" of the random genotype
			{
				randomGenotype[i] = rnd_.nextDouble()-0.5; // Get a double between -0.5 and 0.5 from the random number generator
			}
			population[ count ] = new Individual(randomGenotype);
		}

		return population; //initpop;
	}

	public static void detFitnessPop(Individual[] population)
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
			System.out.printf("Individu %d: ", count);
			population[count].displayFenotype();
		}
	}

	public static Individual[] selectParents( Individual[] population )
	{	// select parents with fitness larger than average fitness
		double minfitness = population[0].getFitness();
		double maxfitness = population[0].getFitness();
		for (int count = 0; count < population.length; count++ )
		{
			double indfitness = population[count].getFitness(); 
			if (indfitness > maxfitness)
				maxfitness = indfitness;
			if (indfitness < minfitness)
				minfitness = indfitness;
		}
		double thresholdfitness = (minfitness + maxfitness)/2;
		int numberParents = 0;
		for (int count = 0;count < population.length;count++ )
		{
			double indfitness = population[count].getFitness();
			if (indfitness > thresholdfitness)
				numberParents++;
		}
		// System.out.printf("selectParents...Threshold: %.1f, aantalparents: %d ", thresholdfitness, numberParents);
		// System.out.println();
		int aantalOuders = Math.max(2, numberParents);
		Individual[] parents = new Individual[aantalOuders];
		//  als aantal parents boven threshold = min 2, dan select die parents uit populatie
		if (numberParents > 1)
		{
			// TestIndividual[] parents = new TestIndividual[numberParents];
			int parentcount = 0;
			for (int count = 0; count < population.length; count++)
			{
				double indfitness = population[count].getFitness();
				if (indfitness > thresholdfitness)
				{
					parents[parentcount] = population[count];
					parentcount++;
				}
			}
		}
		// als slechts 0 of 1 ouder boven threshold, pak dan gewoon de eerste twee uit populatie
		else
		{
			// TestIndividual[] parents = new TestIndividual[2];
			parents[0] = population[0];
			parents[1] = population[1];
			System.out.printf("selectParents...exceptie...aantalparents: %d", parents.length);
			System.out.println();
		}
		return parents;
	} 

	public static Individual[] createChildren( int numChildren, Individual[] parents )
		// children worden gemaakt uit parents
	{
		// create empty child population with right number
		Individual[] newChildren = new Individual[numChildren];
		
		// populate child-population with createChildren
		int aantal_parents = parents.length;
		int next_parent1 = 0;
		int next_parent2 = 1;
		for ( int count = 0; count < numChildren; count++ )
		{	
			if (next_parent1 == aantal_parents)
				next_parent1 = 0;
			if (next_parent2 == aantal_parents)
				next_parent2 = 0;					
			Individual parent1 = parents[next_parent1];
			Individual parent2 = parents[next_parent2];

			// Create a child with recombination of the two parent genomes
			Individual newChild = createChild( parent1, parent2 );

			// Perform the mutation on the child after recombination
			newChild.setGenotype(newChild.mutGenotype(newChild.getGenotype(),tau));	

			newChildren[ count ] = newChild;
			next_parent1 += 1;
			next_parent2 += 1;
		}
		return newChildren;
	}

	public static Individual createChild(Individual parent1, Individual parent2)
	{
		double[] childGenotype = recombineGenotypes(parent1.getGenotype(),parent2.getGenotype());
		// System.out.printf("createChild...par1gen: %d, par2.gen: %d, childGenotype: %d \n", parent1.getGenotype(), parent2.getGenotype(), childGenotype);
		Individual child = new Individual(childGenotype);
		return child;
	}

	public static double[] recombineGenotypes(double[] genotype1, double[] genotype2)
	{
	
		double[] childGenotype = new double[20];
		for (int i=0;i<genotype1.length;i++)
		{
			if (rnd_.nextBoolean()) // Pak een random true of false, aan de hand daarvan het ene of andere genoom samplen. Wordt nu geen onderscheid tussen sigma en x gemaakt
			{
				childGenotype[i]=genotype1[i];	
			}else
			{
				childGenotype[i]=genotype2[i];
			}
		}

		return childGenotype;

	}

}
