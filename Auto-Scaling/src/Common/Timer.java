package Common;
import java.io.IOException;

import Application.Application;
import Application.SoftwareTier;

//Emulates universal timer
public class Timer {
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private ScalingUnitInterface decisionMaker;
	private Application app;
	private int slaViolationCount;
	private int excessiveOperationalCost = 0;
	
	//Constructor
	public Timer(int inter, Monitor mon, ScalingUnitInterface dm, Application app) 
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
			for(SoftwareTier st : app.GetTiers())
			{
				st.GetIaaS().Tick(currentTime);	
			}
//			if (currentTime == 2395)
//			System.out.println(currentTime);
			if (app.GetResponseTime(load) > Application.responseTimeThreshold)
			{
				slaViolationCount++;
			}
			
			if (app.GetResponseTimeFloor(load) < Application.responseTimeThreshold)
			{
				excessiveOperationalCost++;
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
	
	public int GetExcessiveOperationalCost()
	{
		return excessiveOperationalCost;
	}
}

