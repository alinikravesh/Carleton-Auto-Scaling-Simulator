import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Timer {
	public int currentTime = 0;
	public int interval;
	private Map<Integer, String> _actionLog;
	private int _btCap;
	private int _btVmBoot;
	private int _btCurCap;
	private int _dtCap;
	private int _dtVmBoot;
	private int _dtCurCap;
	private List<Log> _capLog;
	
	private Monitor monitor;
	private DecisionMaker decisionMaker;
	private IaaS infrastructure;
	
	public Timer(int inter, int businessTierCap, int databaseTierCap, int businessTierVmBoot, int databaseTierVmBoot, Monitor mon, DecisionMaker dm, IaaS iaas) 
	{
		this.interval = inter;
		this._actionLog = new HashMap<Integer,String>(); 
		this._btCap = businessTierCap;
		this._btVmBoot = businessTierVmBoot;
		this._dtVmBoot = databaseTierVmBoot;
		this._dtCap = databaseTierCap;
		this._capLog = new ArrayList<Log>();
		this._btCurCap = this._btCap;
		this._dtCurCap = this._dtCap;
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.infrastructure = iaas;
	}
//	public void updateActionLog(int time, String action)
//	{
//		this._actionLog.put(time, action);
//	}
	
	
	private void SetAction(String action)
	{
		throw new UnsupportedOperationException();
	}
	
	private String GetAction(int time)
	{
		throw new UnsupportedOperationException();
	}
	
	private void DoAction(String action)
	{
		throw new UnsupportedOperationException();	
	}
	
	public int GetTime()
	{
		return currentTime;
	}
	
	public void tick()
	{
		try {
			double load = monitor.GetWorkload(currentTime);
			String scalingAction = decisionMaker.GenerateScalingAction(load, currentTime);
			//SetAction(scalingAction);
			System.out.println(scalingAction);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String currentAction = GetAction(currentTime);
//		DoAction(currentAction);
//		System.out.println(infrastructure.GetCurrentCapacity());		
		currentTime += interval;
	}
	
//	public void print()
//	{
////		for (Map.Entry<Integer, String> it : _actionLog.entrySet())
////		{
////			System.out.println("time: " + it.getKey() + " Action: " + it.getValue());
////		}
//		for (int i=0; i<_capLog.size(); i++)
//		{
//			Log l = _capLog.get(i);
//			String time = Integer.toString(l.time);
//			String busCap = Integer.toString(l.busCurCap);
//			String datCap = Integer.toString(l.datCurCap);
//			System.out.println("time: "+ time +" businessCap: "+ busCap + " databaseCap: "+ datCap);
//		}
//	}
	
	
	
	
}

