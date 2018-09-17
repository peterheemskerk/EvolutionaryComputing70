import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class player70hillclimb implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player70hillclimb()
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
        
        // count evaluations
        int evals = 0;

        // set domain for dimensions
        double min = -5.0;
        double max = 5.0;
        
        // initialize child with 0-values
        double random_child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        double random = min + Math.random() * (max - min);
        
        // give child random values
        for (int i = 0; i < 10; i++) {
        	random = min + Math.random() * (max - min);
        	random_child[i] = random;
        }

        System.out.print("\n\nOriginal random child: {");
        for (int i = 0; i < 10; i++) {
        	System.out.print(random_child[i] + ", ");
        }
        System.out.print("}\n\n");

        // determine original fitness of random child
        Double child_fitness = (double) evaluation_.evaluate(random_child);
        System.out.print("Original fitness: " + child_fitness + "\n\n");

	    // perform hillclimber 5000 times
	    while(evals < 5000){
	        
	    	// chose spots to switch in child
	        int first_random_int = ThreadLocalRandom.current().nextInt(0, 10);
	    	int second_random_int = ThreadLocalRandom.current().nextInt(0, 10);

	    	// save values of these spots
	    	double first_value = random_child[first_random_int];
	    	double second_value = random_child[second_random_int];

	    	// switch values of spots
	    	random_child[first_random_int] = second_value;
	    	random_child[second_random_int] = first_value;

	    	// calculate new fitness 
	    	Double new_fitness = (double) evaluation_.evaluate(random_child);
	    	// System.out.print("New fitness, after switch: " + new_fitness + "\n");

	    	// check if switch improved fitness 
	    	if (new_fitness < child_fitness) {

	    		// if not, switch back
	    		random_child[first_random_int] = first_value;
	    		random_child[second_random_int] = second_value;
	    	}
	    	else {

	    		// if it is, update current best fitness
	    		child_fitness = new_fitness;
	    	}

	    	// new_fitness = (double) evaluation_.evaluate(random_child);
	    	// System.out.print("Switched back, or not: " + child_fitness + "\n\n");
	    	
	    	evals++;
	    }

	    System.out.print("Final child after hillclimber: {");
        for (int i = 0; i < 10; i++) {
        	System.out.print(random_child[i] + ", ");
        }
        System.out.print("}\n\n");


        // init population
        // calculate fitness
        // while(evals<evaluations_limit_){

        	
        //     // Select parents
        //     // Apply crossover / mutation operators
        //     // double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        //     // Check fitness of unknown fuction
        //     Double fitness = (double) evaluation_.evaluate(random_child);
        //     evals++;
        //     // Select survivors
        // }

	}
}
