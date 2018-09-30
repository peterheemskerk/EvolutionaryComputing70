// eerste java programmaatje

import java.util.Scanner;
import java.util.Random;


// public class pl70genetics
// {
	// Procedure stelt een aantal keer een nieuwe populatie samen. 
	// Een nieuwe nieuwe populatie wordt samengesteld door een aantal parents te selecteren in de method: selectParents()
	// Daarna wordt een aantal kinderen gecreerd door mutatie van een aantal van deze ouders in de method: createChildren() 
	// Er vindt nu nog geen sex plaats bij reproductie. 
	// Een populatie is een array van individuals TestIndividual[], de onderliggende class heet TestIndividual

	// Een mooie taak zou zijn om deze procedure te koppelen aan de player70.java procedure
	// Waarbij ook de fitness bepaald " officieel"  bepaald wordt (BentSigar etc.)

	// Een tweede mooie taak zou zijn om een 2 ouder productie van een kind te maken. 

	/*

	private static final int NUMBER_OF_POPULATIONS = 3; 				// aantal generaties
	private static final int NUMBER_OF_INDIVIDUALS = 8; 				// size of population
	private int currentIndividual; 							// index of next Individual
	private static final Random randomNumbers = new Random();			// random number generator

	public static void main( String[] args )	
	{
		System.out.println("initiate first population...");
		TestIndividual[] startPopulation = initPopulation();
		detFitnessPop(startPopulation);
		printpop(startPopulation);
		TestIndividual[] currentPopulation = startPopulation;

		for (int currentPop = 1; currentPop < NUMBER_OF_POPULATIONS; currentPop++)
		{
			System.out.printf("next population...%d", currentPop);
			System.out.println();
			TestIndividual[] newpopulation = nextPopulation(currentPopulation);		// creert next population	
			System.out.println("...einde nextPopulation");
			detFitnessPop(newpopulation);
			// System.out.println("...einde detFitnesPop");
			printpop(newpopulation);
			currentPopulation = newpopulation;
		}
	} // end of main

	*/

	// hieronder staan allerlei procedures....

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

	/* 

	public static Individual[] nextPopulation(Individual[] population)
	// next population bestaat uit geselecteerde ouders (select Parents) en aangevuld met kinderen (new Children)
	{
		Individual[] newPopulation = new TestIndividual[NUMBER_OF_INDIVIDUALS];
		Individual[] parents = selectParents(population);
		int numChildren = newPopulation.length - parents.length;
		System.out.printf("nextPop...aantal pop: %d/%d, aantal parents: %d, aantal children: %d", NUMBER_OF_INDIVIDUALS, newPopulation.length, parents.length, numChildren);
		System.out.println(); 
		// TestIndividual[] children = createChildren( numChildren );	// ongeslachtelijk
		Individual[] children = crossoverChildren( numChildren, parents );
		System.out.printf("Crossover gelukt.., aantal children: %d", children.length); 
		for (int count = 0; count < newPopulation.length; count++)
		{
			if (count < parents.length)
				newPopulation[count] = parents[count];
			else
				newPopulation[count] = children[count - parents.length];
		}
		return newPopulation;
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
		Individual[] newChildren = new TestIndividual[numChildren];
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
			TestIndividual parent1 = parents[next_parent1];
			TestIndividual parent2 = parents[next_parent2];
			TestIndividual newchild = createChild( parent1, parent2 );
			newChildren[ count ] = newchild;
			next_parent1 += 1;
			next_parent2 += 1;
		}
		return newChildren;
	}

	public static Individual[] createChildren( int numChildren )
		// children wordt nu willekeurig geiniteerd en 1 maal gemuteerd 
	{	
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
		TestIndividual child = new TestIndividual(child_genome);
		return child;
	}

		// print population
	public static void printpop(Individual[] population)
	{
		System.out.println("printpop....:");
		for ( int count = 0; count < population.length; count++)
		{
			System.out.printf("Individu %d: ", count);
			population[count].displayFenotype();
		}
	}

} // end class TestPopulation
	*/
