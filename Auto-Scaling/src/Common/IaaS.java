package Common;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Emulates IaaS API
public class IaaS{
	private List<VirtualMachine> rentedVM = new ArrayList<VirtualMachine>(); //List of rented VM during the experiment
	private List<Integer> vmIds = new ArrayList<Integer>(); //List of current VM IDs that are up and running
	private int currentVmCount = 0;
	private List<Log> spool = new ArrayList<Log>(); //List of VMs that are started but are not operational yet
	private double capacity; //Current capacity of the IaaS. Capacity refers to the number of requests that VM can handle per minute
	private int vmThrashing = 0;
	private int vmBootUpTime = 0;
	private double vmCap = 1;
	private int margin = 0;
	private double capU = 0.0;
	private double capL = 0.0; 
	private double thrU;
	private double thrL;
	private double serviceDemand;
	//Constructor
	//Adds one VM to the IaaS environment, because each application needs at least one VM to run. 
	public IaaS(int vmbt, double sd, double thrUd, double thrLd)
	{
		this.vmBootUpTime = vmbt;
		vmCap = (int)Math.floor(thrU/sd); 
		VirtualMachine vm = new VirtualMachine();
		vm.start = 0;
		vm.end = -1; //-1 represents that VM is still running
		vm.id = 0;
		vm.status = 1; //The first VM is up and running at the time on IaaS initialization
		vmIds.add(0);
		currentVmCount = 1;
		rentedVM.add(vm);
		thrU = thrUd;
		thrL = thrLd; 
		serviceDemand = sd;
	}
	
	public double GetCeilingCapacity()
	{
		double workload = (thrU*currentVmCount)/(serviceDemand); 
		return workload;
	}
	
	public double GetFloorCapacity()
	{
		double workload = (thrL*currentVmCount)/(serviceDemand); 
		return workload;
	}
	
	public int GetVmBootUpTime()
	{
		return vmBootUpTime;
	}
	
	//Emulates timer that is used by IaaS environment. 
	//This timer is used to change state of the started VMs to operational after spending boot-up time period
	//Receives current time from the universal Timer class
	public void Tick(int curTime)
	{
		List<Integer> _index = new ArrayList<Integer>();
		for(int i=0; i< spool.size(); i++)
		{
			Log log = spool.get(i);
			if (curTime - log.time >= vmBootUpTime)
			{
				for(VirtualMachine vm : rentedVM)
				{
					if (vm.id == log.vmId)
					{
						vm.status = 1;
					}
				}
				_index.add(i);
				
			}			
		}
		for (int index : _index)
		{
			spool.remove(index);
		}	
	}
	
	//Returns list of the rented VMs
	public List<VirtualMachine> GetVmList()
	{
		return rentedVM;
	}
	
	//Starts a new VM 
	//Receives VM id and the time that VM should be started
	private void StartVm(int id, int time)
	{
		spool.add(new Log(time, id));
	}
	
	//Finds a new VM inside VM pool
	//We assume that each application can at most rent 200 VMs during its life
	private int GetVMNewId()
	{
		Random rn = new Random();
		return rn.nextInt(200) + 1;
	}
	
	//Implements scale up action
	//Receives current time from the universal Timer class
	public void ScaleUp(int time)
	{
		vmThrashing++;
		VirtualMachine vm = new VirtualMachine();
		vm.start = time;
		vm.end = -1;
		vm.status = 0;
		vm.id = GetVMNewId();
		while (vmIds.contains(vm.id))
		{
			vm.id = GetVMNewId();
		}
		vmIds.add(vm.id);
		currentVmCount++;
		StartVm(vm.id, time);
		rentedVM.add(vm);
	}
	
	//Implements scale down action
	//Receives current time from the universal Timer class. Also receives id of the VM to be stopped
	public void ScaleDown(int id, int time)
	{
		vmThrashing++;
		for(VirtualMachine vm : rentedVM)
		{
			if(vm.id == id)
			{
				vm.end = time;
			}
		}
		currentVmCount--;
	}
	
	//Calculates current capacity of the IaaS infrastructure
	public double GetCurrentCapacity()
	{
		double capacity = 0;
//		for(VirtualMachine vm : rentedVM)
//		{
//			if ((vm.status == 1) && (vm.end < 0)) 
//			{
//				capacity += vmCap;	
//			}
//		}
		int num = GetNumberOfUpVms(); 
		capacity = num * (vmCap-margin - capU);
		this.capacity = capacity;
		return capacity;
	}
	
	public int GetNumberOfVms()
	{
		return this.currentVmCount;
	}
	
	public int GetNumberOfUpVms()
	{
		int res = 0;
		for(VirtualMachine vm : rentedVM)
		{
			if ((vm.status == 1) && (vm.end < 0))
			{
				res++;
			}
		}
		return res;
	}
	
	//Calculates capacity of the IaaS environment in the case of taking scale down action
	public double GetCapacityAfterScaleDown()
	{
		if (currentVmCount < 2)
			return -20;
		return (capacity + (currentVmCount*capU) - (vmCap +margin - (currentVmCount-1)*capL));
	}
	
	//Calculates operational cost 
	public int GetOperationalCost()
	{
		int cost = 0;
		for(VirtualMachine vm: rentedVM)
		{
//			System.out.println("VmId:" + vm.id + ":VmStart:" + vm.start + ":VmEnd:" + vm.end);
			int runningMinutes = vm.end - vm.start;
			int runningHours = 0;
			if (runningMinutes%60 == 0)
			{
				runningHours = runningMinutes/60;
			}
			else
			{
				runningHours = (runningMinutes/60)+1;
			}
			cost += runningHours * vm.price;
		}
		
		return cost;
	}
	
	public int hourVmGet(int s, int e)
	{
		int vmCount = 0;
		for(VirtualMachine vm: rentedVM)
		{
			if ((vm.start <=e) && (vm.end >= s))
			{
				vmCount++;
			}
		}
		return vmCount;
	}
	
	public void EndExperiment(int duration)
	{
		for(VirtualMachine vm: rentedVM)
		{
			if (vm.end==-1)
			{
				vm.end = duration;
			}
		}
	}
	
	public int GetVmThrashing()
	{
		return this.vmThrashing;
	}
}
