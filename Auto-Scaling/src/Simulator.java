import java.io.IOException;

public class Simulator extends InfrastructurePropertires{
	public static void main(String[] args) throws IOException
	{
		Monitor monitor = new Monitor();
		DecisionMaker decisionMaker = new DecisionMaker();
		IaaS iaas = new IaaS();
		Timer timer = new Timer(monitor.GetExperimentDuration(), monitoringInterval,appVmCapacityPerMinute, dbVmCapacityPerMinute, appLayerVmBootUpTime, dbLayerVmBootUpTime, monitor, decisionMaker, iaas);
		timer.tick();
				
	}
}
