
public class DecisionMaker extends InfrastructurePropertires{ 
	private IaaS infrastructure;
	private boolean freezFlag = false;
	private int freezDuration;
	
	public DecisionMaker(IaaS iaas)
	{
		this.infrastructure = iaas;
	}
	
	private void ScalingTimerSet()
	{
		freezFlag = true;
		freezDuration = appLayerVmBootUpTime;
	}
	
	private void ScalingTimerTick()
	{
		freezDuration -= monitoringInterval;
		if (freezDuration < 0)
		{
			freezFlag = false;
		}
	}
	
	public String GenerateScalingAction(double load, int time)
	{
		String action = "N";
		ScalingTimerTick();
		if (freezFlag)
			return action;
		double ceilingCapacity = (double)infrastructure.GetCurrentCapacity();
		double floorCapacity = (double)infrastructure.GetCapacityAfterScaleDown();
		if (floorCapacity > load && floorCapacity > 0)
		{
			action = "D";
//			infrastructure.scaleDown(id, time);
			ScalingTimerSet();
		}
		else if (load > ceilingCapacity)
		{
			action = "U";
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		return action;
	}
}
