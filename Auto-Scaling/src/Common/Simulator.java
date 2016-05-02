package Common;
import java.io.IOException;

import javax.xml.datatype.Duration;

import Application.Application;
import Application.SoftwareTier;
import ScalingUnit.AmazonDecisionMaker;
import ScalingUnit.ThresholdBasedDecisionMaker;
import ScalingUnit.ThresholdBasedFullHour;

public class Simulator{
	public static int monitoringInterval = 5;
	public double btServDem;
	public double btAccessR;
	public int btVmBT;
	public double dtServDem;
	public double dtAccessR;
	public int dtVmBt;
	public String inputFile;
	
	private static double vmCost = 1.0;
	private static double slaViolationCost = 5.0;
	private ScalingUnitInterface decisionMaker; 
	private Application app = new Application();
	private int pthrU;
	private int pthrL;
	private int pdurU;
	private int pdurL;
	private int pinU;
	private int pinL;
	public void SetVmCost(double cost)
	{
		vmCost = cost;
	}
	
	public void SetSlaCost(double cost)
	{
		slaViolationCost = cost;
	}
	
	public Simulator(int thrU, int thrL, int durU, int durL, int inU, int inL)
	{
		pthrU = thrU;
		pthrL = thrL;
		pdurU = durU;
		pdurL = durL;
		pinU = inU;
		pinL = inL;
		double btServDem = 0.076;
		double btAccessR = 1.0;
		int btVmBT = 0;
		double thrUd = (double) thrU/100;
		double thrLd = (double) thrL/100;
		IaaS biaas = new IaaS(btVmBT, btServDem, thrUd, thrLd);
		SoftwareTier bt = new SoftwareTier("BusinessTier", btServDem, btAccessR, biaas, 0);

		//create database tier
		double dtServDem = 0.076;
		double dtAccessR = 0.7;
		int dtVmBt = 0;
		IaaS diaas = new IaaS(dtVmBt, dtServDem, thrUd, thrLd);
		SoftwareTier dt = new SoftwareTier("DatabaseTier", dtServDem, dtAccessR, diaas, 1);
		
		//create application
		app.AddTier(bt);
		app.AddTier(dt);
		decisionMaker = new ThresholdBasedFullHour(app, thrU, thrL, durU, durL, inU, inL);
	}
	
//	public static void main(String[] args) throws IOException
	public double run() throws IOException
	{		
		//create business tier

		
		//create monitor
		Monitor monitor = new Monitor();
		monitor.SetInputFile("/Users/Ali/Dropbox/MyPersonalFolder/University/Simulation/cyclicWorkload_Experiment.xls");
//		monitor.SetInputFile("/Users/Ali/Dropbox/MyPersonalFolder/University/Simulation/growingWorkload_Experiment.xls");
//		monitor.SetInputFile("/Users/Ali/Dropbox/MyPersonalFolder/University/Simulation/unpredictableWorkload_Experiment.xls");

//		monitor.SetInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\cyclicWorkload_Experiment.xls");
//		monitor.SetInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\growingWorkload_Experiment.xls");
//		monitor.SetInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\UnpredictableWorkload_Experiment.xls");
		
		//create decision maker
//		ScalingUnitInterface decisionMaker = new ThresholdBasedFullHour(app);
//		ScalingUnitInterface decisionMaker = new ThresholdBasedDecisionMaker(app);
//		ScalingUnitInterface decisionMaker = new AmazonDecisionMaker(60.0,100.0,app);
		
		//create timer and start the simulation
		Timer timer = new Timer(monitoringInterval, monitor, decisionMaker, app);
		int time = 0;
		while (time < monitor.GetExperimentDuration())
		{
			timer.tick();
			time++;
		}
		app.EndExperiment(monitor.GetExperimentDuration()*monitoringInterval);
		
		//print simulation reports
//		System.out.println("Operational Cost: "+Integer.toString(app.GetOperationalCost()));
//		app.PirntVmHour(monitor.GetExperimentDuration()*monitoringInterval);
		
//		System.out.println("VM Thrashing: " + Integer.toString(app.GetVmThrashing()));
//		System.out.println("SLA Violation Count: "+ Integer.toString(timer.GetSlaViolationCount()));
//		System.out.println("Excessive Operational Cost: "+ Integer.toString(timer.GetExcessiveOperationalCost()));
		double cost = (timer.GetSlaViolationCount() * 30) + (app.GetOperationalCost() * 1) ;
		System.out.println("Individual: <"+pthrU+", "+pthrL+", "+pdurU+", "+pdurL+", "+pinU+", "+pinL+">");
		System.out.println("Cost: "+ cost + "|| operational cost:"+ (app.GetOperationalCost() * 1) + ", SLA violation:" + (timer.GetSlaViolationCount() * 30));
		System.out.println("------------------");
		return cost;
	}
}
