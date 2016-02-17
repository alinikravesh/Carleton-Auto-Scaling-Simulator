package Common;
import java.io.IOException;

import Application.Application;
import ScalingUnit.DecisionMaker;

//Emulates universal timer
public class Timer {
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private DecisionMaker decisionMaker;
	private Application app;
	private int slaViolationCount;
	
	//Constructor
	public Timer(int inter, Monitor mon, DecisionMaker dm, Application app) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.decisionMaker = dm;
		this.app = app;
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
			decisionMaker.GenerateScalingAction(load, currentTime, app);	
//			app.Tick(currentTime);
			if (app.GetResponseTime(load) > Application.responseTimeThreshold)
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

