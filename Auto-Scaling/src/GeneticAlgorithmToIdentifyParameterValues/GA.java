package GeneticAlgorithmToIdentifyParameterValues;

import GeneticAlgorithmToIdentifyParameterValues.Algorithm;
import GeneticAlgorithmToIdentifyParameterValues.Population;

public class GA {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Population myPop = new Population(35, true);
//		Population myPop = new Population(30, false);
		int generationCount = 0;
//		double[] fittestHistory = new double [2];
		int iterations = 7; 
//		for (int t=0; t< fittestHistory.length; t++)
//		{
//			fittestHistory[t] = 0;
//		}
		int it = 0;
		boolean stopCondition = false;
//		while (!stopCondition)
		while (generationCount < iterations)
		{
			generationCount++;
			System.out.println("Generation: " + generationCount);
			myPop.printIndividuals();
//			double fittest = myPop.getFittest().getFitness();
//			fittestHistory[it] = fittest;
			
//			if (it == fittestHistory.length-1)
//			{
//				boolean find = false;
//				for (int j=0; j< fittestHistory.length - 1; j++)
//				{
//					if (fittestHistory[it-1] >= fittestHistory[j])
//					{
//						find = true;
//					}
//				}
//				if (find)
//				{
//					stopCondition = true;
//				}
//				else
//				{
//					for (int j=0; j<fittestHistory.length-1; j++)
//					{
//						fittestHistory[j] = fittestHistory[j+1];
//					}
//					fittestHistory[it] = 0;
//					it--;
//				}
//			}
//			if (iterations  it)
//			{
//				stopCondition = true;
//			}
			it++;
			System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
			myPop = Algorithm.evolvePopulation(myPop);
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());
        System.out.println(myPop.getFittest().getFitness());
        System.out.println("---------------");
        System.out.println("duration: " +  totalTime);
	}
	
	public static boolean StopCondition()
	{
		return true; 
	}

}
