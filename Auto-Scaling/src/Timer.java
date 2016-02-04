import java.io.IOException;

//Emulates universal timer
public class Timer {
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private DecisionMaker decisionMaker;
	private IaaS infrastructure;
	
	//Constructor
	public Timer(int inter, Monitor mon, DecisionMaker dm, IaaS iaas) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.infrastructure = iaas;
	}
		
	//Returns current time
	public int GetTime()
	{
		return currentTime;
	}
	
	//Emulates tick of the universal timer
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

