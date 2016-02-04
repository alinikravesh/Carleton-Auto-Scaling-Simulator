import java.io.IOException;

public class Simulator extends InfrastructurePropertires{
	public static void main(String[] args) throws IOException
	{
		Monitor monitor = new Monitor();
		IaaS iaas = new IaaS();
		DecisionMaker decisionMaker = new DecisionMaker(iaas);
		monitor.setInputFile("C:\\Users\\alinikravesh\\Dropbox\\MyPersonalFolder\\University\\Simulation\\CyclicWorkload.xls");
		int duration = monitor.GetExperimentDuration();
		Timer timer = new Timer(monitoringInterval,appVmCapacityPerMinute, dbVmCapacityPerMinute, appLayerVmBootUpTime, dbLayerVmBootUpTime, monitor, decisionMaker, iaas);
		int time = 0;
		while (time < duration)
		{
			timer.tick();
			time++;
		}		
	}
}
