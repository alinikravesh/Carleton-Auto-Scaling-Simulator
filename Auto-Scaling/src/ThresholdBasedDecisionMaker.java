import java.util.List;

public class ThresholdBasedDecisionMaker extends DecisionMaker{ 
	
	public void GenerateScalingAction(double load, int time, IaaS infrastructure)
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
	
	public void GenerateScalingActionFullHour(double load, int time, IaaS infrastructure)
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
			List<VirtualMachine> rentedVm = infrastructure.GetVmList();
			for(VirtualMachine vm : rentedVm)
			{
				if (vm.end < 0)
				{
					int vmRunningDuration = time - vm.start;
					if (vmRunningDuration%60 == 0)
					{
						vmIdToBeStopped = vm.id;
					}
				} 
			}
			if (vmIdToBeStopped >= 0)
			{
				infrastructure.scaleDown(vmIdToBeStopped, time);
				ScalingTimerSet();
			}			
		}
		else if (load > ceilingCapacity)
		{
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		return;
	}
}
