
public class MultiLayerDecisionMaker extends InfrastructurePropertires{
	private int _appLayerVmCount = 0;
	private int _dbLayerVmCount = 0;
	private int _currentAppLayerCapacity; 
	private int _currentDbLayerCapacity;
	private boolean _appLayerScaleFlag = false; 
	private boolean _dbLayerScaleFlag = false; 
	private int _appTimer = 0;
	private int _dbTimer = 0;
	private boolean _appTimerFlag = false;
	private boolean _dbTimerFlag = false; 
	
	public void Setup()
	{
		this._currentAppLayerCapacity = appVmCapacityPerMinute;
		this._appLayerVmCount = 1;
		this._currentDbLayerCapacity = dbVmCapacityPerMinute;
		this._dbLayerVmCount = 1;
	}


	public String AppLayerDecisionMaker(double currentLoad)
	{
		String action = "N";
		this.AppTimerTick();
		if (this._appLayerScaleFlag)
		{
			System.out.println(_currentAppLayerCapacity);
			return action;
		}
		if (currentLoad > CalculateAppLayerCapacity())
		{
			this._appLayerVmCount++;
			this._currentAppLayerCapacity += appVmCapacityPerMinute;
			action = "BU";
			this.SetAppTimer();
		}
		else if(currentLoad < (CalculateAppLayerCapacity() - appVmCapacityPerMinute))
		{
			this._appLayerVmCount--;
			this._currentAppLayerCapacity -= appVmCapacityPerMinute;
			action = "BD";
			this.SetAppTimer();
		}
		System.out.println(_currentAppLayerCapacity);
		//System.out.println("load: "+currentLoad + " action: "+action);
		return action;
	}
	
	private double CalculateAppLayerCapacity() {
		return  this._currentAppLayerCapacity;
	}

	public String SimpleAppLayerDecisionMaker(double currentLoad)
	{
		String action = "N";
		if (currentLoad > CalculateAppLayerCapacity())
		{
			_appLayerVmCount++;
			_currentAppLayerCapacity += appVmCapacityPerMinute;
			action = "BU";
		}
		else if(currentLoad < (CalculateAppLayerCapacity() - appVmCapacityPerMinute))
		{
			_appLayerVmCount--;
			_currentAppLayerCapacity -= appVmCapacityPerMinute;
			action = "BD";
		}
//		System.out.println(action);
		return action;
	}
	
	public String DBLayerDecisionMaker(double currentLoad)
	{
		String action = "N";
		this.DBTimerTick();
		if (this._dbLayerScaleFlag)
		{
			//System.out.println(_currentDbLayerCapacity);
			return action;
		}
		if (currentLoad > this._currentDbLayerCapacity)
		{
			this._dbLayerVmCount++;
			this._currentDbLayerCapacity += dbVmCapacityPerMinute;
			action = "DU";
			this.SetDBTimer();
		}
		else if(currentLoad < (this._currentDbLayerCapacity - dbVmCapacityPerMinute))
		{
			this._dbLayerVmCount--;
			this._currentDbLayerCapacity -= dbVmCapacityPerMinute;
			action = "DD";
			this.SetDBTimer();
		}
		//System.out.println(_currentDbLayerCapacity);
		return action;
	}
	
	private void AppTimerTick()
	{
		if (!this._appTimerFlag)
			this._appLayerScaleFlag = false; 
		else
		{
			this._appTimer += monitoringInterval;
			if (this._appTimer >= appLayerVmBootUpTime)
			{
				this._appTimerFlag = false; 
				this._appLayerScaleFlag = false; 
			}
		}
		
	}
	
	private void DBTimerTick()
	{
		if (!this._dbTimerFlag)
			this._dbLayerScaleFlag = false; 
		else
		{
			this._dbTimer += monitoringInterval;
			if (this._dbTimer >= dbLayerVmBootUpTime)
			{
				this._dbTimerFlag = false; 
				this._dbLayerScaleFlag = false; 
			}
		}
		
	}
	
	private void SetAppTimer()
	{
		this._appTimer = 0;
		this._appTimerFlag = true;
		this._appLayerScaleFlag = true;
	}
	
	private void SetDBTimer()
	{
		this._dbTimer = 0;
		this._dbTimerFlag = true;
		this._dbLayerScaleFlag = true;
	}
	public void Run(int workload)
	{
		this.Setup();
//		char appLayerScalingDecision = this.AppLayerDecisionMaker(workload); 
//		char dbLayerScalingDecision = this.DBLayerDecisionMaker((int)(workload * _p + 0.5));
//		System.out.println("Application layer scaling decision is: " + appLayerScalingDecision);
//		System.out.println("Database layer scaling decision is: " + dbLayerScalingDecision);
		System.out.println("Number of VMs in Application layer: " + _appLayerVmCount);
		System.out.println("Number of VMs in Database layer: " + _dbLayerVmCount);
	}

}
