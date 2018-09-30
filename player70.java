import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;

public class player70 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    	private int evaluations_limit_;
	private final int[] genomeRange = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};	

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
        
 	       	int evals = 0;
		int generation = 0;
		final int NUMBER_OF_INDIVIDUALS = 100; 				// size of population
		final Random randomNumbers = new Random();			// random number generator

        	// init population
		Individual[] currentPop = initPopulation(NUMBER_OF_INDIVIDUALS);
        	// calculate fitness - let op dit is maar dummy berekening
		detFitnessPop(currentPop);

        	while(evals<evaluations_limit_){

            		// Select parents		
			int num_individuals = currentPop.length; 
			Individual[] newParents = selectParents(currentPop);
			int num_children = NUMBER_OF_INDIVIDUALS - newParents.length;
			System.out.printf("nextPop...generation: %d, aantal pop: %d, aantal parents: %d, aantal children: %d", generation, NUMBER_OF_INDIVIDUALS, newParents.length, num_children);
			System.out.println(); 

            		// Apply crossover / mutation operators
			Individual[] newChildren = crossoverChildren( num_children, newParents );
			// System.out.printf("Crossover gelukt.., aantal children: %d/%d", num_children, newChildren.length); 

			// Test fitness van gecreerde children
			for (int i = 0; (i < newChildren.length && evals<evaluations_limit_); i++)
			{
				double child[] = newChildren[i].setFenotype();
            			// Check fitness of unknown fuction
            			Double fitness = (double) evaluation_.evaluate(child);
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
		int[] genomeRange = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		Individual[] population = new Individual[ num_individuals ];
		for (int count = 0; count < population.length; count++ )
			population[ count ] = new Individual( genomeRange[ count % 10 ] );
		return population; //initpop;
	}

	public static void detFitnessPop(Individual[] population)
	{
		for ( int count = 0; count < population.length; count++ )
		population[count].detFitness();
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
			// System.out.printf("selectParents...2 ouders of meer...numberParents: %d/%d", numberParents, parents.length); 
			// System.out.println();
			// TestIndividual[] parents = new TestIndividual[numberParents];
			int parentcount = 0;
			for (int count = 0; count < population.length; count++)
			{
				double indfitness = population[count].getFitness();
				if (indfitness > thresholdfitness)
				{
					// System.out.printf("selectParents...parentcount: %d, populationcount: %d", parentcount, count);
					// System.out.println();
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

	public static Individual[] crossoverChildren( int numChildren, Individual[] parents )
		// children worden gemaakt uit parents
	{
		// create empty child population with right number
		Individual[] newChildren = new Individual[numChildren];
		// System.out.printf("aantalchilds: %d.", numChildren);
		// System.out.println();
		
		// populate child-population with crossoverChildren
		int aantal_children = newChildren.length;
		int aantal_parents = parents.length;
		int next_parent1 = 0;
		int next_parent2 = 1;
		for ( int count = 0; count < aantal_children; count++ )
		{	
			if (next_parent1 == aantal_parents)
				next_parent1 = 0;
			if (next_parent2 == aantal_parents)
				next_parent2 = 0;					
			Individual parent1 = parents[next_parent1];
			Individual parent2 = parents[next_parent2];
			Individual newchild = createChild( parent1, parent2 );
			newChildren[ count ] = newchild;
			next_parent1 += 1;
			next_parent2 += 1;
		}
		return newChildren;
	}

	public static Individual[] createChildren( int numChildren )
		// children wordt nu willekeurig geiniteerd en 1 maal gemuteerd 
	{	
		final int[] genomeRange = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};  // deze eigenlijk nog globaal definieren
		// create emtpy child-population with right number
		Individual[] newChildren = new Individual[numChildren];
		System.out.printf("aantalchilds: %d.", numChildren);
		System.out.println();

		// populate child-population with Individual objects
		for ( int count = 0; count < newChildren.length; count++ )
			newChildren[ count ] = new Individual( genomeRange[ count % 10 ] );

		// mutation on populated children
		for (int count = 0; count < newChildren.length; count++)
		{
			System.out.println("child..voor mutatie...");
			newChildren[count].displayFenotype();
			newChildren[count].mutGenome();   
			System.out.println("child..na mutatie...");
			newChildren[count].displayFenotype();
		}
		return newChildren;
	}

	public static Individual createChild(Individual parent1, Individual parent2)
	{
		int child_genome = (parent1.getGenome() + parent2.getGenome())/2+2;
		System.out.printf("createChild...par1gen: %d, par2.gen: %d, child_genome: %d", parent1.getGenome(), parent2.getGenome(), child_genome);
		System.out.println();
		Individual child = new Individual(child_genome);
		return child;
	}

}
