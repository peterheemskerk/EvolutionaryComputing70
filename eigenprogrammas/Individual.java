// Test voor class Individual --> Individual

public class Individual
//  Deze classe bevat een aantal zeer eenvoudige procedures. 
//  Individu wordt bepaald door genome (een geheel getal tussen -5 en 5)
//  De method mutGenome() muteert het genome door er 1 bij op te tellen.
//  De method setFenotype() berekent uit het genome het fenotype (een rij getallen tussen -0.5 en +0.5)
//  De method getFitness() berekent nu de fitness (getal tussen 0 en 10) uit het Fenotype. 
//         Deze moet nog gekoppeld worden aan de procedure evaluate_.evaluate(child) uit player70.java. 

{
	private int genome;
	private double[] genotype = {0,0,0,0,0,0,0,0,0,0};
	private double fitness;

	public Individual( Integer indGenome )
	{	// constructor initializes individual genome - versie 1.1 nu wordt genotype gebruikt (gelijk aan fenotype), dat willekeurig wordt geinitieerd. (maar mutatie en cross over nog niet aangepast)
		genome = indGenome; // initialize
		for (int i=0; i<genotype.length; i++)
		{
			genotype[i]=10*(Math.random()-0.5);
		}
	} 

	public double[] setFenotype()
	{	// nieuw in versie 1.1 - fenotype = genotype
		double fenotype[] = genotype; 
		return fenotype;
	}

	public double[] setFenotype_genome()
	{	// deze procedure wordt niet meer gebruikt
		double fenotype[] = {-0.5,-0.4,-0.3,-0.2,-0.1,0.0,0.1,0.2,0.3,0.4};
		for(int i=0; i < fenotype.length; i++)
			fenotype[i]=fenotype[i]*genome;
		return fenotype;
	}

	public double[] getGenotype()
	{	
		return genotype;
	}

	public void setGenome()
	{	//   deze functie wordt niet meer gebruikt. 
		genome = 3;
	}

	public int getGenome()
	{
		return genome;
	}

	public void mutGenome()
	{	// deze functie niet meer gebruiken, nu gebruiken mutGenotype()
		if ( genome < 5 )
			genome =+1;
		else
			genome = -5;
	}

	public void mutGenotype()
	{	// versie 1.1 - gebeurt niets, deze functie aanpassen met mutatie van N(0, sigma)
	}

	public Individual createChild_old(Individual parent1, Individual parent2)
	{
		int child_genome = (parent1.getGenome() + parent2.getGenome())/2;
		Individual child = new Individual(child_genome);
		return child;
	}

	public void detFitness()
	{	
		fitness = 10;
		double fenotype[] = setFenotype();
		for (int i=0; i<fenotype.length; i++)
			fitness += 2*fenotype[i];
		// vervangen door:
	        // Double fitness = (double) evaluation_.evaluate(child);
	
	}

	public void setFitness( double newfitness )
	{	
		fitness = newfitness;
	}

	public double getFitness()
	{
		return fitness;
	}
	
	public void displayFenotype()	
	{
		System.out.printf("Genome: %d", genome);
		System.out.println();
		System.out.printf("Genotype: ");
		for (int i=0; i<genotype.length; i++)
			System.out.printf( "- %.1f ", genotype[ i ]);
		System.out.println();
		System.out.print("Fenotype ");
		double[] fenotype = setFenotype();
		for ( int counter = 0; counter < fenotype.length; counter++ )
			System.out.printf( "- %.1f ", fenotype[ counter ] ); 
		System.out.println();
		System.out.printf("Fitness: %.1f", fitness);
		System.out.println();
	}
}
