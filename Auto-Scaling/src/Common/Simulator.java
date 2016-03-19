package Common;
import java.io.IOException;

import Application.Application;
import Application.SoftwareTier;
import ScalingUnit.AmazonDecisionMaker;
import ScalingUnit.ThresholdBasedDecisionMaker;

public class Simulator{
	public static int monitoringInterval = 5;
	
	public static void main(String[] args) throws IOException
	{		
		//create business tier
		double btServDem = 0.076;
		double btAccessR = 1.0;
		int btVmBT = 0;
		IaaS biaas = new IaaS(btVmBT, btServDem);
		SoftwareTier bt = new SoftwareTier("BusinessTier", btServDem, btAccessR, biaas, 0);

		//create database tier
		double dtServDem = 0.076;
		double dtAccessR = 0.7;
		int dtVmBt = 0;
		IaaS diaas = new IaaS(dtVmBt, dtServDem);
		SoftwareTier dt = new SoftwareTier("DatabaseTier", dtServDem, dtAccessR, diaas, 1);
		
		//create application
		Application app = new Application();
		app.AddTier(bt);
		app.AddTier(dt);
		
		//create monitor
		Monitor monitor = new Monitor();
		monitor.SetInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		
		//create decision maker
//		ScalingUnitInterface decisionMaker = new ThresholdBasedDecisionMaker(app);
		ScalingUnitInterface decisionMaker = new AmazonDecisionMaker(60.0,100.0,app);
		
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
		System.out.println("Operational Cost: "+Integer.toString(app.GetOperationalCost()));
		System.out.println("VM Thrashing: " + Integer.toString(app.GetVmThrashing()));
		System.out.println("SLA Violation Count: "+ Integer.toString(timer.GetSlaViolationCount()));
	}
}
