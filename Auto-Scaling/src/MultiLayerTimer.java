import java.io.IOException;
import java.sql.DatabaseMetaData;

//Emulates universal timer
public class MultiLayerTimer extends InfrastructurePropertires{
	private int currentTime = 0;
	private int interval;
	private Monitor monitor;
	private DecisionMaker blDecisionMaker;
	private DecisionMaker dlDecisionMaker;
	private IaaS blInfrastructure;
	private IaaS dlInfrastructure;
	
	//Constructor
	public MultiLayerTimer(int inter, Monitor mon, DecisionMaker bldm, DecisionMaker dldm,IaaS bliaas, IaaS dliaas) 
	{
		this.interval = inter; 
		this.currentTime = 0;
		this.monitor = mon;
		this.blDecisionMaker = bldm;
		this.blInfrastructure = bliaas;
		this.dlDecisionMaker = dldm;
		this.dlInfrastructure = dliaas;
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
			blDecisionMaker.GenerateScalingAction(load, currentTime);
			blInfrastructure.Tick(currentTime);
			dlDecisionMaker.GenerateScalingAction(load*databaseAccessRate, currentTime);
			dlInfrastructure.Tick(currentTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentTime += interval;
	}	
}

