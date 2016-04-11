package ScalingUnit;
import java.util.ArrayList;
import java.util.List;

import Application.Application;
import Application.SoftwareTier;
import Common.IaaS;
import Common.ScalingUnitInterface;
import Common.Simulator;
import Common.VirtualMachine;

public class ThresholdBasedFullHour implements ScalingUnitInterface{ 
	private int tierCount = 0;
	private List<FreezingInfo> freezingFlags = new ArrayList<FreezingInfo>();
	private Application app = new Application();
	
	double ceilingCapacity = 0;
	double floorCapacity = -20;
	int margin = 0;
	private List<FreezingInfo> vUp = new ArrayList<FreezingInfo>();
	private List<FreezingInfo> vDown = new ArrayList<FreezingInfo>();
	private List<FreezingInfo> inU = new ArrayList<FreezingInfo>();
	private List<FreezingInfo> inL = new ArrayList<FreezingInfo>();
	int vUpDuration = 0;
	int vDownDuration = 0;
	int inUDuration = 0;
	int inLDuration = 0;
	

	public ThresholdBasedFullHour(Application appl) {
		app = appl;
		tierCount = app.GetTierCount();
		for (int i=0; i<tierCount; i++)
		{
			freezingFlags.add(new FreezingInfo(i, false, app.GetTier(i).GetIaaS().GetVmBootUpTime()));
			inU.add(new FreezingInfo(i, false, inUDuration));
			inL.add(new FreezingInfo(i, false, inLDuration));
			vUp.add(new FreezingInfo(i, false, vUpDuration));
			vDown.add(new FreezingInfo(i, false, vDownDuration));
		}
	}
	
	public ThresholdBasedFullHour(Application appl, double thrU, double thrLo, int vUpd, int vDownd, int inUd, int inLd)
	{
		app = appl;
		tierCount = app.GetTierCount();
		for (int i=0; i<tierCount; i++)
		{
			freezingFlags.add(new FreezingInfo(i, false, app.GetTier(i).GetIaaS().GetVmBootUpTime()));
			inUDuration = inUd*Simulator.monitoringInterval;
			inLDuration = inLd*Simulator.monitoringInterval;
			vUpDuration = vUpd*Simulator.monitoringInterval;
			vDownDuration = vDownd*Simulator.monitoringInterval;
			inU.add(new FreezingInfo(i, false, inUDuration));
			inL.add(new FreezingInfo(i, false, inLDuration));
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
	
	private boolean GetInUStatus(int i)
	{
		boolean res = false; 
		for(FreezingInfo inf: inU)
		{
			if (inf.number == i)
			{
				res = inf.status;
			}
		}
		return res;
	}
	
	private boolean GetInLStatus(int i)
	{
		boolean res = false; 
		for(FreezingInfo inf: inL)
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
//				ceilingCapacity = (double)iaas.GetCurrentCapacity();
				ceilingCapacity = (double)iaas.GetCeilingCapacity();
//				floorCapacity = (double)iaas.GetCapacityAfterScaleDown();
				floorCapacity = (double)iaas.GetFloorCapacity();
				double workload = load * st.GetAccessRate();
				if (st.GetName() == "BusinessTier")
				{
//					System.out.println("time:"+time+":load:"+workload+":ceiling:"+ceilingCapacity+":floor:"+floorCapacity);
				}
				if ((floorCapacity -margin > workload) & !GetInLStatus(i))
				{
					vUpSet(i);
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
//							int timeToFullHour = (((vmRunningDuration/60)+1)*60)-vmRunningDuration;
//							if (minTimeToFullHour > timeToFullHour)
//							{
//								minTimeToFullHour = timeToFullHour;
//								vmIdToBeStopped = vm.id;
//							}
						} 
					}
//					if (st.GetName().startsWith("Bu"))
//						System.out.println("Time: "+time+" Layer:"+st.GetName()+" -1");
//					System.out.println(st.GetName() +"D");
					vDownTick(i);
					if (vDownGetStatus(i))
					{
						if (vmIdToBeStopped >= 0)
						{
							iaas.ScaleDown(vmIdToBeStopped, time);
							InLSet(i);
							ScalingTimerSet(i);
							vDownSet(i);
						}
						else
						{
//							System.out.println("failed time:"+time);
						}
					}
					
				}
				else if ((workload > ceilingCapacity -margin) & !GetInUStatus(i))
				{
					vDownSet(i);
//					if (st.GetName().startsWith("Bu"))
//						System.out.println("Time: "+time+" Layer:"+st.GetName()+" 1");
//					System.out.println(st.GetName() +"U");
					vUpTick(i);
					if (vUpGetStatus(i))
					{
						iaas.ScaleUp(time);
						InUSet(i);
						ScalingTimerSet(i);
						vUpSet(i);
					}
				}
				else
				{
					vUpSet(i);
					vDownSet(i);
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
	
	public void InUSet(int i) {
		for(FreezingInfo inf : inU)
		{
			if (inf.number == i)
			{
				inf.status = true;
//				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
				inf.duration = inUDuration;
			}
		}
	}
	
	public void InLSet(int i) {
		for(FreezingInfo inf : inL)
		{
			if (inf.number == i)
			{
				inf.status = true;
//				inf.duration = app.GetTier(i).GetIaaS().GetVmBootUpTime();
				inf.duration = inLDuration;
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
		for(FreezingInfo inf: inU)
		{
			inf.duration -= Simulator.monitoringInterval;
			if (inf.duration < 0)
			{
				inf.status = false;
			}
		}
		for(FreezingInfo inf: inL)
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
