package ScalingUnit;
import java.util.ArrayList;
import java.util.List;

import Application.Application;
import Application.SoftwareTier;
import Common.IaaS;
import Common.ScalingUnitInterface;
import Common.Simulator;
import Common.VirtualMachine;

public class ThresholdBasedDecisionMaker implements ScalingUnitInterface{ 
	private int tierCount = 0;
	private List<FreezingInfo> freezingFlags = new ArrayList<FreezingInfo>();
	private Application app = new Application();
	
	public ThresholdBasedDecisionMaker(Application appl) {
		app = appl;
		tierCount = app.GetTierCount();
		for (int i=0; i<tierCount; i++)
		{
			freezingFlags.add(new FreezingInfo(i, false, app.GetTier(i).GetIaaS().GetVmBootUpTime()));
		}
	}
	
	private boolean GetStatus(int i)
	{
		boolean res = false; 
		for(FreezingInfo inf: freezingFlags)
		{
			if (inf.number == i)
			{
				res = inf.status;
			}
		}
		return res;
	}
	
	public void GenerateScalingAction(double load, int time, Application app)
	{
		ScalingTimerTick();		
		for(int i=0; i<app.GetTierCount(); i++)
		{
			SoftwareTier st = app.GetTier(i);
			if (!GetStatus(i))
			{
				IaaS iaas = st.GetIaaS();
				double ceilingCapacity = (double)iaas.GetCurrentCapacity();
				double floorCapacity = (double)iaas.GetCapacityAfterScaleDown();
				double workload = load * st.GetAccessRate();
				if (floorCapacity > workload)
				{
					int vmIdToBeStopped = -1;
					int minTimeToFullHour = 100;
					List<VirtualMachine> rentedVm = iaas.GetVmList();
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
					iaas.ScaleDown(vmIdToBeStopped, time);
					ScalingTimerSet(i);
				}
				else if (workload > ceilingCapacity)
				{
					iaas.ScaleUp(time);
					ScalingTimerSet(i);
				}
			}
		}
		return;
	}

	public void ScalingTimerSet(int i) {
		for(FreezingInfo inf : freezingFlags)
		{
			if (inf.number == i)
			{
				inf.status = true;
				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
			}
		}
	}
	
	protected  void ScalingTimerTick()
	{
		for(FreezingInfo inf: freezingFlags)
		{
			inf.duration -= Simulator.monitoringInterval;
			if (inf.duration < 0)
			{
				inf.status = false;
			}
		}
	}		
}
