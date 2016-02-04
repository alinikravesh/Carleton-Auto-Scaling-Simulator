import java.io.IOException;


public class Timer {
	public int currentTime = 0;
	public int interval;
	private Monitor monitor;
	private DecisionMaker decisionMaker;
	private IaaS infrastructure;
	
	public Timer(int inter, Monitor mon, DecisionMaker dm, IaaS iaas) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.infrastructure = iaas;
	}
		
	
	public int GetTime()
	{
		return currentTime;
	}
	
	public void tick()
	{
		try {
			double load = monitor.GetWorkload(currentTime);
			decisionMaker.GenerateScalingAction(load, currentTime);
			infrastructure.Tick(currentTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentTime += interval;
	}	
}

