import java.util.List;

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
	
	public void GenerateScalingAction(double load, int time)
	{
//		String action = "N";
		ScalingTimerTick();
		if (freezFlag)
			return;
		double ceilingCapacity = (double)infrastructure.GetCurrentCapacity();
		double floorCapacity = (double)infrastructure.GetCapacityAfterScaleDown();
		if (floorCapacity > load)
		{
//			action = "D";
			int vmIdToBeStopped = -1;
			int minTimeToFullHour = 100;
			List<VirtualMachine> rentedVm = infrastructure.GetVmList();
			for(VirtualMachine vm: rentedVm)
			{
				if (vm.end < 0)
				{
					int vmRunningDuration = time - vm.start;
					if (vmRunningDuration%60 == 0)
					{
						vmIdToBeStopped = vm.id;
						break;
					}
					int timeToFullHour = (((vmRunningDuration/60)+1)*60)-vmRunningDuration;
					if (minTimeToFullHour > timeToFullHour)
					{
						minTimeToFullHour = timeToFullHour;
						vmIdToBeStopped = vm.id;
					}
				} 
			}
			infrastructure.scaleDown(vmIdToBeStopped, time);
			ScalingTimerSet();
		}
		else if (load > ceilingCapacity)
		{
//			action = "U";
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		return;
	}
}
