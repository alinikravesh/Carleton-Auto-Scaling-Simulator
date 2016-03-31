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
	
	double ceilingCapacity = 0;
	double floorCapacity = -20;
	int margin = 0;
	private List<FreezingInfo> vUp = new ArrayList<FreezingInfo>();
	private List<FreezingInfo> vDown = new ArrayList<FreezingInfo>();
	int vUpDuration = 20;
	int vDownDuration = 0;
	

	public ThresholdBasedDecisionMaker(Application appl) {
		app = appl;
		tierCount = app.GetTierCount();
		for (int i=0; i<tierCount; i++)
		{
			freezingFlags.add(new FreezingInfo(i, false, app.GetTier(i).GetIaaS().GetVmBootUpTime()));
			vUp.add(new FreezingInfo(i, false, vUpDuration));
			vDown.add(new FreezingInfo(i, false, vDownDuration));
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
		//System.out.println("time:"+time+":load:"+load+":ceiling:"+ceilingCapacity+":floor:"+floorCapacity);
		for(int i=0; i<app.GetTierCount(); i++)
		{
			SoftwareTier st = app.GetTier(i);
			if (!GetStatus(i))
			{
				IaaS iaas = st.GetIaaS();
				ceilingCapacity = (double)iaas.GetCurrentCapacity();
				floorCapacity = (double)iaas.GetCapacityAfterScaleDown();
				double workload = load * st.GetAccessRate();
				if (st.GetName() == "BusinessTier")
				{
//					System.out.println("time:"+time+":load:"+workload+":ceiling:"+ceilingCapacity+":floor:"+floorCapacity);
				}
				if (floorCapacity -margin > workload)
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
//					if (st.GetName().startsWith("Bu"))
//						System.out.println("Time: "+time+" Layer:"+st.GetName()+" -1");
//					System.out.println(st.GetName() +"D");
					vDownTick(i);
					if (vDownGetStatus(i))
					{
						iaas.ScaleDown(vmIdToBeStopped, time);
						ScalingTimerSet(i);
						vDownSet(i);
					}
					
				}
				else if (workload > ceilingCapacity -margin)
				{
//					if (st.GetName().startsWith("Bu"))
//						System.out.println("Time: "+time+" Layer:"+st.GetName()+" 1");
//					System.out.println(st.GetName() +"U");
					vUpTick(i);
					if (vUpGetStatus(i))
					{
						iaas.ScaleUp(time);
						ScalingTimerSet(i);
						vUpSet(i);
					}
				}
				else
				{
//					if (st.GetName().startsWith("Bu"))
//						System.out.println("Time: "+time+" Layer:"+st.GetName()+" 0");
				}
			}
			else
			{
//				if (st.GetName().startsWith("Bu"))
//					System.out.println("Time: "+time+" Layer:"+st.GetName()+" 0");
			}
		}
		return;
	}

	private boolean vDownGetStatus(int i) {
		boolean res = false; 
		for(FreezingInfo inf: vDown)
		{
			if (inf.number == i)
			{
				res = inf.status;
			}
		}
		return res;
	}
	
	private boolean vUpGetStatus(int i) {
		boolean res = false; 
		for(FreezingInfo inf: vUp)
		{
			if (inf.number == i)
			{
				res = inf.status;
			}
		}
		return res;
	}

	public void ScalingTimerSet(int i) {
		for(FreezingInfo inf : freezingFlags)
		{
			if (inf.number == i)
			{
				inf.status = true;
//				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
				inf.duration = 0;
			}
		}
	}
	
	public void vUpSet(int i) {
		for(FreezingInfo inf : vUp)
		{
			if (inf.number == i)
			{
				inf.status = false;
//				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
				inf.duration = vUpDuration;
			}
		}
	}
	
	public void vDownSet(int i) {
		for(FreezingInfo inf : vDown)
		{
			if (inf.number == i)
			{
				inf.status = false;
//				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
				inf.duration = vDownDuration;
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
	protected  void vUpTick(int i)
	{
		for(FreezingInfo inf: vUp)
		{
			if (inf.number == i)
			{
				inf.duration -= Simulator.monitoringInterval;
				if (inf.duration < 0)
				{
					inf.status = true;
				}
			}
			
		}
	}	
	protected  void vDownTick(int i)
	{
		for(FreezingInfo inf: vDown)
		{
			if (inf.number == i)
			{
				inf.duration -= Simulator.monitoringInterval;
				if (inf.duration < 0)
				{
					inf.status = true;
				}
			}
			
		}
	}	
}
