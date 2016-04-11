package GeneticAlgorithmToIdentifyParameterValues;

import java.util.Random;

public class Individual {

    // individual format: <0.thrU, 1.thrL, 2.durU, 3.durL, 4.inU, 5.inL>
	// individual constraints: 
	//		1. LL   <=  thrL
	//		2. thrL <   thrU	
	//		3. thrU <=  UL
	//		4. 0    <=  durU
	//		5. 0    <=  durL
	//		6. 0    <=  inU
	//		7. 0    <=  inL
	
	// Number of genes in individuals
	static int defaultGeneLength = 6;
	
	// the upper limit of the threshold value
	static int ULT = 100; 
	
	// the lower limit of the threshold value
	static int LLT = 1;
	
	// the upper limit of scaling duration
	static int Udur = 5;
	
	// the lower limit of scaling duration
	static int Ldur = 0;
	
	// the upper limit of freezing period
	static int Uin = 5;

	// the lower limit of freezing period
	static int Lin = 0;
    private int[] genes = new int[defaultGeneLength];
    private double fitness = 0;

    public int GenerateValue(int min, int max)
    {
    	int value = 0;
    	Random r = new Random();
    	value = r.nextInt(max-min) + min; 
    	return value;
    }
    
    // Create a random individual
    public void generateIndividual() {
    	genes[0] = GenerateValue(LLT+1, ULT);  
    	genes[1] = GenerateValue(LLT, genes[0]);
    	genes[2] = GenerateValue(Ldur, Udur);
    	genes[3] = GenerateValue(Ldur, Udur);
    	genes[4] = GenerateValue(Lin, Uin);
    	genes[5] = GenerateValue(Lin, Uin);
    	fitness = getFitness();
    }

    /* Getters and setters */
    public int getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int value) {
        genes[index] = value;
        fitness = 0;
    }
    
    public void setFitness()
    {
    	fitness = getFitness();
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

    public double getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }

	public boolean IsFeasible() {
		boolean feasible = true;
		if ((genes[1] < LLT) || (genes[1] >= genes[0]) || (genes[0] > ULT) || 
				(genes[2] < 0) || (genes[2] > Udur) || (genes[3] < 0) || (genes[3] > Udur) || 
				(genes[4] < 0) || (genes[4] > Uin) || (genes[5] < 0) || (genes[5] > Uin))
		{
			feasible = false;
		}
		return feasible;
	}

	public int mutate(int i) {
		int newVlue = 0;
		switch (i) {
			case 0: newVlue = GenerateValue(LLT, ULT);
					break;
			case 1: newVlue = GenerateValue(genes[1], ULT);
					break;
			case 2: newVlue = GenerateValue(Ldur, Udur);
					break;
			case 3: newVlue = GenerateValue(Ldur, Udur);
					break;
			case 4: newVlue = GenerateValue(Lin, Uin);
					break;
			case 5: newVlue = GenerateValue(Lin, Uin);
			default: break;
		}
		return newVlue;
	}
}