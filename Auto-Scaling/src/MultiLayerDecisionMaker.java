import java.util.List;
public class MultiLayerDecisionMaker extends InfrastructurePropertires{
//	private MlIaaS infrastructure;
//	private boolean blFreezFlag = false;
//	private boolean dlFreezFlag = false;
//	private int blFreezDuration;
//	private int dlFreezDuration;
//	
//	//Constructor
//	public MultiLayerDecisionMaker(MlIaaS iaas)
//	{
//		this.infrastructure = iaas;
//	}
//	
//	//Sets a timer to freeze decision maker to prevent VM thrashing
//	private void ScalingBlTimerSet()
//	{
//		blFreezFlag = true;
//		blFreezDuration = appLayerVmBootUpTime; //we assume decision maker freezes until the new VM is up and running. This is because of VM thrashing issue
//	}
//	
//	private void ScalingDlTimerSet()
//	{
//		dlFreezFlag = true;
//		blFreezDuration = appLayerVmBootUpTime; //we assume decision maker freezes until the new VM is up and running. This is because of VM thrashing issue
//	}
//	
//	//Emulates a timer that keeps account of the freezing period
//	private void ScalingTimerTick()
//	{
//		dlFreezDuration -= monitoringInterval;
//		blFreezDuration -= monitoringInterval;
//		if (dlFreezDuration < 0)
//		{
//			dlFreezFlag = false;
//		}
//		if (blFreezDuration < 0)
//		{
//			blFreezFlag = false;
//		}
//	}
//	
//	
//	public void BlGenerateScalingAction(double load, int time)
//	{
//		if (blFreezFlag)
//		{
//			return;	
//		}			
//		double blCeilingCapacity = (double)infrastructure.GetBlCurrentCapacity();
//		double blFloorCapacity = (double)infrastructure.GetBlCapacityAfterScaleDown();
//		if (blFloorCapacity > load)
//		{
//			int vmIdToBeStopped = -1;
//			int minTimeToFullHour = 100;
//			List<VirtualMachine> rentedVm = infrastructure.GetBlVmList();
//			for(VirtualMachine vm: rentedVm)
//			{
//				if (vm.end < 0)
//				{
//					int vmRunningDuration = time - vm.start;
//					if (vmRunningDuration%60 == 0)
//					{
//						vmIdToBeStopped = vm.id;
//						break;
//					}
//					int timeToFullHour = (((vmRunningDuration/60)+1)*60)-vmRunningDuration;
//					if (minTimeToFullHour > timeToFullHour)
//					{
//						minTimeToFullHour = timeToFullHour;
//						vmIdToBeStopped = vm.id;
//					}
//				} 
//			}
//			infrastructure.ScaleBlDown(vmIdToBeStopped, time);
//			ScalingBlTimerSet();
//		}
//		else if (load > ceilingCapacity)
//		{
//			infrastructure.ScaleBlUp(time);
//			ScalingBlTimerSet();
//		}
//		return;
//	}
//	
//	public void DlGenerateScalingAction(double load, int time)
//	{
//		if (dlFreezFlag)
//		{
//			return;	
//		}			
//		double dlCeilingCapacity = (double)infrastructure.GetDlCurrentCapacity();
//		double dlFloorCapacity = (double)infrastructure.GetDlCapacityAfterScaleDown();
//		if (dlFloorCapacity > load)
//		{
//			int vmIdToBeStopped = -1;
//			int minTimeToFullHour = 100;
//			List<VirtualMachine> rentedVm = infrastructure.GetBlVmList();
//			for(VirtualMachine vm: rentedVm)
//			{
//				if (vm.end < 0)
//				{
//					int vmRunningDuration = time - vm.start;
//					if (vmRunningDuration%60 == 0)
//					{
//						vmIdToBeStopped = vm.id;
//						break;
//					}
//					int timeToFullHour = (((vmRunningDuration/60)+1)*60)-vmRunningDuration;
//					if (minTimeToFullHour > timeToFullHour)
//					{
//						minTimeToFullHour = timeToFullHour;
//						vmIdToBeStopped = vm.id;
//					}
//				} 
//			}
//			infrastructure.ScaleDlDown(vmIdToBeStopped, time);
//			ScalingDlTimerSet();
//		}
//		else if (load > dlCeilingCapacity)
//		{
//			infrastructure.ScaleDlUp(time);
//			ScalingDlTimerSet();
//		}
//		return;
//	}
//	
//	
//	//Generates scaling actions
//	//Receives the current workload and the current time
//	//Calls IaaS environment API with appropriate scaling actions
//	public void GenerateScalingAction(double load, int time)
//	{
//		ScalingTimerTick();
//		BlGenerateScalingAction(load, time);
//		DlGenerateScalingAction(load*databaseAccessRate, time);
//		return;
//	}
}
