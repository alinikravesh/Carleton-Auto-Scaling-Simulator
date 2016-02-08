import java.io.IOException;

public class SingleLayerSystemSimulator extends InfrastructurePropertires{
	public static void main(String[] args) throws IOException
	{		
		Monitor monitor = new Monitor();
		IaaS iaas = new IaaS();
		DecisionMaker decisionMaker = new DecisionMaker();	
		monitor.setInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		int duration = monitor.GetExperimentDuration();
		Timer timer = new Timer(monitoringInterval, monitor, decisionMaker, iaas, dontKillVmBeforeFullHour);
		int time = 0;
		while (time < duration)
		{
			timer.tick();
			time++;
		}
		iaas.EndExperiment(duration*monitoringInterval);
		System.out.println("Operational Cost: "+Integer.toString(iaas.CalculateOperationalCost()));
		System.out.println("VM Thrashing: " + Integer.toString(iaas.GetVmThrashing()));
	}
}
