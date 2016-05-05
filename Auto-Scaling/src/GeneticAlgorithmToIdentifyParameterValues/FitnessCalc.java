package GeneticAlgorithmToIdentifyParameterValues;

import java.io.IOException;
import java.util.*;

import Common.Simulator;
public class FitnessCalc {

    static byte[] solution = new byte[64];

    /* Public methods */
    // Set a candidate solution as a byte array
    public static void setSolution(byte[] newSolution) {
        solution = newSolution;
    }

    // To make it easier we can use this method to set our candidate solution 
    // with string of 0s and 1s
    static void setSolution(String newSolution) {
        solution = new byte[newSolution.length()];
        // Loop through each character of our string and save it in our byte 
        // array
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if (character.contains("0") || character.contains("1")) {
                solution[i] = Byte.parseByte(character);
            } else {
                solution[i] = 0;
            }
        }
    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    // ALI
    // I need to write my own fitness function
    static double getFitness(Individual individual) {
        double fitness = 0;
        // Loop through our individuals genes and compare them to our cadidates
//        for (int i = 0; i < individual.size() && i < solution.length; i++) {
//            if (individual.getGene(i) == solution[i]) {
//                fitness++;
//            }
//        }
//        Random r = new Random();
//        fitness = r.nextDouble()*100;
        int thrU = individual.getGene(0);
        int thrL = individual.getGene(1);
        int durU = individual.getGene(2);
        int durL = individual.getGene(3);
        int inU = individual.getGene(4);
        int inL = individual.getGene(5);
        Simulator sm = new Simulator(thrU, thrL, durU, durL, inU, inL);
        sm.SetVmCost(1);
        sm.SetSlaCost(1);
        try{
//        	fitness = sm.run();
        }
        catch(Exception e)
        {
        	fitness = 0;
        }
        return fitness;
    }
    
    // Get optimum fitness
    static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }
}

