import java.util.List;

//Implements Decision Maker component
public class DecisionMaker extends InfrastructurePropertires{ 
	private IaaS infrastructure;
	private boolean freezFlag = false;
	private int freezDuration;
	
	//Constructor
	public DecisionMaker(IaaS iaas)
	{
		this.infrastructure = iaas;
	}
	
	//Sets a timer to freeze decision maker to prevent VM thrashing
	private void ScalingTimerSet()
	{
		freezFlag = true;
		freezDuration = appLayerVmBootUpTime; //we assume decision maker freezes until the new VM is up and running. This is because of VM thrashing issue
	}
	
	//Emulates a timer that keeps account of the freezing period
	private void ScalingTimerTick()
	{
		freezDuration -= monitoringInterval;
		if (freezDuration < 0)
		{
			freezFlag = false;
		}
	}
	
	//Generates scaling actions
	//Receives the current workload and the current time
	//Calls IaaS environment API with appropriate scaling actions
	public void GenerateScalingAction(double load, int time)
	{
		ScalingTimerTick();
		if (freezFlag)
		{
			return;	
		}			
		double ceilingCapacity = (double)infrastructure.GetCurrentCapacity();
		double floorCapacity = (double)infrastructure.GetCapacityAfterScaleDown();
		if (floorCapacity > load)
		{
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
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		return;
	}
}
