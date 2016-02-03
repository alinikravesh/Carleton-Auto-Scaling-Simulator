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
	}
	public void updateActionLog(int time, String action)
	{
		this._actionLog.put(time, action);
	}
	
	public void tick()
	{
		int t = 0;
		while (t <= this.duration)
		{
			if (this._actionLog.containsKey(t))
			{
				switch (_actionLog.get(t)) {
				case "BU":
					_btCurCap += _btCap; 
					Log lbu = new Log(t, _btCurCap, _dtCurCap);
					_capLog.add(lbu);
					break;
				case "DU":
					_dtCurCap += _dtCap;
					Log ldu = new Log(t, _btCurCap, _dtCurCap);
					_capLog.add(ldu);
					break;
				case "BD":
					_btCurCap -= _btCap;
					Log lbd = new Log(t, _btCurCap, _dtCurCap);
					_capLog.add(lbd);
					break;
				case "DD":
					_dtCurCap -= _dtCap;
					Log ldd = new Log(t, _btCurCap, _dtCurCap);
					_capLog.add(ldd);
					break;
				default:
					Log n = new Log(t, _btCurCap, _dtCurCap);
					_capLog.add(n);
					break;
				}
			}
			else
			{
				Log n = new Log(t, _btCurCap, _dtCurCap);
				_capLog.add(n);
			}
			t += interval;
		}
	}
	
	public void print()
	{
//		for (Map.Entry<Integer, String> it : _actionLog.entrySet())
//		{
//			System.out.println("time: " + it.getKey() + " Action: " + it.getValue());
//		}
		for (int i=0; i<_capLog.size(); i++)
		{
			Log l = _capLog.get(i);
			String time = Integer.toString(l.time);
			String busCap = Integer.toString(l.busCurCap);
			String datCap = Integer.toString(l.datCurCap);
			System.out.println("time: "+ time +" businessCap: "+ busCap + " databaseCap: "+ datCap);
		}
	}
	
	
	
}
