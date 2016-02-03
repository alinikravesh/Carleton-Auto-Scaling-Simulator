
public class DecisionMaker extends InfrastructurePropertires{
	private int currentCapacity = appVmCapacityPerMinute; 
	private IaaS infrastructure;
	
	public DecisionMaker(IaaS iaas)
	{
		this.infrastructure = iaas;
	}
	public int CalculateCapacity()
	{
		return currentCapacity;
	}
	
	public String GenerateScalingAction(double load)
	{
		String action = "N";
		double capacity = (double)infrastructure.GetCurrentCapacity();
		if (capacity < load)
		{
			action = "U";
		}
		else if (capacity > (double)infrastructure.GetCapacityAfterScaleDown())
		{
			action = "D";
		}
		return action;
	}
}
