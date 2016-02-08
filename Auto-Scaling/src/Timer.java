import java.io.IOException;

//Emulates universal timer
public class Timer {
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private DecisionMaker decisionMaker;
	private IaaS infrastructure;
	private boolean fullHourFlag;
	private boolean printCapacityFlag = false;
	private int slaViolationCount;
	
	//Constructor
	public Timer(int inter, Monitor mon, DecisionMaker dm, IaaS iaas, boolean fullHour, boolean printCap) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.infrastructure = iaas;
		this.fullHourFlag = fullHour;
		this.printCapacityFlag = printCap;
	}
	
	public Timer(int inter, Monitor mon, DecisionMaker dm, IaaS iaas, boolean fullHour) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.infrastructure = iaas;
		this.fullHourFlag = fullHour;
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
			if (fullHourFlag)
			{
				decisionMaker.GenerateScalingActionFullHour(load, currentTime, this.infrastructure);	
			}
			else
			{
				decisionMaker.GenerateScalingAction(load, currentTime, this.infrastructure);
			}
			infrastructure.Tick(currentTime, printCapacityFlag);
			if (infrastructure.GetResponseTime(load) > 7)
			{
				slaViolationCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentTime += interval;
	}
	
	public int GetSlaViolationCount()
	{
		return slaViolationCount;
	}
}

