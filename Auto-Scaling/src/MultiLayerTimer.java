import java.io.IOException;

//Emulates universal timer for multi-layer environments 
public class MultiLayerTimer extends InfrastructurePropertires{
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private DecisionMaker blDecisionMaker;
	private DecisionMaker dlDecisionMaker;
	private IaaS blInfrastructure;
	private IaaS dlInfrastructure;
	private int slaViolationCount;
	private boolean fullHourFlag;
	
	//Constructor
	public MultiLayerTimer(int inter, Monitor mon, DecisionMaker bldm, DecisionMaker dldm,IaaS bliaas, IaaS dliaas, boolean fullHour) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.blDecisionMaker = bldm;
		this.blInfrastructure = bliaas;
		this.dlDecisionMaker = dldm;
		this.dlInfrastructure = dliaas;
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
				blDecisionMaker.GenerateScalingActionFullHour(load, currentTime, this.blInfrastructure);
				dlDecisionMaker.GenerateScalingActionFullHour(load*databaseAccessRate, currentTime, this.dlInfrastructure);
			}
			else
			{
				blDecisionMaker.GenerateScalingAction(load, currentTime, this.blInfrastructure);
				dlDecisionMaker.GenerateScalingAction(load*databaseAccessRate, currentTime, this.dlInfrastructure);
			}
			blInfrastructure.Tick(currentTime, false);
			dlInfrastructure.Tick(currentTime, false);
			if (blInfrastructure.GetResponseTime(load) > 7)
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

