// Test voor class TestIndividual --> Individual

public class TestIndividual
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

	public void mutGenome()
	{	
		if ( genome < 5 )
			genome =+1;
		else
			genome = -5;
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
