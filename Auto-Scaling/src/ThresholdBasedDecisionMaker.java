import java.util.List;

import Application.Application;

public class ThresholdBasedDecisionMaker extends DecisionMaker{ 
	
	public void GenerateScalingAction(double load, int time, Application app)
	{
		ScalingTimerTick();
		if (freezFlag)
		{
			return;	
		}			
		double btCeilingCapacity = (double)app.GetBtIaaS().GetCurrentCapacity();
		double btFloorCapacity = (double)app.GetBtIaaS().GetCapacityAfterScaleDown();
		double dtCeilingCapacity = (double)app.GetDtIaaS().GetCurrentCapacity();
		double dtFloorCapacity = (double)app.GetDtIaaS().GetCapacityAfterScaleDown();
		
		int bottleneck = -1; //0 is business and 1 is database tiers
		
		if ((1/app.GetBussServDemand() <= load))
		{
			bottleneck = 0;
		}
		else if ((1/app.GetDbServDemand() <= load*app.GetDbAccessRate()))
		{
			bottleneck = 1;
		}
		
		if (bottleneck == 0)
		{
			app.GetBtIaaS().ScaleUp(time);
			return;
		}
		else if (bottleneck == 1)
		{
			app.GetDtIaaS().ScaleUp(time);
			return;
		}
		
		else
		{
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
				//test
			}
		}

		return;
	}
}
