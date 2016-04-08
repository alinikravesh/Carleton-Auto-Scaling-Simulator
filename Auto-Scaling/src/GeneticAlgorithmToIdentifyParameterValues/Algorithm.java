package GeneticAlgorithmToIdentifyParameterValues;

import java.util.Arrays;

public class Algorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i+=2) {
            Offspring parents = select(pop); 
//        	Individual indiv1 = tournamentSelection(pop);
//            Individual indiv2 = tournamentSelection(pop);
            Offspring newOffsprings = crossover(parents.offspring1, parents.offspring2);
            newPopulation.saveIndividual(i, newOffsprings.offspring1);
            newPopulation.saveIndividual(i+1, newOffsprings.offspring2);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }
    
    private static Offspring select(Population pop)
    {
    	Offspring selectedParents = new Offspring();
    	Individual parent1 = pop.getFittest(); 
    	pop.remove(parent1); 
    	Individual parent2 = pop.getFittest();
    	pop.remove(parent2);
    	selectedParents.offspring1=parent1;
    	selectedParents.offspring2 = parent2;
    	return selectedParents;
    }

    // Crossover individuals
    private static Offspring crossover(Individual indiv1, Individual indiv2) {
        Offspring newOffspring = new Offspring();
        boolean firstFine = false;
        boolean secondFine = false;
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                 newOffspring.offspring1.setGene(i, indiv1.getGene(i));
                 newOffspring.offspring2.setGene(i, indiv2.getGene(i));
            } else {
            	newOffspring.offspring1.setGene(i, indiv2.getGene(i));
            	newOffspring.offspring2.setGene(i, indiv1.getGene(i));
            }
        }
        if (newOffspring.offspring1.IsFeasible())
        {
        	newOffspring.offspring1.setFitness();
        	firstFine = true;
        }
        if (newOffspring.offspring2.IsFeasible())
        {
        	newOffspring.offspring2.setFitness();
        	secondFine = true;
        }      
        if (firstFine && !secondFine)
        {
        	newOffspring.offspring2 = indiv1;
        }
        else if(!firstFine && secondFine)
        {
        	newOffspring.offspring1 = indiv1;
        }
        else if(!firstFine && !secondFine)
        {
        	newOffspring.offspring1 = indiv1;
        	newOffspring.offspring2 = indiv2;
        }        
        return newOffspring;
    }

    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        boolean changed = false;
    	for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                int newGeneValue = indiv.mutate(i);
                int oldGeneValue = indiv.getGene(i); 
                indiv.setGene(i, newGeneValue);
                if (!indiv.IsFeasible())
                {
                	indiv.setGene(i, oldGeneValue);
                }
                else
                {
                	changed = true; 
                }
            }
        }
    	if (changed)
    	{
    		indiv.setFitness();
    	}
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
}

