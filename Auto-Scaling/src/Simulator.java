import java.io.IOException;

public class Simulator extends InfrastructurePropertires{
	public static void main(String[] args) throws IOException
	{
		Monitor monitor = new Monitor();
		IaaS biaas = new IaaS();
		IaaS diaas = new IaaS();
		DecisionMaker bdecisionMaker = new DecisionMaker(biaas);
		DecisionMaker ddecisionMaker = new DecisionMaker(diaas);
		monitor.setInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		int duration = monitor.GetExperimentDuration();
//		Timer timer = new Timer(monitoringInterval, monitor, decisionMaker, iaas);
		MultiLayerTimer timer = new MultiLayerTimer(monitoringInterval, monitor, bdecisionMaker, ddecisionMaker, biaas, diaas);
		int time = 0;
		while (time < duration)
		{
			timer.tick();
			time++;
		}
		biaas.EndExperiment(duration*monitoringInterval);
		diaas.EndExperiment(duration*monitoringInterval);
		System.out.println(Integer.toString(biaas.CalculateOperationalCost())+" BUSINESS");
		System.out.println(Integer.toString(diaas.CalculateOperationalCost())+ " DATABASE");
	}
}
