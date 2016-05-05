package ScalingUnit;

import java.util.ArrayList;
import java.util.List;

import Application.Application;
import Application.SoftwareTier;
import Common.IaaS;
import Common.ScalingUnitInterface;
import Common.Simulator;
import Common.VirtualMachine;

public class AmazonDecisionMaker implements ScalingUnitInterface{

	private double lT = 0.0;
	private double uT = 0.0;
	private int tierCount = 0;
	private List<FreezingInfo> freezingFlags = new ArrayList<FreezingInfo>();
	private Application app = new Application();
	
	public AmazonDecisionMaker(double lowerThreshold, double upperThreshold, Application appl)
	{
		lT = lowerThreshold;
		uT = upperThreshold;
		app = appl;
		tierCount = app.GetTierCount();
		for (int i=0; i<tierCount; i++)
		{
			freezingFlags.add(new FreezingInfo(i, false, app.GetTier(i).GetIaaS().GetVmBootUpTime()));
		}
	}
	
	public void SetUpperThreshold(double upperThreshold)
	{
		uT = upperThreshold;
	}
	
	public double GetUpperThreshold()
	{
		return uT;
	}
	
	public void SetLowerThreshold(double lowerThreshold)
	{
		lT = lowerThreshold;
	}
	
	public double GetLowerThreshold()
	{
		return lT;
	}
	
	private double CalculateCpuUtilization(double load, double serviceDemand)
	{
		double utilization = 0.0;
		utilization = load * serviceDemand*100;
		return utilization;
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
	public void GenerateScalingAction(double load, int time, Application app, double actualLoad) {
		//FOR EACH LAYER
		//CALCULATE CPU UTILIZATION
		//SET UPPER THRESHOLD AND LOWER THRESHOLD FOR THE UTILIZATION
		//SCALE UP/DOWN THE APPLICATION WHENEVER CPU UTILIZATION EXCEEDS UPPER/LOWER THRESHOLD
		ScalingTimerTick();	
		for(int i=0; i<app.GetTierCount(); i++)
		{
			SoftwareTier st = app.GetTier(i);
			if (!GetStatus(i))
			{
				IaaS iaas = st.GetIaaS();
				double workload = load*st.GetAccessRate();
				double utilization = CalculateCpuUtilization(workload/iaas.GetNumberOfUpVms(), st.GetDemand());
				double ceilingCapacity = uT; 
				double floorCapacity = GetFloorCapacity(lT, iaas.GetNumberOfUpVms());
				if (st.GetName() == "BusinessTier")
					System.out.println("time:"+time+":utilization:"+utilization+":ceiling:"+ceilingCapacity+":floor:"+floorCapacity);
				if (utilization >= ceilingCapacity)
				{
					//scale up
					iaas.ScaleUp(time);
					ScalingTimerSet(i);
				}
				else if (utilization <= floorCapacity)
				{
					//scale down
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
			}
		}
		return;
	}

	private double GetFloorCapacity(double lT2, int numberOfUpVms) {
		if (numberOfUpVms <2)
			return -20.0;
		return lT2;
	}



}
