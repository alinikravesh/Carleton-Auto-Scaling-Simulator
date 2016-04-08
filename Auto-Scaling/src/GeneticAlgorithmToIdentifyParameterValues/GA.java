package GeneticAlgorithmToIdentifyParameterValues;

import GeneticAlgorithmToIdentifyParameterValues.Algorithm;
import GeneticAlgorithmToIdentifyParameterValues.Population;

public class GA {

	public static void main(String[] args) {
		Population myPop = new Population(30, true);
//		Population myPop = new Population(30, false);
		int generationCount = 0;
		
		while (!StopCondition())
		{
			generationCount++;
			System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
			myPop = Algorithm.evolvePopulation(myPop);
		}
		System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());
	}
	
	public static boolean StopCondition()
	{
		return true; 
	}

}
