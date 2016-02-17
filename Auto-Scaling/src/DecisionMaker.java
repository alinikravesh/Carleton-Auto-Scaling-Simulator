import Application.Application;

public abstract class DecisionMaker extends InfrastructurePropertires{
	protected boolean freezFlag = false;
	protected int freezDuration;
	
	public abstract void GenerateScalingAction(double load, int time, Application app);
	protected  void ScalingTimerSet()
	{
		freezFlag = true;
		freezDuration = appLayerVmBootUpTime; //we assume decision maker freezes until the new VM is up and running. This is because of VM thrashing issue
	}
	protected  void ScalingTimerTick()
	{
		freezDuration -= monitoringInterval;
		if (freezDuration < 0)
		{
			freezFlag = false;
		}
	}
}
