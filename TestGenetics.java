// eerste java programmaatje

import java.util.Scanner;
import java.util.Random;

public class TestGenetics
{
	private static final int NUMBER_OF_POPULATIONS = 3; 				// aantal generaties
	private static final int NUMBER_OF_INDIVIDUALS = 3; 				// size of population
	private int currentIndividual; 							// index of next Individual
	private static final Random randomNumbers = new Random();			// random number generator
	private static final int[] genomeRange = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

	public static void main( String[] args )	
	{
		System.out.println("initiate first population...");
		TestIndividual[] startPopulation = initPopulation();
		detFitnessPop(startPopulation);
		printpop(startPopulation);
		TestIndividual[] currentPopulation = startPopulation;

		for (int currentPop = 0; currentPop < NUMBER_OF_POPULATIONS; currentPop++)
		{
			System.out.printf("next population...%d", currentPop);
			System.out.println();
			TestIndividual[] newpopulation = nextPopulation(currentPopulation);		// creert next population	
			System.out.println("...einde nextPopulation");
			detFitnessPop(newpopulation);
			System.out.println("...einde detFitnesPop");
			printpop(newpopulation);
			currentPopulation = newpopulation;
		}
	} // end of main


	// hieronder staan allerlei procedures....

	public static TestIndividual[] initPopulation()
	{
		TestIndividual[] population = new TestIndividual[ NUMBER_OF_INDIVIDUALS ];
		for (int count = 0; count < population.length; count++ )
			population[ count ] = new TestIndividual( genomeRange[ count % 10 ] );
		// TestIndividual[] initpop = population;
		return population; //initpop;
	}

	public static void detFitnessPop(TestIndividual[] population)
	{
		for ( int count = 0; count < population.length; count++ )
		population[count].detFitness();
	}

	public static TestIndividual[] nextPopulation(TestIndividual[] population)
	// next population bestaat uit geselecteerde ouders (select Parents) en aangevuld met kinderen (new Children)
	{
		TestIndividual[] newPopulation = new TestIndividual[NUMBER_OF_INDIVIDUALS];
		TestIndividual[] parents = selectParents(population);
		int numChildren = newPopulation.length - parents.length;
		TestIndividual[] children = createChildren( numChildren );
		for (int count = 0; count < newPopulation.length; count++)
		{
			if (count < parents.length)
				newPopulation[count] = parents[count];
			else
				newPopulation[count] = children[count - parents.length];
		}
		return newPopulation;
	}

	public static TestIndividual[] selectParents( TestIndividual[] population )
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
		System.out.printf("Nextpop...Threshold: %.1f, aantalparents: %d ", thresholdfitness, numberParents);
		TestIndividual[] parents = new TestIndividual[numberParents];
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
		return parents;
	} 

	public static TestIndividual[] createChildren( int numChildren )
		// children wordt nu willekeurig geiniteerd en 1 maal gemuteerd 
	{	
		TestIndividual[] newChildren = new TestIndividual[numChildren];
		System.out.printf("aantalchilds: %d.", numChildren);
		System.out.println();

		// populate population with Individual objects
		for ( int count = 0; count < newChildren.length; count++ )
			newChildren[ count ] = new TestIndividual( genomeRange[ count % 10 ] );

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

		// print population
	public static void printpop(TestIndividual[] population)
	{
		System.out.println("Testpopulation:");
		for ( int count = 0; count < population.length; count++)
		{
			System.out.printf("Individu %d: ", count);
			population[count].displayFenotype();
		}
	}

} // end class TestPopulation
	
