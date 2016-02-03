import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IaaS extends InfrastructurePropertires{
	public List<VirtualMachine> rentedVM = new ArrayList<VirtualMachine>();
	public List<Integer> vmIds = new ArrayList<Integer>();
	private List<Log> spool = new ArrayList<Log>();
	public int capacity;
	
	public IaaS()
	{
		VirtualMachine vm = new VirtualMachine();
		vm.start = 0;
		vm.end = -1;
		vm.id = 0;
		vm.status = 1;
		vmIds.add(0);
		rentedVM.add(vm);
	}
	
	public void Tick(int curTime)
	{
		List<Integer> _index = new ArrayList<Integer>();
		for(int i=0; i< spool.size(); i++)
		{
			Log log = spool.get(i);
			if (curTime - log.time >= appLayerVmBootUpTime)
			{
				for(VirtualMachine vm : rentedVM)
				{
					if (vm.id == log.vmId)
					{
						vm.status = 1;
					}
				}
				
			}
			_index.add(i);
		}
		for (Integer index : _index)
		{
			spool.remove(index);
		}	
	}
	
	public List<VirtualMachine> GetVmList()
	{
		return rentedVM;
	}
	
	private void StartVm(int id, int time)
	{
		spool.add(new Log(time, id));
	}
	
	private int GetVMNewId()
	{
		Random rn = new Random();
		return rn.nextInt(200) + 1;
	}
	
	public void scaleUp(int time)
	{
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
		StartVm(vm.id, time);
		rentedVM.add(vm);
	}
	
	public void scaleDown(int id, int time)
	{
		for(VirtualMachine vm : rentedVM)
		{
			if(vm.id == id)
			{
				vm.end = time;
			}
		}
		vmIds.remove(vmIds.indexOf(id));		
	}
	
	public int GetCurrentCapacity()
	{
		int capacity = 0;
		for(VirtualMachine vm : rentedVM)
		{
			if (vm.status == 1)
			{
				capacity += vm.capacity;	
			}
		}
		this.capacity = capacity;
		return capacity;
	}
	
	public int GetCapacityAfterScaleDown()
	{
		if (vmIds.size() < 2)
			return -20;
		return (capacity - appVmCapacityPerMinute);
	}
	

}
