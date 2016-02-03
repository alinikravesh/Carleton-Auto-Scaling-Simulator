import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Timer {
	public int currentTime = 0;
	public int interval;
	public int duration;
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
	
	public Timer(int dur, int inter, int businessTierCap, int databaseTierCap, int businessTierVmBoot, int databaseTierVmBoot) 
	{
		this.interval = inter;
		this.duration = dur * interval;
		this._actionLog = new HashMap<Integer,String>(); 
		this._btCap = businessTierCap;
		this._btVmBoot = businessTierVmBoot;
		this._dtVmBoot = databaseTierVmBoot;
		this._dtCap = databaseTierCap;
		this._capLog = new ArrayList<Log>();
		this._btCurCap = this._btCap;
		this._dtCurCap = this._dtCap;
		this.currentTime = 0;
	}
//	public void updateActionLog(int time, String action)
//	{
//		this._actionLog.put(time, action);
//	}
	
	private void Setup()
	{
		monitor = new Monitor();
		decisionMaker = new DecisionMaker();
		infrastructure = new IaaS();
	}
	
	private void SetAction(String action)
	{
		
	}
	
	private String GetAction(int time)
	{
		String action = "N";
		return action;
	}
	
	private void DoAction(String action)
	{
		
	}
	
	private String GetCurrentCapacity(int time)
	{
		int capacity = this._btCap;
		return Integer.toString(capacity); 
	}
	public void tick()
	{
		try {
			double load = monitor.GetWorkload(currentTime);
			String scalingAction = decisionMaker.GenerateScalingAction(load);
			SetAction(scalingAction);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String currentAction = GetAction(currentTime);
		DoAction(currentAction);
		System.out.println(GetCurrentCapacity(currentTime));	
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

