// Test voor class Individual --> Individual


import java.lang.Math; //TODO Kijken of dit nodig is
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Individual
//  Deze classe bevat een aantal zeer eenvoudige procedures. 
//  Individu wordt bepaald door genotype wat meegegeven wordt in de constructor
//  De method setFenotype() berekent uit het genome het fenotype (een rij getallen tussen -0.5 en +0.5)
//  De method getFitness() berekent nu de fitness (getal tussen 0 en 10) uit het Fenotype. 
//         Deze moet nog gekoppeld worden aan de procedure evaluate_.evaluate(child) uit player70.java. 

{
	private int genome;
	private double[] genotype; 
	private double fitness;
	private Random rand = new Random(); //TODO Is een aparte random number generator per individu okee of willen we een centrale gebruiken?
	private static final double epsilon = 1E-6; //TODO Machine precision epsilon, om sigma niet nul te laten worden. Eens met deze waarde?
	private double[] domainFunction = {-5,5};

	public Individual() // Overloaded constructor, dan kunnen we hem ook zonder argumenten aanroepen
	{
		double[] initGenotype = new double[20];
		this.setGenotype(initGenotype);

	}

	public Individual(double[] genotype)
	{	// constructor initializes individual genotype
		this.setGenotype(genotype);
	} 

	public double[] mapGenoToFeno(double[] genotype) //TODO Ik maak me bij dit soort dubbele naamgeving (de input heet hetzelfde als de private variabele hierboven) altijd een beetje zorgen, gaat dat hier goed?
	{
		double[] fenotype = new double[10]; //TODO Eigenlijk is het "phenotype" in het engels, niet "fenotype", zullen we toch maar de f aanhouden?
		for (int i = 0; i < 10; i++)
		{
			fenotype[i] = genotype[i + 10];
		}
		return fenotype;
	}

	public double[] getGenotype()
	{	
		return genotype;
	}

	public void setGenotype(double[] newGenotype)
	{
		genotype = newGenotype;
	}
	
	public double[] getFenotype()
	{
		return mapGenoToFeno(this.genotype); //TODO Hier weer de vraag of "this" nodig is.
	}

	
	public void mutGenotypeRandom(double[] oldGenotype)
	{	// muteert een willekeurige van de 20 genen, naar een willekeurige waarde tussen 0 en N(0,1) of -5 en 5
		double[] newGenotype = new double[20];		
		int randomGenIndex = rand.nextInt(19);
		double randomSigma = Math.abs(rand.nextGaussian());
		double randomX = -5+10*rand.nextDouble();
		if (randomGenIndex < 10)
		{	newGenotype[randomGenIndex] = randomSigma;}
		else
		{	newGenotype[randomGenIndex] = randomX;}
		// System.out.printf("GenIndex: %d - oldgene: %.1f - newgene: %.1f = ", randomGenIndex, oldGenotype[randomGenIndex], newGenotype[randomGenIndex]);	// TODO - dit is tijdelijk
		// System.out.println();
		this.setGenotype(newGenotype);
	}
			
	public void mutGenotype(double[] oldGenotype, double tau, boolean sigma_mut)
	{	
		double[] newGenotype = new double[20];
		if (sigma_mut)
		{   	// start procedure sigma-mut

			double candidateSigma;
			double factor;

			// Eerst de sigma's muteren (de eerste 10 elementen)
			for (int geneIndex=0;geneIndex<10;geneIndex++)
			{
				factor = Math.pow(Math.E,tau*rand.nextGaussian());
				candidateSigma = oldGenotype[geneIndex]*factor;			// Peter: dit is dus * ipv +
				// Als de nieuwe sigma kleiner is dan de machine precision epsilon, hem gelijk stellen hieraan
				if (candidateSigma < epsilon)
				{
					candidateSigma = epsilon;
				}
		 		// De aangepaste kandidaat voor sigma in het nieuwe genoom zetten
		 		newGenotype[geneIndex] = candidateSigma;

				// if (geneIndex==0){System.out.printf("oldsigma: %.1f - sigma: %.1f = ", oldGenotype[geneIndex], newGenotype[geneIndex]);} 	// TODO - dit is tijdelijk

		 	}   // end for

		 	// Nu de x-waardes zelf (de laatste 10 elementen)
		 	for (int geneIndex=10;geneIndex<20;geneIndex++)
		 	{
		 		double candidateX = oldGenotype[geneIndex]+newGenotype[geneIndex-10]*rand.nextGaussian();
			
		 		//Als de kandidaat X waarde uit het domein van de functie gaat, hem gelijk stellen aan de randwaarde.
		 		if (candidateX < domainFunction[0])
			 	{
			 		candidateX = domainFunction[0];
			 	}
				else if (candidateX >domainFunction[1])
		 		{
			 		candidateX = domainFunction[1];
		 		}
			
				// De aangepaste kandidaat voor X in het nieuwe genotype zetten
				newGenotype[geneIndex] = candidateX;
			}   // end for			
						
		}  //  End of procedure sigma-mut

		else
		{  	// start procedure willekeurige swap (sigma-mut = False)
		
			int first_gene_int = ThreadLocalRandom.current().nextInt(10, 20);
			int second_gene_int = ThreadLocalRandom.current().nextInt(10, 20);

			while (first_gene_int == second_gene_int) 
			{
				second_gene_int = ThreadLocalRandom.current().nextInt(10, 20);
			}

			for (int i = 0; i < 20; i++) 
			{
				if (i != first_gene_int && i != second_gene_int) 
				{
					newGenotype[i] = oldGenotype[i];
				}
				else if (i == first_gene_int) 
				{
					newGenotype[i] = oldGenotype[second_gene_int];
				}
				else 
				{
					newGenotype[i] = oldGenotype[first_gene_int];
				}
			}	// end for

		}	// End of procedure willekeurige mutatie (sigma-mut = False


		this.setGenotype(newGenotype);

	}  // end method mut_Genotype


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

	}
}
