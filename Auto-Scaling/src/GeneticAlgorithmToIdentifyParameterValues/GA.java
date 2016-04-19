package GeneticAlgorithmToIdentifyParameterValues;

import GeneticAlgorithmToIdentifyParameterValues.Algorithm;
import GeneticAlgorithmToIdentifyParameterValues.Population;

public class GA {

	public static void main(String[] args) {
		Population myPop = new Population(10, true);
//		Population myPop = new Population(30, false);
		int generationCount = 0;
		double[] fittestHistory = new double [6];
		for (int t=0; t< fittestHistory.length; t++)
		{
			fittestHistory[t] = 0;
		}
		int it = 0;
		boolean stopCondition = false;
		while (!stopCondition)
		{
			generationCount++;
			double fittest = myPop.getFittest().getFitness();
			fittestHistory[it] = fittest;
			it++;
			if (it == fittestHistory.length-1)
			{
				boolean find = false;
				for (int j=0; j< fittestHistory.length - 1; j++)
				{
					if (fittestHistory[it-1] >= fittestHistory[j])
					{
						find = true;
					}
				}
				if (find)
				{
					stopCondition = true;
				}
				else
				{
					for (int j=0; j<fittestHistory.length-1; j++)
					{
						fittestHistory[j] = fittestHistory[j+1];
					}
					fittestHistory[it] = 0;
					it--;
				}
			}
			System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
			myPop = Algorithm.evolvePopulation(myPop);
		}
		System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());
        System.out.println(myPop.getFittest().getFitness());
	}
	
	public static boolean StopCondition()
	{
		return true; 
	}

}
