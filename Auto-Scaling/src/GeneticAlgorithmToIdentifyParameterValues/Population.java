package GeneticAlgorithmToIdentifyParameterValues;

import java.util.ArrayList;
import java.util.List;

public class Population {

//    Individual[] individuals;
    List<Individual> individuals;   
    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new ArrayList<Individual>();
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual getFittest() {
        Individual fittest = individuals.get(0);
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() > getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.size();
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals.add(index, indiv);
    }
    
    public int indexOf(Individual indiv)
    {
    	int i = 0;
    	i = individuals.indexOf(indiv);
    	return i;
    }
    
    public void remove(Individual indiv)
    {
    	individuals.remove(indiv);
    }

	public void printIndividuals() {
		
		for(Individual ind : individuals)
		{
			System.out.println("< " + ind.getGene(0) + ", " + ind.getGene(1) + ", " + ind.getGene(2) + ", " + ind.getGene(3) + ", " + ind.getGene(4) + ", " + ind.getGene(5)+ ">");
		}
		
	}

}

