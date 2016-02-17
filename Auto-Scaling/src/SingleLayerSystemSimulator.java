import java.io.IOException;

import Application.Application;

public class SingleLayerSystemSimulator{
	public static void main(String[] args) throws IOException
	{		
		double btServDem = 0.076;
		double dtServDem = 0.000001;
		double dbAccessR = 0.7;
		int btVmBT = 10;
		int dtVmBt = 15;
		int monitoringInterval = 5;
		Monitor monitor = new Monitor();
		IaaS biaas = new IaaS(btVmBT, btServDem);
		IaaS diaas = new IaaS(dtVmBt, dtServDem);
		DecisionMaker decisionMaker = new ThresholdBasedDecisionMaker();
		Application app = new Application(biaas, diaas, btServDem, dtServDem, dbAccessR);
		monitor.SetInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		int duration = monitor.GetExperimentDuration();
		Timer timer = new Timer(monitoringInterval, monitor, decisionMaker, app);
		int time = 0;
		while (time < duration)
		{
			timer.tick();
			time++;
		}
		app.EndExperiment(duration*monitoringInterval);
		System.out.println("Operational Cost: "+Integer.toString(app.GetOperationalCost()));
		System.out.println("VM Thrashing: " + Integer.toString(app.GetVmThrashing()));
		System.out.println("SLA Violation Count: "+ Integer.toString(timer.GetSlaViolationCount()));
	}
}
