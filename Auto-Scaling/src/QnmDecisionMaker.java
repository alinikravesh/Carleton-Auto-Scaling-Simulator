import java.util.List;

public class QnmDecisionMaker extends DecisionMaker{
	private int vmCapacity = (int)Math.floor(1/serviceDemand);
	
	public void GenerateScalingAction(double load, int time, IaaS infrastructure) {
		ScalingTimerTick();
		if (freezFlag)
		{
			return;	
		}			
		int numberOfVms = infrastructure.GetNumberOfVms();
		int workload = (int)Math.ceil(load);
		int requiredVms = 0;
		if (workload%vmCapacity == 0)
		{
			requiredVms = workload/vmCapacity;
		}
		else
		{
			requiredVms = workload/vmCapacity + 1;
		}
		if (requiredVms > numberOfVms)
		{
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		else if (numberOfVms > requiredVms)
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
		return;
	}

	public void GenerateScalingActionFullHour(double load, int time, IaaS infrastructure) {
		ScalingTimerTick();
		if (freezFlag)
		{
			return;	
		}			
		int numberOfVms = infrastructure.GetNumberOfVms();
		int workload = (int)Math.ceil(load);
		int requiredVms = 0;
		if (workload%vmCapacity == 0)
		{
			requiredVms = workload/vmCapacity;
		}
		else
		{
			requiredVms = workload/vmCapacity + 1;
		}
		if (requiredVms > numberOfVms)
		{
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		else if (numberOfVms > requiredVms)
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
		return;		
	}

}
