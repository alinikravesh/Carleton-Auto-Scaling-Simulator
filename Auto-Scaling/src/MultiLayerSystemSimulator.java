import java.io.IOException;

public class MultiLayerSystemSimulator extends InfrastructurePropertires {
	public static void main(String[] args) throws IOException
	{
		Monitor monitor = new Monitor();
		IaaS biaas = new IaaS();
		IaaS diaas = new IaaS();
		DecisionMaker bdecisionMaker = new ThresholdBasedDecisionMaker();
		DecisionMaker ddecisionMaker = new ThresholdBasedDecisionMaker();
		monitor.setInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		int duration = monitor.GetExperimentDuration();
		MultiLayerTimer timer = new MultiLayerTimer(monitoringInterval, monitor, bdecisionMaker, ddecisionMaker, biaas, diaas, dontKillVmBeforeFullHour);
		int time = 0;
		while (time < duration)
		{
			timer.tick();
			time++;
		}
		biaas.EndExperiment(duration*monitoringInterval);
		diaas.EndExperiment(duration*monitoringInterval);
		System.out.println("Business Layer Operational Cost: " + Integer.toString(biaas.CalculateOperationalCost())+" BUSINESS");
		System.out.println("Database Layer Operational Cost: " + Integer.toString(diaas.CalculateOperationalCost())+ " DATABASE");
		System.out.println("Business Layer VM Thrashing: " + Integer.toString(biaas.GetVmThrashing()));
		System.out.println("Database Layer VM Thrashing: " + Integer.toString(diaas.GetVmThrashing()));
		System.out.println("SLA Violation Count: "+ Integer.toString(timer.GetSlaViolationCount()));
	}
}
