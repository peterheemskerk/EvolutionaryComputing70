// Test voor class TestIndividual --> Individual

public class TestIndividual
//  Deze classe bevat een aantal zeer eenvoudige procedures. 
//  Individu wordt bepaald door genome (een geheel getal tussen -5 en 5)
//  De method mutGenome() muteert het genome door er 1 bij op te tellen.
//  De method setFenotype() berekent uit het genome het fenotype (een rij getallen tussen -0.5 en +0.5)
//  De method getFitness() berekent nu de fitness (getal tussen 0 en 10) uit het Fenotype. 
//         Deze moet nog gekoppeld worden aan de procedure evaluate_.evaluate(child) uit player70.java. 

{
	private int genome;
	private double fitness;

	public TestIndividual( Integer indGenome )
	{	// constructor initializes individual genome
		genome = indGenome; // initialize
	} 

	public double[] setFenotype()
	{
		double fenotype[] = {-0.5,-0.4,-0.3,-0.2,-0.1,0.0,0.1,0.2,0.3,0.4};
		for(int i=0; i < fenotype.length; i++)
			fenotype[i]=fenotype[i]*genome;
		return fenotype;
	}

	public void setGenome()
	{
		genome = 3;
	}

	public int getGenome()
	{
		return genome;
	}

	public void mutGenome()
	{	
		if ( genome < 5 )
			genome =+1;
		else
			genome = -5;
	}

	public TestIndividual createChild_old(TestIndividual parent1, TestIndividual parent2)
	{
		int child_genome = (parent1.getGenome() + parent2.getGenome())/2;
		TestIndividual child = new TestIndividual(child_genome);
		return child;
	}

	public void detFitness()
	{	
		fitness = 10;
		double fenotype[] = setFenotype();
		for (int i=0; i<fenotype.length; i++)
			fitness += 2*fenotype[i];
	}

	public void setFitness( double newfitness )
	{	
		fitness = newfitness;
	}

	public double getFitness()
	{
		return fitness;
	}

	public Integer aantalTekens( String tekst)
	{
		String myString = new String(tekst);
		int aantal;
		aantal = myString.length();
		return aantal;
	}
	
	public void displayFenotype()	
	{
		System.out.printf("Genome: %d", genome);
		System.out.println();
		System.out.print("Fenotype ");
		double[] fenotype = setFenotype();
		for ( int counter = 0; counter < fenotype.length; counter++ )
			// System.out.printf( "%d: %f\n", counter, fenotype[ counter ] ); 
			System.out.printf( "- %.1f ", fenotype[ counter ] ); 
		System.out.println();
		System.out.printf("Fitness: %.1f", fitness);
		System.out.println();
	}
}
