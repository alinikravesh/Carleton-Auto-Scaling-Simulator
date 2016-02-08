import java.util.List;

//Implements Decision Maker component
public class DecisionMakerI implements DecisionMaker{ 
//	private IaaS infrastructure;
	private boolean freezFlag = false;
	private int freezDuration;
	private int vmCapacity = (int)Math.ceil(60/serviceDemand);
	
	
	//Constructor
//	public DecisionMaker(IaaS iaas)
//	{
//		this.infrastructure = iaas;
//	}
	
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
	public void GenerateScalingAction(double load, int time, IaaS infrastructure)
	{
		String action = "0";
		ScalingTimerTick();
		if (freezFlag)
		{
			System.out.println(action);
			return;	
		}			
		double ceilingCapacity = (double)infrastructure.GetCurrentCapacity();
		double floorCapacity = (double)infrastructure.GetCapacityAfterScaleDown();
		int numberOfVms = infrastructure.GetNumberOfVms();
		int workload = (int)Math.ceil(load);
		int requiredVms = (int)Math.ceil(workload/vmCapacity); 
		if (requiredVms > numberOfVms)
		{
			//Scale up
		}
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
			action = "-1";
			infrastructure.scaleDown(vmIdToBeStopped, time);
			ScalingTimerSet();
		}
		else if (load > ceilingCapacity)
		{
			action = "1";
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		System.out.println(action);
		return;
	}
	
	public void GenerateScalingActionQNM(double load, int time, IaaS infrastructure)
	{
		String action = "0";
		ScalingTimerTick();
		if (freezFlag)
		{
			System.out.println(action);
			return;	
		}			
		int numberOfVms = infrastructure.GetNumberOfVms();
		int workload = (int)Math.ceil(load);
		int requiredVms = (int)Math.ceil(workload/vmCapacity); 
		if (requiredVms > numberOfVms)
		{
			action = "1";
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
			action = "-1";
			infrastructure.scaleDown(vmIdToBeStopped, time);
			ScalingTimerSet();
		}
		System.out.println(action);
		return;
	}
	
	public void GenerateScalingActionFullHour(double load, int time, IaaS infrastructure)
	{
		String action = "0"; 
		ScalingTimerTick();
		if (freezFlag)
		{
			System.out.println(action);
			return;	
		}			
		double ceilingCapacity = (double)infrastructure.GetCurrentCapacity(load);
		double floorCapacity = (double)infrastructure.GetCapacityAfterScaleDown(load);
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
				action = "-1";
				infrastructure.scaleDown(vmIdToBeStopped, time);
				ScalingTimerSet();
			}			
		}
		else if (load > ceilingCapacity)
		{
			action = "1";
			infrastructure.scaleUp(time);
			ScalingTimerSet();
		}
		System.out.println(action);
		return;
	}

	@Override
	public void GenerateScalingAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GenerateScalingActionFullHour() {
		// TODO Auto-generated method stub
		
	}
}
