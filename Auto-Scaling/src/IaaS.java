import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IaaS extends InfrastructurePropertires{
	public List<VirtualMachine> rentedVM = new ArrayList<VirtualMachine>();
	public List<Integer> vmIds = new ArrayList<Integer>(); 
	public int capacity;
	
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
		vm.id = GetVMNewId();
		while (vmIds.contains(vm.id))
		{
			vm.id = GetVMNewId();
		}
		vmIds.add(vm.id);
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
			capacity =+ vm.capacity;
		}
		return capacity;
	}
	
	public int GetCapacityAfterScaleDown()
	{
		return (capacity - appVmCapacityPerMinute);
	}
	

}
